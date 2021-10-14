package br.com.alura.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.forum.modelo.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {

	@Bean
	public Docket forumApi() {
		//informa o tipo de documentação
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				//informa o pacote base(a partir de qual pacote ele começará a ler as classes)
				.apis(RequestHandlerSelectors.basePackage("br.com.alura.forum"))
				//quais endpoints serão feitas análises
				.paths(PathSelectors.ant("/**"))
				.build()
				//manda ignorar as urls que trabalham com usuário
				//para que os dados do usuário não apareçao na interface de documentação
				.ignoredParameterTypes(Usuario.class)
				.globalOperationParameters(Arrays.asList(
						new ParameterBuilder()
						.name("Authorization")
						.description("Header para token JWT")
						.modelRef(new ModelRef("string"))
						.parameterType("header")
						.required(false)
						.build()));
	}
}
