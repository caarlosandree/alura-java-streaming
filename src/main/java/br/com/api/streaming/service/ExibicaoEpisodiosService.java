package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosEpisodio;
import br.com.api.streaming.model.DadosTemporada;

import java.util.List;

public class ExibicaoEpisodiosService implements IExibicaoEpisodiosService {

    @Override
    public void exibirTitulos(List<DadosTemporada> temporadas, Integer totalTemporadas) {
        for (int i = 0; i < totalTemporadas; i++) {
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
