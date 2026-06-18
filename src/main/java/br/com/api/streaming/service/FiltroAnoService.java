package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FiltroAnoService implements IFiltroAnoService {

    @Override
    public void filtrarPorAno(List<Episodio> episodios, int ano) {
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " - Episódio: " + e.getNumero() +
                                " - Titulo: " + e.getTitulo() +
                                " - Avaliação: " + e.getAvaliacao() +
                                " - Lançamento: " + e.getLancamento().format(formatador)
                ));
    }
}
