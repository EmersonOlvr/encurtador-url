package com.emerson.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public ModelAndView encurtar(Model model, @ModelAttribute @Validated(value=ValidationSequence.class) Url url, BindingResult result) {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("url", url);
		
		if (result.hasErrors()) {
			return mv;
		}
		
		// se a url não estiver personalizada...
		if (!url.isPersonalizada()) {
			// busca esta url no banco...
			List<Url> lista = this.urlRep.findByUrlOriginalAndPersonalizada(url.getUrlOriginal(), false);
			// se já existir alguma...
			if (lista.size() > 0) {
				System.out.println("URL já cadastrada ("+url.getUrlOriginal()+"). Redirecionando para o preview ("+lista.get(0).getUrlEncurtada()+")...");
				// redireciona para o preview desta url
				return new ModelAndView("redirect:/"+lista.get(0).getUrlEncurtada()+"+");
			// senão...
			} else {
				// gera uma url encurtada única...
				while (this.urlRep.findByUrlEncurtada(url.getUrlEncurtada()).size() > 0) {
					url.setUrlEncurtada(url.gerarNome(4));
				}
				// salva no banco
				this.urlRep.save(url);
				System.out.println("Nova URL cadastrada: "+url.getUrlOriginal()+" ("+url.getUrlEncurtada()+").");
				return new ModelAndView("redirect:/"+url.getUrlEncurtada()+"+");
			}
		// se a url estiver personalizada...
		} else {
			// busca urls no banco com esta mesma personalização
			List<Url> lista = this.urlRep.findByUrlEncurtada(url.getUrlEncurtada());
			// se já existir alguma...
			if (lista.size() > 0) {
				// adiciona um novo erro para ser exibido na view
				result.addError(new ObjectError("personalizacaoExistente", "Personalização já existente."));
				System.out.println("Erro: personalização já existente ("+url.getUrlEncurtada()+").");
				return mv;
			// se não existir...
			} else {
				// salva no banco
				this.urlRep.save(url);
				System.out.println("Nova URL (personalizada) cadastrada: "+url.getUrlOriginal()+" ("+url.getUrlEncurtada()+").");
				return new ModelAndView("redirect:/"+url.getUrlEncurtada()+"+");
			}
		}
	}
	@GetMapping("/{url_encurtada}")
	public String redirecionar(@PathVariable String url_encurtada) {
		Url url;
		// busca no banco a url encurtada
		List<Url> lista = this.urlRep.findByUrlEncurtada(url_encurtada);
		// se existir alguma...
		if (lista.size() > 0) {
			// obtém a primeira (e só poderia ter 1 mesmo)
			url = lista.get(0);
		} else {
			System.out.println("Erro: URL não cadastrada ("+url_encurtada+").");
			return "redirect:/";
		}
		// incrementa os acessos e atualiza no banco
		url.setAcessos(url.getAcessos() + 1);
		this.urlRep.save(url);
		
		System.out.println("Redirecionando para "+url.getUrlOriginal()+" ("+url_encurtada+")...");
		return "redirect:"+url.getUrlOriginal();
	}
	@GetMapping("/{url_encurtada}+")
	public ModelAndView preview(@PathVariable String url_encurtada) {
		Url url;
		// busca no banco a url encurtada
		List<Url> lista = this.urlRep.findByUrlEncurtada(url_encurtada);
		// se existir alguma...
		if (lista.size() > 0) {
			// obtém a primeira (e só poderia ter 1 mesmo)
			url = lista.get(0);
		} else {
			System.out.println("Erro: URL não cadastrada ("+url_encurtada+").");
			return new ModelAndView("redirect:/");
		}
		ModelAndView mv = new ModelAndView("preview");
		// adiciona as informações desta url no preview
		mv.addObject("url", url);
		return mv;
	}
	
}
