---
trigger: model_decision
description: Estratégia de testes para o projeto streaming — JUnit 5, lógica de streams, conversão JSON e construção de Episodio
globs: "**/*.java"
---

# Testes — projeto streaming

## O que testar

Por ser um projeto CLI sem banco de dados, o foco é:

- **Construção de `Episodio`** — tratamento de `NumberFormatException` (avaliação inválida) e `DateTimeException` (data inválida)
- **Conversão JSON → Java** — `ConverteDados.obterDados()` com JSONs representativos da OMDB
- **Lógica de Streams** — filtros, ordenação, `flatMap`, `limit`
- `ConsumoApi` **não precisa de teste unitário** — depende de rede; use mock se necessário

## Estrutura de um teste (Given/When/Then)

```java
// src/test/java/br/com/api/streaming/model/EpisodioTest.java
class EpisodioTest {

    @Test
    void deveUsarAvaliacaoZeroQuandoValorNaoNumerico() {
        // Given
        DadosEpisodio dados = new DadosEpisodio("Piloto", 1, "N/A", "2010-01-17");

        // When
        Episodio episodio = new Episodio(1, dados);

        // Then
        assertThat(episodio.getAvaliacao()).isEqualTo(0.0);
    }

    @Test
    void deveUsarLancamentoNullQuandoDataInvalida() {
        // Given
        DadosEpisodio dados = new DadosEpisodio("Piloto", 1, "8.5", "N/A");

        // When
        Episodio episodio = new Episodio(1, dados);

        // Then
        assertThat(episodio.getLancamento()).isNull();
    }

    @Test
    void deveConverterAvaliacaoNumericaCorretamente() {
        // Given
        DadosEpisodio dados = new DadosEpisodio("Ozymandias", 14, "9.9", "2013-09-15");

        // When
        Episodio episodio = new Episodio(5, dados);

        // Then
        assertThat(episodio.getAvaliacao()).isEqualTo(9.9);
        assertThat(episodio.getTemporada()).isEqualTo(5);
        assertThat(episodio.getLancamento()).isEqualTo(LocalDate.of(2013, 9, 15));
    }
}
```

## Teste de conversão JSON

```java
// src/test/java/br/com/api/streaming/service/ConverteDadosTest.java
class ConverteDadosTest {

    private final ConverteDados conversor = new ConverteDados();

    @Test
    void deveDeserializarDadosSerieCorretamente() {
        // Given
        String json = """
            {"Title": "Breaking Bad", "totalSeasons": "5", "imdbRating": "9.5", "Genre": "Drama"}
            """;

        // When
        DadosSerie serie = conversor.obterDados(json, DadosSerie.class);

        // Then
        assertThat(serie.titulo()).isEqualTo("Breaking Bad");
        assertThat(serie.totalTemporadas()).isEqualTo(5);
        assertThat(serie.avaliacao()).isEqualTo("9.5");
    }

    @Test
    void deveIgnorarCamposDesconhecidosNaDesserializacao() {
        // Given — JSON com campos extras não mapeados
        String json = """
            {"Title": "The Wire", "totalSeasons": "5", "imdbRating": "9.3",
             "Genre": "Crime", "Director": "N/A", "Language": "English"}
            """;

        // When / Then — não deve lançar exceção
        assertThatCode(() -> conversor.obterDados(json, DadosSerie.class))
            .doesNotThrowAnyException();
    }
}
```

## Execução

```bash
./gradlew test                                     # todos os testes
./gradlew test --tests "*EpisodioTest*"            # apenas EpisodioTest
./gradlew test --tests "*ConverteDadosTest*"       # apenas ConverteDadosTest
```

## Módulos relacionados

- `java-core.md` — padrões de código e estrutura
- `java-checklist.md` — checklist pré-commit
