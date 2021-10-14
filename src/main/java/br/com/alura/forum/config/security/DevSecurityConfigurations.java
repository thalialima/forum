package br.com.alura.forum.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//representas as configurações de segurança do projeto do ambiente dev
//habilita o spring security
@EnableWebSecurity
//carrega e lê as configurações dentro desta classe
@Configuration
//carrega a classe de acordo com o profile ativo
@Profile("dev")
public class DevSecurityConfigurations extends WebSecurityConfigurerAdapter {
	// faz a configuração de autorização(urls e perfil de acesso)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/**").permitAll()
		
			//disabilita a proteção desnecessária
			.and().csrf().disable();
	}

}
