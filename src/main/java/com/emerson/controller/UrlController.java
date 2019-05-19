package com.emerson.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.emerson.dao.UrlDAO;
import com.emerson.errorgroups.ValidationSequence;
import com.emerson.model.Url;

@Controller
public class UrlController {

	@Autowired
	private UrlDAO urlRep;
	
	@GetMapping("/")
	public ModelAndView viewIndex() {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("url", new Url());
		return mv;
	}
	@PostMapping("/")
	public ModelAndView encurtar(@ModelAttribute @Validated(value=ValidationSequence.class) Url url, BindingResult result) {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("url", url);
		
		if (result.hasFieldErrors()) {
			return mv;
		}
		
		if (!url.isPersonalizado()) {
			List<Url> lista = this.urlRep.findByLinkAndPersonalizado(url.getLink(), false);
			if (lista.size() > 0) {
				System.out.println("URL já cadastrada ("+url.getLink()+"). Redirecionando para o preview ("+lista.get(0).getNome()+")...");
				return new ModelAndView("redirect:/"+lista.get(0).getNome()+"+");
			} else {
				while (this.urlRep.findByNome(url.getNome()).size() > 0) {
					url.setNome(url.GerarNome(4));
				}
				this.urlRep.save(url);
				System.out.println("Nova URL cadastrada: "+url.getLink()+" ("+url.getNome()+").");
				return new ModelAndView("redirect:/"+url.getNome()+"+");
			}
		} else {
			List<Url> lista = this.urlRep.findByNome(url.getNome());
			if (lista.size() > 0) {
				System.out.println("Erro: personalização já em uso ("+url.getNome()+")!");
				result.addError(new ObjectError("personalizacaoEmUso", "Personalização já em uso!"));
				return mv;
			} else {
				this.urlRep.save(url);
				System.out.println("Nova URL (personalizada) cadastrada: "+url.getLink()+" ("+url.getNome()+").");
				return new ModelAndView("redirect:/"+url.getNome()+"+");
			}
		}
	}
	@GetMapping("/{nome}")
	public String redirecionar(@PathVariable String nome) {
		Url url;
		try {
			// obtém a linha do banco que tem o nome {nome}
			url = this.urlRep.findByNome(nome).get(0);
		} catch(Exception e) {
			System.out.println("Erro: não existe nenhum link com este nome: "+nome);
			return "redirect:/";
		}
		// incrementa os acessos
		url.setAcessos(url.getAcessos() + 1);
		
		// atualiza a url no banco de dados (os acessos)
		this.urlRep.save(url);
		
		System.out.println("Redirecionando para: "+url.getLink()+" ("+nome+")...");
		return "redirect:"+url.getLink();
	}
	@GetMapping("/{nome}+")
	public ModelAndView preview(@PathVariable String nome) {
		Url url;
		try {
			// obtém a linha do banco que tem o nome {nome}
			url = this.urlRep.findByNome(nome).get(0);
		} catch(Exception e) {
			System.out.println("Erro: não existe nenhum link com este nome: "+nome);
			return new ModelAndView("redirect:/");
		}
		ModelAndView mv = new ModelAndView("preview");
		mv.addObject("url", url);
		
		return mv;
	}
	
}
