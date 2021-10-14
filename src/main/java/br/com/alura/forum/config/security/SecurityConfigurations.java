package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

//dentro dessa classe estão todas as configurações de segurança do projeto
//habilita o spring security
@EnableWebSecurity
//carrega e lê as configurações dentro desta classe
@Configuration

//carrega a classe de acordo com o profile ativo
@Profile("prod")
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoService autenticacaoService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	// método que faz a injeção de dependências do authManager
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// faz a configuração de autenticação(login)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// informa qual classe(service) possui a lógica de autenticação
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());

	}

	// faz a configuração de autorização(urls e perfil de acesso)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/topicos").permitAll()
				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll().antMatchers(HttpMethod.POST, "/auth").permitAll()
				// este endpoint devolve infos sensíveis sobre a aplicação
				// nao deve ser mostrado em produção
				.antMatchers(HttpMethod.GET, "/actuator/**").permitAll().antMatchers(HttpMethod.DELETE, "/topicos/*")
				.hasRole("MODERADOR")
				// só permite disparar requisições para esse endereço se o cliente estivar
				// autenticado
				// somentes as urls que não foram configuradas terão exigencia de autenticação
				.anyRequest().authenticated()
				// disabilita a proteção desnecessária
				.and().csrf().disable()
				// indica que a aplicação é statless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				// indica ao Spring gerar um fomulário de atenticação
				// .and().formLogin();
				.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository),
						UsernamePasswordAuthenticationFilter.class);
	}

	// faz configuraçoes de recursos estáticos(js, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {

		// libera o swagger
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**",
				"/swagger-resources/**");

	}

}
