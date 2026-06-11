package br.com.api.streaming.principal;

import br.com.api.streaming.model.DadosEpisodio;
import br.com.api.streaming.model.DadosSerie;
import br.com.api.streaming.model.DadosTemporada;
import br.com.api.streaming.model.Episodio;
import br.com.api.streaming.service.ConsumoApi;
import br.com.api.streaming.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibeMenu(){
        try {
            System.out.println("Digite o nome da série para busca");
            var nomeSerie = leitura.nextLine();
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            System.out.println(dados);

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i<=dados.totalTemporadas(); i++){
                json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(t -> System.out.println(t));

            for(int i = 0; i < dados.totalTemporadas(); i++){
                List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
                for(int j = 0; j< episodiosTemporada.size(); j++){
                    System.out.println(episodiosTemporada.get(j).titulo());
                }
            }

            temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

            List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream())
                    .collect(Collectors.toList());

            System.out.println("Top 5 episódios com melhor avaliação");
            dadosEpisodios.stream()
                    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                    .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                    .limit(5)
                    .forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numero(), e))
                    ).collect(Collectors.toList());

            episodios.forEach(System.out::println);

        } finally {
            leitura.close();
        }
    }
}
