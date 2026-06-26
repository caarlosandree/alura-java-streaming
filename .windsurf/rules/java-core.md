---
trigger: always_on
description: Arquitetura, estrutura de pacotes, nomenclatura e padrões de código do projeto streaming (CLI Java + Spring Boot)
globs: "**/*.java"
---

# Java Core — projeto streaming

## Contexto do projeto

Aplicação CLI (`CommandLineRunner`) que consome a API OMDB e usa Java Streams para filtrar e exibir séries e episódios. **Não é uma API REST** — não há servidor HTTP, controllers nem endpoints expostos.

## Estrutura de pacotes

```
br.com.api.streaming/
├── StreamingApplication.java   # CommandLineRunner → chama Principal.exibeMenu()
├── principal/                  # Orquestração, menu interativo, lógica de exibição
├── model/                      # Records (DTOs OMDB) + Episodio (entidade de domínio)
└── service/                    # Interfaces e implementações de serviços
```

Novos pacotes seguem o padrão: `br.com.api.streaming.<nome-do-módulo>`.

## Nomenclatura

- Métodos, variáveis e parâmetros em **português** (`obterDados`, `exibeMenu`, `nomeSerie`, `totalTemporadas`, `dadosEpisodios`)
- Classes e records em **PascalCase** (`DadosSerie`, `ConsumoApi`, `ConverteDados`)
- Constantes em **UPPER_SNAKE_CASE** (`ENDERECO`, `API_KEY`)
- Pacotes em **lowercase** (`br.com.api.streaming.model`)

## Padrões por tipo de classe

### DTOs de API (records com Jackson)

Use `record` com `@JsonIgnoreProperties(ignoreUnknown = true)` e `@JsonAlias` mapeando os campos exatos da OMDB:

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(
    @JsonAlias("Title") String titulo,
    @JsonAlias("Episode") Integer numero,
    @JsonAlias("imdbRating") String avaliacao,
    @JsonAlias("Released") String dataLancamento) {}
```

### Entidade de domínio (classe com getters/setters)

`Episodio` representa o episódio após conversão dos dados brutos. Mantém getters/setters explícitos e `toString()` descritivo. Erros de parsing (`NumberFormatException`, `DateTimeException`) são tratados no construtor com valores de fallback (`0.0` e `null`).

### Serviços

Sempre defina **interface** antes da implementação:

```java
// Interface genérica
public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}

// Implementação com ObjectMapper
public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## Java Streams

Use Streams para toda transformação e filtragem de coleções — é o foco pedagógico do projeto:

```java
// flatMap para achatar temporadas → episódios com número de temporada
List<Episodio> episodios = temporadas.stream()
    .flatMap(t -> t.episodios().stream()
        .map(e -> new Episodio(t.numero(), e)))
    .collect(Collectors.toList());

// filtrar, ordenar e limitar
dadosEpisodios.stream()
    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
    .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
    .limit(5)
    .forEach(System.out::println);

// filtrar por data usando LocalDate
episodios.stream()
    .filter(e -> e.getLancamento() != null && e.getLancamento().isAfter(dataBusca))
    .forEach(e -> System.out.println(...));
```

## Formatação

- Indentação: **4 espaços**
- Máximo: **120 caracteres** por linha
- Sem código comentado ou arquivos de debug

## Módulos relacionados

- `java-testing.md` — testes unitários para lógica de streams e conversão
- `java-checklist.md` — checklist pré-commit
- `commit.md` — padrão de commits
- `security.md` — proteção da chave de API OMDB
