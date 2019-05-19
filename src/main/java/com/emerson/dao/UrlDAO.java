package com.emerson.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emerson.model.Url;

public interface UrlDAO extends JpaRepository<Url, Integer>{

	List<Url> findByNome(String nome);
	List<Url> findByLink(String link);
	List<Url> findByLinkAndPersonalizado(String link, boolean personalizado);
	
}
