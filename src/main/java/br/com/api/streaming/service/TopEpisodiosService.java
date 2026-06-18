package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosEpisodio;
import br.com.api.streaming.model.DadosTemporada;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopEpisodiosService implements ITopEpisodiosService {

    @Override
    public void exibirTopEpisodios(List<DadosTemporada> temporadas) {
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("Top 10 episódios com melhor avaliação");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro (N/A)" + e.avaliacao()))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("Segundo filtro (ordem decrescente)" + e.avaliacao()))
                .limit(10)
                .peek(e -> System.out.println("Terceiro filtro (limit 10)" + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Quarto filtro (titulo em maiúsculo)" + e))
                .forEach(System.out::println);
    }
}
