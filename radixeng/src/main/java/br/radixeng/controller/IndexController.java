package br.radixeng.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class IndexController {

	

	@RequestMapping("/rotas")
	public String index(){
		return "index";
	
}
}