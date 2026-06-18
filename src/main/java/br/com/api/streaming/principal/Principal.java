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

    public void exibeMenu() {
        try {
            System.out.println("Digite o nome da série para busca");
            var nomeSerie = leitura.nextLine();

            DadosSerie dados = buscaSerieService.buscarSerie(nomeSerie);
            System.out.println(dados);

            if (dados.obterTotalTemporadas() == null || dados.obterTotalTemporadas() <= 0) {
                System.out.println("Série não encontrada ou sem temporadas disponíveis");
                return;
            }

            List<DadosTemporada> temporadas = buscaSerieService.buscarTemporadas(nomeSerie, dados.obterTotalTemporadas());
            temporadas.forEach(System.out::println);

            exibicaoEpisodiosService.exibirTitulos(temporadas, dados.obterTotalTemporadas());

            topEpisodiosService.exibirTopEpisodios(temporadas);

            List<Episodio> episodios = conversaoEpisodioService.converter(temporadas);
            episodios.forEach(System.out::println);

            System.out.println("Digite o trecho do título para buscar: ");
            var trechoTitulo = leitura.nextLine();
            buscaTituloService.buscarPorTitulo(episodios, trechoTitulo);

            System.out.println("A partir de que ano você deseja ver os episódios? ");
            var ano = leitura.nextInt();
            leitura.nextLine();

            filtroAnoService.filtrarPorAno(episodios, ano);
        } finally {
            leitura.close();
        }
    }
}
