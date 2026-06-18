package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosTemporada;

import java.util.List;

public interface IExibicaoEpisodiosService {

    void exibirTitulos(List<DadosTemporada> temporadas, Integer totalTemporadas);
}
