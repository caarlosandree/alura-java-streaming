package br.com.api.streaming.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episodio {
    private Integer temporada;
    private Integer numero;
    private String titulo;
    private Double avaliacao;
    private LocalDate lancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.numero = dadosEpisodio.numero();
        this.titulo = dadosEpisodio.titulo();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException nfe) {
            this.avaliacao = 0.0;
        }

        try {
            this.lancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeException dtEx) {
            this.lancamento = null;
        }

    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getLancamento() {
        return lancamento;
    }

    public void setLancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                ", numero=" + numero +
                ", titulo='" + titulo + '\'' +
                ", avaliacao=" + avaliacao +
                ", lancamento=" + lancamento ;
    }
}
