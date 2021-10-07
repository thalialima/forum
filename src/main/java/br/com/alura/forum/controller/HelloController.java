package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {

	//url
	@RequestMapping("/")
	//devolve a string direto para o navegador
	@ResponseBody
	public String hello() {
		return "<h1>Hello World!</h1>";
	}
}
