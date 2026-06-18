package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosTemporada;
import br.com.api.streaming.model.Episodio;

import java.util.List;

public interface IConversaoEpisodioService {

    List<Episodio> converter(List<DadosTemporada> temporadas);
}
