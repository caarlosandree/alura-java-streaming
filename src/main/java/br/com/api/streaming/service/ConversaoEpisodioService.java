package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosTemporada;
import br.com.api.streaming.model.Episodio;

import java.util.List;
import java.util.stream.Collectors;

public class ConversaoEpisodioService implements IConversaoEpisodioService {

    @Override
    public List<Episodio> converter(List<DadosTemporada> temporadas) {
        return temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new Episodio(t.numero(), e)))
                .collect(Collectors.toList());
    }
}
