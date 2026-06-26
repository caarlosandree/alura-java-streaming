package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class EstatisticasService implements IEstatisticasService {

    @Override
    public void exibirEstatisticas(List<Episodio> episodios) {
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Estatísticas de avaliações:");
        var mediaArredondada = BigDecimal.valueOf(est.getAverage())
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Média: " + mediaArredondada);
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade de episódios avaliados: " + est.getCount());
    }
}
