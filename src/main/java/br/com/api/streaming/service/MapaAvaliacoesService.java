package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapaAvaliacoesService implements IMapaAvaliacoesService {

    @Override
    public Map<Integer, Double> obterMapaAvaliacoes(List<Episodio> episodios) {
        return episodios.stream()
                .filter(episodio -> episodio.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(Episodio::getAvaliacao),
                                avaliacao -> BigDecimal.valueOf(avaliacao)
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .doubleValue())));
    }
}
