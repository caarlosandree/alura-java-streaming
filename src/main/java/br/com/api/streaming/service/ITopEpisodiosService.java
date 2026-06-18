package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosTemporada;

import java.util.List;

public interface ITopEpisodiosService {

    void exibirTopEpisodios(List<DadosTemporada> temporadas);
}
