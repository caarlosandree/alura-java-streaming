package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.util.List;

public interface IEstatisticasService {
    void exibirEstatisticas(List<Episodio> episodios);
}
