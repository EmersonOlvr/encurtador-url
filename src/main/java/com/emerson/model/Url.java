package com.emerson.model;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.emerson.errorgroups.url.*;

@Entity
@Table(name="urls")
public class Url {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=2048, nullable=false)
	@NotBlank(message="Insira uma URL.", groups=UrlOriginalNotBlankGroup.class)
	private String urlOriginal;
	
	@Column(length=64, unique=true)
	@Pattern(regexp="^[A-z0-9]*$", message="Nome inválido: somente letras e números são permitidos.", groups=UrlEncurtadaPatternGroup.class)
	@Size(max=64, message="Nome muito grande. Máximo de 64 caracteres.", groups=UrlEncurtadaSizeMaxGroup.class)
	private String urlEncurtada;
	
	private boolean personalizada;
	private int acessos;
	
	// getters e setters
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrlOriginal() {
		return urlOriginal;
	}
	public void setUrlOriginal(String urlOriginal) {
		// atribui http:// no link se não tiver
		if (!urlOriginal.contains("://")) {
			this.urlOriginal = "http://"+urlOriginal;
		} else {
			this.urlOriginal = urlOriginal;
		}
	}
	public String getUrlEncurtada() {
		return urlEncurtada;
	}
	public void setUrlEncurtada(String personalizacao) {
		// se não receber nenhuma personalização gera uma aleatória
		if (personalizacao.isEmpty()) {
			this.urlEncurtada = this.gerarNome(4);
			this.personalizada = false;
		} else {
			this.urlEncurtada = personalizacao;
			this.personalizada = true;
		}
	}
	public boolean isPersonalizada() {
		return personalizada;
	}
	public void setPersonalizada(boolean personalizada) {
		this.personalizada = personalizada;
	}
	public int getAcessos() {
		return acessos;
	}
	public void setAcessos(int acessos) {
		this.acessos = acessos;
	}
	
	@Override
	public String toString() {
		return "Url [id=" + id + ", urlOriginal=" + urlOriginal + ", urlEncurtada=" + urlEncurtada + ", personalizada="
				+ personalizada + ", acessos=" + acessos + "]";
	}
	
	public String gerarNome(int tamanho) {
		// se tamanho = 2: 3.782 links possíveis
		// se tamanho = 3: 226.920 links possíveis
		// se tamanho = 4: 13.388.280 links possíveis
		char[] letras = "bZwCF8gSak4Ru1KVoz2yJXQrPTmi7Mc3BH0Lv5YtsWlOIDdfN6qGejAp9xUEnh".toCharArray(); // 62 caracteres
		StringBuffer nome = new StringBuffer();
		
		for (int i = 0; i < tamanho; i++) {
			Random rand = new Random();
			nome.append(letras[rand.nextInt(letras.length)]);
		}
		
		return nome.toString();
	}
	
}
