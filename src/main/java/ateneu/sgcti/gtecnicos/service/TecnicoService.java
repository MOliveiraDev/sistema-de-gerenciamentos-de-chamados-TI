package ateneu.sgcti.gtecnicos.service;

import ateneu.sgcti.auth.entity.UsuarioEntity;
import ateneu.sgcti.auth.enums.Role;
import ateneu.sgcti.auth.repository.UsuarioRepository;
import ateneu.sgcti.gchamados.repository.ChamadoRepository;
import ateneu.sgcti.gsolicitantes.repository.SolicitanteRepository;
import ateneu.sgcti.gtecnicos.dto.TecnicoRequest;
import ateneu.sgcti.gtecnicos.dto.TecnicoResponse;
import ateneu.sgcti.gtecnicos.dto.TecnicoUpdateRequest;
import ateneu.sgcti.gtecnicos.entity.TecnicoEntity;
import ateneu.sgcti.gtecnicos.exception.TecnicoBusinessException;
import ateneu.sgcti.gtecnicos.exception.TecnicoNotFoundException;
import ateneu.sgcti.gtecnicos.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final ChamadoRepository chamadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<TecnicoResponse> listarTodos() {
        return tecnicoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TecnicoResponse buscarPorId(Integer id) {
        return toResponse(buscarEntidadeOuFalha(id));
    }

    public TecnicoResponse cadastrar(TecnicoRequest request) {
        validarEmailUnico(request.email(), null);
        validarCpfUnico(request.cpf(), null);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(normalizar(request.nome()));
        usuario.setEmail(normalizar(request.email()));
        usuario.setSenha(passwordEncoder.encode(normalizar(request.senha())));
        usuario.setRole(Role.TECNICO);
        usuario = usuarioRepository.save(usuario);

        TecnicoEntity tecnico = new TecnicoEntity();
        tecnico.setUsuarioEntity(usuario);
        tecnico.setCpf(normalizar(request.cpf()));
        tecnico.setEspecialidade(normalizar(request.especialidade()));
        tecnico.setTelefone(normalizar(request.telefone()));

        return toResponse(tecnicoRepository.save(tecnico));
    }

    public TecnicoResponse atualizar(Integer id, TecnicoUpdateRequest request) {
        TecnicoEntity tecnico = buscarEntidadeOuFalha(id);
        UsuarioEntity usuario = tecnico.getUsuarioEntity();

        validarEmailUnico(request.email(), usuario.getId());
        validarCpfUnico(request.cpf(), tecnico.getId());

        usuario.setNome(normalizar(request.nome()));
        usuario.setEmail(normalizar(request.email()));
        if (StringUtils.hasText(request.senha())) {
            usuario.setSenha(passwordEncoder.encode(normalizar(request.senha())));
        }
        usuarioRepository.save(usuario);

        tecnico.setCpf(normalizar(request.cpf()));
        tecnico.setEspecialidade(normalizar(request.especialidade()));
        tecnico.setTelefone(normalizar(request.telefone()));
        return toResponse(tecnicoRepository.save(tecnico));
    }

    public void excluir(Integer id) {
        TecnicoEntity tecnico = buscarEntidadeOuFalha(id);

        if (chamadoRepository.existsByTecnicoEntity_Id(id)) {
            throw new TecnicoBusinessException("Não é possível excluir o técnico porque existem chamados vinculados.");
        }

        Integer usuarioId = tecnico.getUsuarioEntity().getId();
        if (solicitanteRepository.findByUsuarioEntity_Id(usuarioId).isPresent()) {
            throw new TecnicoBusinessException("Não é possível excluir o usuário porque ele também está vinculado a um solicitante.");
        }

        tecnicoRepository.delete(tecnico);
        usuarioRepository.delete(tecnico.getUsuarioEntity());
    }

    private TecnicoEntity buscarEntidadeOuFalha(Integer id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new TecnicoNotFoundException(id));
    }

    private void validarEmailUnico(String email, Integer usuarioIdAtual) {
        usuarioRepository.findByEmail(normalizar(email)).ifPresent(usuario -> {
            if (usuarioIdAtual == null || !usuarioIdAtual.equals(usuario.getId())) {
                throw new TecnicoBusinessException("Já existe um usuário cadastrado com este e-mail.");
            }
        });
    }

    private void validarCpfUnico(String cpf, Integer tecnicoIdAtual) {
        tecnicoRepository.findByCpf(normalizar(cpf)).ifPresent(tecnico -> {
            if (tecnicoIdAtual == null || !tecnicoIdAtual.equals(tecnico.getId())) {
                throw new TecnicoBusinessException("Já existe um técnico cadastrado com este CPF.");
            }
        });
    }

    private TecnicoResponse toResponse(TecnicoEntity tecnico) {
        UsuarioEntity usuario = tecnico.getUsuarioEntity();
        return new TecnicoResponse(
                tecnico.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                tecnico.getCpf(),
                tecnico.getEspecialidade(),
                tecnico.getTelefone()
        );
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
