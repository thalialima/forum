package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication

//com essa anotaçãoo suporte é habilitado para o spring pegar os campos de ordenação
@EnableSpringDataWebSupport

//habilita o uso de cache na aplicação
@EnableCaching

//habilita o swagger no projeto
@EnableSwagger2
public class ForumApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
