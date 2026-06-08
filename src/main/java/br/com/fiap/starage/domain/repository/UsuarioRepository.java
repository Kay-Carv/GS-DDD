package br.com.fiap.starage.domain.repository;

import br.com.fiap.starage.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    UserDetails findByEmail(String email);
    }
