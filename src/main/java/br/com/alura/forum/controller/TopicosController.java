package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

//primeiro endpoint
//usando @RestController, o Spring assume que por padrão todos os métos já vão conter
//o @ResponseBody
@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	// read
	@GetMapping

	// Indica que o retorno desse método deve ser guardado em cache
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable paginacao) {

		if (nomeCurso == null) {
			// findAll faz uma consulta carregando todos os registros do BD
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDTO.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
			return TopicoDTO.converter(topicos);
		}
	}

	// atualiza a cache
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	// create/save
	@PostMapping
	@Transactional
	// TopocoForm é um DTO para receber dados do cliente
	// @RequestBody indica que os parâmentros devem ser pegos do corpo da requisição
	// o código 201 indica que algo foi criado com sucesso no servidor
	// @Valid faz com que as validações do bean validation sejam executadas
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.toTopico(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		// devolve o código 201
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}

	// read
	@GetMapping("/{id}")
	// @PathVariable indica que a variavel vem da url
	public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id) {
		// findById recupera um optional pelo id
		// no optional pode ser que tenha um registro, pode ser que não
		Optional<Topico> topico = topicoRepository.findById(id);

		if (topico.isPresent())
			return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get()));

		return ResponseEntity.notFound().build();
	}

	// atualiza a cache
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	// update
	@PutMapping("/{id}")
	// @Transactional avisa ao Sring para commitar a transação no final do método
	@Transactional
	// O método PUT sobrescreve o recurso inteiro(atualia todas as infos)
	// já o PACTH faz a atualização de alguns campos do recurso
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {

		Optional<Topico> optional = topicoRepository.findById(id);

		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDTO(topico));
		}

		return ResponseEntity.notFound().build();

	}

	// atualiza a cache
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	// delete
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);

		if (optional.isPresent()) {
			topicoRepository.deleteById(id);

			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}

}
