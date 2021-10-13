package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//dentro dessa classe estão todas as configurações de segurança do projeto
//habilita o spring security
@EnableWebSecurity
//carrega e lê as configurações dentro desta classe
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	//faz a configuração de autenticação(login)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//informa qual classe(service) possui a lógica de autenticação
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
		
	}

	//faz a configuração de autorização(urls e perfil de acesso)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll()
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
			//só permite disparar requisições para esse endereço se o cliente estivar autenticado
			//somentes as urls que não foram configuradas terão exigencia de autenticação
			.anyRequest().authenticated()
			//indica ao Spring gerar um fomulário de atenticação
			.and().formLogin();
	}

	//faz configuraçoes de recursos estáticos(js, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {

	}
	

}