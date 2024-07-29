package com.github.wesleyav.desafioapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.wesleyav.desafioapi.entities.Usuarios;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {

}
