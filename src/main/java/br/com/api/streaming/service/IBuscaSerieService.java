package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosSerie;
import br.com.api.streaming.model.DadosTemporada;

import java.util.List;

public interface IBuscaSerieService {

    DadosSerie buscarSerie(String nomeSerie);

    List<DadosTemporada> buscarTemporadas(String nomeSerie, Integer totalTemporadas);
}
