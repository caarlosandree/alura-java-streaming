package br.com.api.streaming.principal;
import br.com.api.streaming.model.DadosSerie;
import br.com.api.streaming.model.DadosTemporada;
import br.com.api.streaming.model.Episodio;
import br.com.api.streaming.service.*;

import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private IBuscaSerieService buscaSerieService = new BuscaSerieService();
    private IExibicaoEpisodiosService exibicaoEpisodiosService = new ExibicaoEpisodiosService();
    private ITopEpisodiosService topEpisodiosService = new TopEpisodiosService();
    private IConversaoEpisodioService conversaoEpisodioService = new ConversaoEpisodioService();
    private IBuscaTituloService buscaTituloService = new BuscaTituloService();
    private IFiltroAnoService filtroAnoService = new FiltroAnoService();
    private IMapaAvaliacoesService mapaAvaliacoesService = new MapaAvaliacoesService();

    public void exibeMenu() {
        try {
            System.out.println("Digite o nome da série para busca: ");
            var nomeSerie = leitura.nextLine();

            DadosSerie dados = buscaSerieService.buscarSerie(nomeSerie);
            System.out.println(dados);

            if (dados.obterTotalTemporadas() == null || dados.obterTotalTemporadas() <= 0) {
                System.out.println("Série não encontrada ou sem temporadas disponíveis");
                return;
            }

            List<DadosTemporada> temporadas = buscaSerieService.buscarTemporadas(nomeSerie, dados.obterTotalTemporadas());
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = conversaoEpisodioService.converter(temporadas);

            var opcao = -1;
            while (opcao != 0) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1 - Exibir títulos de episódios");
                System.out.println("2 - Exibir top 5 episódios");
                System.out.println("3 - Buscar episódio por trecho do título");
                System.out.println("4 - Filtrar episódios por ano");
                System.out.println("5 - Exibir mapa de avaliações por temporada");
                System.out.println("0 - Sair");

                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1 -> exibicaoEpisodiosService.exibirTitulos(temporadas, dados.obterTotalTemporadas());
                    case 2 -> topEpisodiosService.exibirTopEpisodios(temporadas);
                    case 3 -> {
                        System.out.println("Digite o trecho do título para buscar: ");
                        var trechoTitulo = leitura.nextLine();
                        buscaTituloService.buscarPorTitulo(episodios, trechoTitulo);
                    }
                    case 4 -> {
                        System.out.println("A partir de que ano você deseja ver os episódios? ");
                        var ano = leitura.nextInt();
                        leitura.nextLine();
                        filtroAnoService.filtrarPorAno(episodios, ano);
                    }
                    case 5 -> mapaAvaliacoesService.obterMapaAvaliacoes(episodios)
                            .forEach((temporada, avaliacao) -> System.out.println("Temporada " + temporada + ": " + avaliacao));
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida");
                }
            }
        } finally {
            leitura.close();
        }
    }
}
