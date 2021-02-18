package com.blogPessoal.blogPessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogPessoal.blogPessoal.model.Postagem;
import com.blogPessoal.blogPessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")//aceitar requisições de qlqr origem
public class PostagemController {

	@Autowired
	private PostagemRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Postagem>> GetAll(){
		return ResponseEntity.ok(repository.findAll());//vai retornar a lista de postagens
	}
	
	@GetMapping("/{id}")//qaundo tiver alguma requisição com o ponstagens/id será acessado este metodo
	public ResponseEntity<Postagem> GetById(@PathVariable long id){//@PatchVariable - pegar o valor da url e colocar na variavel id
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp)) //devolver o objeto do tipo postagem
				.orElse(ResponseEntity.notFound().build()); //devolver um not found
	}
	
	@GetMapping("/titulo/{titulo}")//não se confundir com a rota do metodo do GetById
	public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	@PostMapping
	public ResponseEntity<Postagem> post(@RequestBody Postagem postagem){//@RequestBody - Para Recepcionar os valores/objetos que são passadas via body para nossa aplicação
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}
	
	
}
