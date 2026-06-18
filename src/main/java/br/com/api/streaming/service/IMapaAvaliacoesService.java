package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.util.List;
import java.util.Map;

public interface IMapaAvaliacoesService {
    Map<Integer, Double> obterMapaAvaliacoes(List<Episodio> episodios);
}
