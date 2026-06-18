package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.util.List;

public interface IBuscaTituloService {

    void buscarPorTitulo(List<Episodio> episodios, String trechoTitulo);
}
