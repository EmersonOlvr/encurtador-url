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
	@NotBlank(message="Insira uma URL.", groups=LinkNotBlankGroup.class)
	private String link;
	
	@Column(length=64, unique=true)
	@Pattern(regexp="^[A-z0-9]*$", message="Nome inválido: somente letras e números são permitidos.", groups=NomePatternGroup.class)
	@Size(max=64, message="Nome muito grande. Máximo de 64 caracteres.", groups=NomeSizeMaxGroup.class)
	private String nome;
	
	private boolean personalizado;
	private int acessos;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		if (!link.contains("://")) {
			this.link = "http://"+link;
		} else {
			this.link = link;
		}
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		if (nome.isEmpty()) {
			this.nome = this.gerarNome(4);
			this.personalizado = false;
		} else {
			this.nome = nome;
			this.personalizado = true;
		}
	}
	public boolean isPersonalizado() {
		return personalizado;
	}
	public void setPersonalizado(boolean personalizado) {
		this.personalizado = personalizado;
	}
	public int getAcessos() {
		return acessos;
	}
	public void setAcessos(int acessos) {
		this.acessos = acessos;
	}
	
	@Override
	public String toString() {
		return "Url [id=" + id + ", link=" + link + ", nome=" + nome + ", personalizado=" + personalizado + ", acessos="
				+ acessos + "]";
	}
	
	public String gerarNome(int tamanho) {
		char[] letras = "bZwCF8gSak4Ru1KVoz2yJXQrPTmi7Mc3BH0Lv5YtsWlOIDdfN6qGejAp9xUEnh".toCharArray();
		StringBuffer nome = new StringBuffer();
		
		for (int i = 0; i < tamanho; i++) {
			Random rand = new Random();
			nome.append(letras[rand.nextInt(letras.length)]);
		}
		
		return nome.toString();
	}
	
}
