package br.com.api.streaming.service;

import br.com.api.streaming.model.Episodio;

import java.util.List;
import java.util.Optional;

public class BuscaTituloService implements IBuscaTituloService {

    @Override
    public void buscarPorTitulo(List<Episodio> episodios, String trechoTitulo) {
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado: " + episodioBuscado.get());
        } else {
            System.out.println("Episódio não encontrado");
        }
    }
}
