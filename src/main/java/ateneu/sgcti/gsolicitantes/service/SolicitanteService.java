package ateneu.sgcti.gsolicitantes.service;

import ateneu.sgcti.auth.entity.UsuarioEntity;
import ateneu.sgcti.auth.repository.UsuarioRepository;
import ateneu.sgcti.gchamados.repository.ChamadoRepository;
import ateneu.sgcti.gsolicitantes.dto.SolicitanteRequest;
import ateneu.sgcti.gsolicitantes.dto.SolicitanteResponse;
import ateneu.sgcti.gsolicitantes.entity.SolicitanteEntity;
import ateneu.sgcti.gsolicitantes.exception.SolicitanteBusinessException;
import ateneu.sgcti.gsolicitantes.exception.SolicitanteNotFoundException;
import ateneu.sgcti.gsolicitantes.repository.SolicitanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitanteService {

    private final SolicitanteRepository solicitanteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChamadoRepository chamadoRepository;

    @Transactional(readOnly = true)
    public List<SolicitanteResponse> listarTodos() {
        return solicitanteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitanteResponse buscarPorId(Integer id) {
        return toResponse(buscarEntidadeOuFalha(id));
    }

    public SolicitanteResponse cadastrar(SolicitanteRequest request) {
        validarEmailUnico(request.email(), null);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(normalizar(request.nome()));
        usuario.setEmail(normalizar(request.email()));
        usuario.setSenha(normalizar(request.senha()));
        usuario = usuarioRepository.save(usuario);

        SolicitanteEntity solicitante = new SolicitanteEntity();
        solicitante.setUsuarioEntity(usuario);
        solicitante.setSetor(normalizar(request.setor()));
        solicitante.setTelefone(normalizar(request.telefone()));

        return toResponse(solicitanteRepository.save(solicitante));
    }

    public SolicitanteResponse atualizar(Integer id, SolicitanteRequest request) {
        SolicitanteEntity solicitante = buscarEntidadeOuFalha(id);
        UsuarioEntity usuario = solicitante.getUsuarioEntity();

        validarEmailUnico(request.email(), usuario.getId());

        usuario.setNome(normalizar(request.nome()));
        usuario.setEmail(normalizar(request.email()));
        if (StringUtils.hasText(request.senha())) {
            usuario.setSenha(normalizar(request.senha()));
        }
        usuarioRepository.save(usuario);

        solicitante.setSetor(normalizar(request.setor()));
        solicitante.setTelefone(normalizar(request.telefone()));
        return toResponse(solicitanteRepository.save(solicitante));
    }

    public void excluir(Integer id) {
        SolicitanteEntity solicitante = buscarEntidadeOuFalha(id);

        if (chamadoRepository.existsBySolicitanteEntity_Id(id)) {
            throw new SolicitanteBusinessException("Não é possível excluir o solicitante porque existem chamados vinculados.");
        }

        solicitanteRepository.delete(solicitante);
        usuarioRepository.delete(solicitante.getUsuarioEntity());
    }

    private SolicitanteEntity buscarEntidadeOuFalha(Integer id) {
        return solicitanteRepository.findById(id)
                .orElseThrow(() -> new SolicitanteNotFoundException(id));
    }

    private void validarEmailUnico(String email, Integer usuarioIdAtual) {
        usuarioRepository.findByEmail(normalizar(email)).ifPresent(usuario -> {
            if (usuarioIdAtual == null || !usuarioIdAtual.equals(usuario.getId())) {
                throw new SolicitanteBusinessException("Já existe um usuário cadastrado com este e-mail.");
            }
        });
    }

    private SolicitanteResponse toResponse(SolicitanteEntity solicitante) {
        UsuarioEntity usuario = solicitante.getUsuarioEntity();
        return new SolicitanteResponse(
                solicitante.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                solicitante.getSetor(),
                solicitante.getTelefone()
        );
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
