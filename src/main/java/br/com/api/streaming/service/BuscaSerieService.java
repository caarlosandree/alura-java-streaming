package br.com.api.streaming.service;

import br.com.api.streaming.model.DadosSerie;
import br.com.api.streaming.model.DadosTemporada;

import java.util.ArrayList;
import java.util.List;

public class BuscaSerieService implements IBuscaSerieService {

    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    @Override
    public DadosSerie buscarSerie(String nomeSerie) {
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    @Override
    public List<DadosTemporada> buscarTemporadas(String nomeSerie, Integer totalTemporadas) {
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= totalTemporadas; i++) {
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        return temporadas;
    }
}
