package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

	//O _ indica que é o atributo de uma agregacao e não o atributo da classe
	List<Topico> findByCurso_Nome(String nomeCurso);

	
}
