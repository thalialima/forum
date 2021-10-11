package br.com.alura.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

	//O _ indica que é o atributo de uma agregacao e não o atributo da classe
	Page<Topico> findByCurso_Nome(String nomeCurso, Pageable paginacao);

	
}
