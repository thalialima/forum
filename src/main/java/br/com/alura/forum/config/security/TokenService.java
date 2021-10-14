package br.com.alura.forum.config.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	// Injetando parânmetros do application.properties
	@Value("${forum.jwt.expiration}")
	private String expiration;

	// Injetando parâmetros do application.properties
	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {

		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

		return Jwts.builder().setIssuer("Api do fórum da Alura").setSubject(logado.getId().toString()).setIssuedAt(hoje)
				.setExpiration(dataExpiracao).signWith(SignatureAlgorithm.HS256, secret)
				// transforma isso em uma String
				.compact();
	}

	public boolean isTokenValido(String token) {

		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}

	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		//pega o id do usuário
		
		return Long.parseLong(claims.getSubject());
	}

}
