package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosEpisodio;
import br.com.api.streaming.model.Episodio;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapaAvaliacoesServiceTest {

    private final IMapaAvaliacoesService mapaAvaliacoesService = new MapaAvaliacoesService();

    @Test
    void deveCalcularMediaDeAvaliacoesPorTemporada() {
        // Given
        List<Episodio> episodios = Arrays.asList(
                new Episodio(1, new DadosEpisodio("E1", 1, "8.0", "2023-01-01")),
                new Episodio(1, new DadosEpisodio("E2", 2, "9.0", "2023-01-08")),
                new Episodio(2, new DadosEpisodio("E3", 1, "7.0", "2023-02-01")),
                new Episodio(2, new DadosEpisodio("E4", 2, "9.0", "2023-02-08"))
        );

        // When
        Map<Integer, Double> resultado = mapaAvaliacoesService.obterMapaAvaliacoes(episodios);

        // Then
        assertThat(resultado)
                .containsEntry(1, 8.50)
                .containsEntry(2, 8.00);
    }

    @Test
    void deveIgnorarEpisodiosComAvaliacaoInvalida() {
        // Given
        List<Episodio> episodios = Arrays.asList(
                new Episodio(1, new DadosEpisodio("E1", 1, "8.0", "2023-01-01")),
                new Episodio(1, new DadosEpisodio("E2", 2, "N/A", "2023-01-08")),
                new Episodio(2, new DadosEpisodio("E3", 1, "N/A", "2023-02-01"))
        );

        // When
        Map<Integer, Double> resultado = mapaAvaliacoesService.obterMapaAvaliacoes(episodios);

        // Then
        assertThat(resultado)
                .containsEntry(1, 8.0)
                .doesNotContainKey(2);
    }

    @Test
    void deveRetornarMapaVazioQuandoNaoHouverEpisodios() {
        // Given
        List<Episodio> episodios = Collections.emptyList();

        // When
        Map<Integer, Double> resultado = mapaAvaliacoesService.obterMapaAvaliacoes(episodios);

        // Then
        assertThat(resultado).isEmpty();
    }
}
