package com.blogPessoal.blogPessoal.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired//injeção de dependencia
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService); //loadByusuario e verifica se possui um cliente autenticado - UserDetailService
	}
	
	@Bean// usado quando você precisa explicitamente configurar o bean ao invés de deixar o spring automaticamente fazer. 
	public PasswordEncoder passwordEnconder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		.antMatchers("/usuarios/logar").permitAll()//liberar alguns caminhos do meu controller para o client não precisar de tokens e tenha acesso
		.antMatchers("/usuarios/cadastrar").permitAll()//tanto cadastrar quanto logar serão liberados dentro da API
		.anyRequest().authenticated()// as demais requisições precisaram de token - deverão ser atenticadas
		.and().httpBasic() // utilizar o padrão basic para gerar a chave token
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //indicar qual o tipo de sessão que vamos utilizar | sessonCreation -> não vai guardar sessão(stateless)
		.and().cors() //habilitar o cors
		.and().csrf().disable(); //vou utilizar o padrão da arquitetura csrf e não personalizada
	}
	
}
