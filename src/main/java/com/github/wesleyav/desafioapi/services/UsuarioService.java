package com.github.wesleyav.desafioapi.services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.wesleyav.desafioapi.entities.Usuarios;
import com.github.wesleyav.desafioapi.repositories.UsuarioRepository;
import com.github.wesleyav.desafioapi.services.exceptions.ResourceEmptyException;
import com.github.wesleyav.desafioapi.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

	private final UsuarioRepository userRepository;

	public UsuarioService(UsuarioRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public Page<Usuarios> findAllPaged(Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Page<Usuarios> userPage = userRepository.findAll(pageable);

		if (userPage.isEmpty()) {
			throw new ResourceEmptyException("Não foram encontrados registros, base vazia.");
		}

		return userPage;
	}

	@Transactional
	public Usuarios findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceEmptyException("Não foram encontrados registros, base vazia."));
	}

	@Transactional
	public Usuarios create(Usuarios userToCreate) {
		return userRepository.save(userToCreate);
	}

	@Transactional
	public void delete(Long id) {
		try {
			if (!userRepository.existsById(id)) {
				throw new ResourceNotFoundException(id);
			}
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@Transactional
	public Usuarios update(Long id, Usuarios userToUpdate) {
		Usuarios dbUser = this.findById(id);
		dbUser.setId(id);
		if (dbUser.getId().equals(userToUpdate.getId())) {
			throw new ResourceNotFoundException("Update IDs must be the same.");
		}

		dbUser.setName(userToUpdate.getName());
		dbUser.setAccount(userToUpdate.getAccount());
		dbUser.setCard(userToUpdate.getCard());
		dbUser.setFeatures(userToUpdate.getFeatures());
		dbUser.setNews(userToUpdate.getNews());

		return this.userRepository.save(dbUser);
	}

}
