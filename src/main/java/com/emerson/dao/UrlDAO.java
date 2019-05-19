package com.emerson.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emerson.model.Url;

public interface UrlDAO extends JpaRepository<Url, Integer>{

	List<Url> findByUrlEncurtada(String url_encurtada);
	List<Url> findByUrlOriginal(String url_original);
	List<Url> findByUrlOriginalAndPersonalizada(String url_original, boolean personalizada);
	
}
