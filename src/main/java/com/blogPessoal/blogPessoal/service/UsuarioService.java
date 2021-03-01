package com.blogPessoal.blogPessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogPessoal.blogPessoal.model.UserLogin;
import com.blogPessoal.blogPessoal.model.Usuario;
import com.blogPessoal.blogPessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	public Usuario CadastrarUsuario(Usuario usuario) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String senhaEncoder = encoder.encode(usuario.getSenha());//string armazenando a senha digitada no cadastro mas incriptada pelo encoder.encode()
		usuario.setSenha(senhaEncoder);//setando senha já encriptada
		
		return repository.save(usuario);//salvando no BD o usuario com a senha criptografada
	}
	
	public Optional<UserLogin> Logar(Optional<UserLogin> user){
		//retornar para o cliente as informações nome/usuario/senha
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
		
		if(usuario.isPresent()) {//se retornar algum usuario do BD
			if(encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {//matches está verificando se as senhas encriptadas são iguais
				String auth = user.get().getUsuario()+":"+user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader= "Basic "+ new String(encodedAuth);
				
				user.get().setToken(authHeader);//passando a criptografia basica - outro modelo de criptografia de token
				user.get().setNome(usuario.get().getNome());
				//seto o token e o nome para conseguir atribuir informação ao useLogin q está ativo no momento do login
				return user;
						}
			}
		
		return null;
	}
	
}
