package ateneu.sgcti.gchamados.service;

import ateneu.sgcti.auth.enums.Role;
import ateneu.sgcti.gchamados.dto.AtribuirTecnicoRequest;
import ateneu.sgcti.gchamados.dto.AtualizarStatusRequest;
import ateneu.sgcti.gchamados.dto.ChamadoHistoricoResponse;
import ateneu.sgcti.gchamados.dto.ChamadoRequest;
import ateneu.sgcti.gchamados.dto.ChamadoResponse;
import ateneu.sgcti.gchamados.dto.ChamadoUpdateRequest;
import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import ateneu.sgcti.gchamados.entity.ChamadoHistoricoEntity;
import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;
import ateneu.sgcti.gchamados.exception.ChamadoBusinessException;
import ateneu.sgcti.gchamados.exception.ChamadoNotFoundException;
import ateneu.sgcti.gchamados.repository.ChamadoHistoricoRepository;
import ateneu.sgcti.gchamados.repository.ChamadoRepository;
import ateneu.sgcti.gsolicitantes.entity.SolicitanteEntity;
import ateneu.sgcti.gsolicitantes.exception.SolicitanteNotFoundException;
import ateneu.sgcti.gsolicitantes.repository.SolicitanteRepository;
import ateneu.sgcti.gtecnicos.entity.TecnicoEntity;
import ateneu.sgcti.gtecnicos.exception.TecnicoNotFoundException;
import ateneu.sgcti.gtecnicos.repository.TecnicoRepository;
import ateneu.sgcti.shared.security.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ChamadoHistoricoRepository chamadoHistoricoRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final TecnicoRepository tecnicoRepository;

    @Transactional(readOnly = true)
    public List<ChamadoResponse> listar(StatusChamado status,
                                        PrioridadeChamado prioridade,
                                        Integer tecnicoId,
                                        Integer solicitanteId) {
        UsuarioPrincipal usuarioLogado = obterUsuarioLogado();
        if (usuarioLogado.getRole() == Role.SOLICITANTE) {
            validarFiltrosPermitidosParaSolicitante(prioridade, tecnicoId, solicitanteId);
            solicitanteId = obterSolicitanteIdDoUsuario(usuarioLogado.getId());
        }

        return chamadoRepository.buscarComFiltros(status, prioridade, tecnicoId, solicitanteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChamadoResponse buscarPorId(Integer id) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(id);
        validarAcessoSolicitanteAoChamado(chamado);
        return toResponse(chamado);
    }

    public ChamadoResponse registrar(ChamadoRequest request) {
        UsuarioPrincipal usuarioLogado = obterUsuarioLogado();

        SolicitanteEntity solicitante;
        TecnicoEntity tecnico;

        if (usuarioLogado.getRole() == Role.SOLICITANTE) {
            Integer solicitanteIdLogado = obterSolicitanteIdDoUsuario(usuarioLogado.getId());
            if (!solicitanteIdLogado.equals(request.solicitanteId())) {
                throw new AccessDeniedException("Solicitante só pode registrar chamados para si mesmo.");
            }
            if (request.tecnicoId() != null) {
                throw new AccessDeniedException("Solicitante não pode atribuir técnico no cadastro do chamado.");
            }

            solicitante = buscarSolicitanteOuFalha(solicitanteIdLogado);
            tecnico = null;
        } else {
            solicitante = buscarSolicitanteOuFalha(request.solicitanteId());
            tecnico = request.tecnicoId() == null ? null : buscarTecnicoOuFalha(request.tecnicoId());
        }

        ChamadoEntity chamado = new ChamadoEntity();
        chamado.setTitulo(normalizar(request.titulo()));
        chamado.setDescricao(normalizar(request.descricao()));
        chamado.setPrioridade(request.prioridade());
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setDataAtualizacao(LocalDateTime.now());
        chamado.setSolicitanteEntity(solicitante);
        chamado.setTecnicoEntity(tecnico);

        ChamadoEntity salvo = chamadoRepository.save(chamado);

        registrarHistorico(
                salvo,
                "Chamado registrado",
                null,
                StatusChamado.ABERTO,
                null,
                tecnico == null ? null : tecnico.getId()
        );

        return toResponse(salvo);
    }

    public ChamadoResponse atualizar(Integer id, ChamadoUpdateRequest request) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(id);

        chamado.setTitulo(normalizar(request.titulo()));
        chamado.setDescricao(normalizar(request.descricao()));
        chamado.setPrioridade(request.prioridade());
        chamado.setDataAtualizacao(LocalDateTime.now());

        ChamadoEntity salvo = chamadoRepository.save(chamado);
        registrarHistorico(salvo, "Dados do chamado atualizados", chamado.getStatus(), chamado.getStatus(),
                chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId(),
                chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId());

        return toResponse(salvo);
    }

    public void excluir(Integer id) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(id);
        chamadoHistoricoRepository.deleteByChamado_Id(id);
        chamadoRepository.delete(chamado);
    }

    public ChamadoResponse atribuirTecnico(Integer chamadoId, AtribuirTecnicoRequest request) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(chamadoId);
        TecnicoEntity tecnico = buscarTecnicoOuFalha(request.tecnicoId());

        Integer tecnicoAnteriorId = chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId();

        chamado.setTecnicoEntity(tecnico);
        chamado.setDataAtualizacao(LocalDateTime.now());

        ChamadoEntity salvo = chamadoRepository.save(chamado);
        registrarHistorico(salvo, "Técnico atribuído ao chamado", chamado.getStatus(), chamado.getStatus(),
                tecnicoAnteriorId, tecnico.getId());

        return toResponse(salvo);
    }

    public ChamadoResponse atualizarStatus(Integer chamadoId, AtualizarStatusRequest request) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(chamadoId);

        StatusChamado statusAnterior = chamado.getStatus();
        StatusChamado statusNovo = request.status();
        validarTransicaoStatus(statusAnterior, statusNovo);

        chamado.setStatus(statusNovo);
        chamado.setDataAtualizacao(LocalDateTime.now());

        if (statusNovo == StatusChamado.FECHADO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }

        ChamadoEntity salvo = chamadoRepository.save(chamado);
        registrarHistorico(
                salvo,
                "Status do chamado atualizado",
                statusAnterior,
                statusNovo,
                chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId(),
                chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId()
        );

        return toResponse(salvo);
    }

    public ChamadoResponse atenderChamado(Integer chamadoId, AtribuirTecnicoRequest request) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(chamadoId);
        TecnicoEntity tecnico = buscarTecnicoOuFalha(request.tecnicoId());

        if (chamado.getStatus() == StatusChamado.RESOLVIDO || chamado.getStatus() == StatusChamado.FECHADO) {
            throw new ChamadoBusinessException("Não é possível atender um chamado já resolvido ou fechado.");
        }

        StatusChamado statusAnterior = chamado.getStatus();
        Integer tecnicoAnteriorId = chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId();

        chamado.setTecnicoEntity(tecnico);
        chamado.setStatus(StatusChamado.EM_ATENDIMENTO);
        chamado.setDataAtualizacao(LocalDateTime.now());

        ChamadoEntity salvo = chamadoRepository.save(chamado);
        registrarHistorico(
                salvo,
                "Chamado em atendimento",
                statusAnterior,
                StatusChamado.EM_ATENDIMENTO,
                tecnicoAnteriorId,
                tecnico.getId()
        );

        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<ChamadoHistoricoResponse> listarHistorico(Integer chamadoId) {
        ChamadoEntity chamado = buscarEntidadeOuFalha(chamadoId);
        validarAcessoSolicitanteAoChamado(chamado);

        return chamadoHistoricoRepository.findByChamado_IdOrderByDataEventoDesc(chamadoId)
                .stream()
                .map(this::toHistoricoResponse)
                .toList();
    }

    private void validarTransicaoStatus(StatusChamado atual, StatusChamado novo) {
        if (atual == novo) {
            return;
        }

        boolean transicaoValida = switch (atual) {
            case ABERTO -> novo == StatusChamado.EM_ATENDIMENTO;
            case EM_ATENDIMENTO -> novo == StatusChamado.RESOLVIDO;
            case RESOLVIDO -> novo == StatusChamado.FECHADO;
            case FECHADO -> false;
        };

        if (!transicaoValida) {
            throw new ChamadoBusinessException("Transição de status inválida de " + atual + " para " + novo + ".");
        }
    }

    private void validarAcessoSolicitanteAoChamado(ChamadoEntity chamado) {
        UsuarioPrincipal usuarioLogado = obterUsuarioLogado();
        if (usuarioLogado.getRole() != Role.SOLICITANTE) {
            return;
        }

        Integer solicitanteIdLogado = obterSolicitanteIdDoUsuario(usuarioLogado.getId());
        if (!chamado.getSolicitanteEntity().getId().equals(solicitanteIdLogado)) {
            throw new AccessDeniedException("Solicitante só pode visualizar os próprios chamados.");
        }
    }

    private void validarFiltrosPermitidosParaSolicitante(PrioridadeChamado prioridade,
                                                          Integer tecnicoId,
                                                          Integer solicitanteId) {
        if (prioridade != null || tecnicoId != null || solicitanteId != null) {
            throw new AccessDeniedException("Solicitante só pode filtrar chamados por status.");
        }
    }

    private UsuarioPrincipal obterUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UsuarioPrincipal usuarioPrincipal)) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }
        return usuarioPrincipal;
    }

    private Integer obterSolicitanteIdDoUsuario(Integer usuarioId) {
        return solicitanteRepository.findByUsuarioEntity_Id(usuarioId)
                .map(SolicitanteEntity::getId)
                .orElseThrow(() -> new AccessDeniedException("Usuário solicitante inválido."));
    }

    private SolicitanteEntity buscarSolicitanteOuFalha(Integer solicitanteId) {
        return solicitanteRepository.findById(solicitanteId)
                .orElseThrow(() -> new SolicitanteNotFoundException(solicitanteId));
    }

    private TecnicoEntity buscarTecnicoOuFalha(Integer tecnicoId) {
        return tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new TecnicoNotFoundException(tecnicoId));
    }

    private ChamadoEntity buscarEntidadeOuFalha(Integer id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNotFoundException(id));
    }

    private void registrarHistorico(ChamadoEntity chamado,
                                    String descricaoEvento,
                                    StatusChamado statusAnterior,
                                    StatusChamado statusNovo,
                                    Integer tecnicoAnteriorId,
                                    Integer tecnicoNovoId) {
        ChamadoHistoricoEntity historico = new ChamadoHistoricoEntity();
        historico.setChamado(chamado);
        historico.setDataEvento(LocalDateTime.now());
        historico.setDescricaoEvento(descricaoEvento);
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusNovo(statusNovo);
        historico.setTecnicoAnteriorId(tecnicoAnteriorId);
        historico.setTecnicoNovoId(tecnicoNovoId);
        chamadoHistoricoRepository.save(historico);
    }

    private ChamadoResponse toResponse(ChamadoEntity chamado) {
        Integer tecnicoId = chamado.getTecnicoEntity() == null ? null : chamado.getTecnicoEntity().getId();
        String nomeTecnico = chamado.getTecnicoEntity() == null
                ? null
                : chamado.getTecnicoEntity().getUsuarioEntity().getNome();

        return new ChamadoResponse(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getPrioridade(),
                chamado.getStatus(),
                chamado.getDataAbertura(),
                chamado.getDataAtualizacao(),
                chamado.getDataFechamento(),
                chamado.getSolicitanteEntity().getId(),
                chamado.getSolicitanteEntity().getUsuarioEntity().getNome(),
                tecnicoId,
                nomeTecnico
        );
    }

    private ChamadoHistoricoResponse toHistoricoResponse(ChamadoHistoricoEntity historico) {
        return new ChamadoHistoricoResponse(
                historico.getId(),
                historico.getDataEvento(),
                historico.getDescricaoEvento(),
                historico.getStatusAnterior(),
                historico.getStatusNovo(),
                historico.getTecnicoAnteriorId(),
                historico.getTecnicoNovoId()
        );
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}

