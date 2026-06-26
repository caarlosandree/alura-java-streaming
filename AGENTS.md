# streaming — Instruções para Agentes de IA

Aplicação CLI Java que consome a API OMDB para buscar e filtrar séries e episódios usando Java Streams. Projeto de aprendizado do curso Alura — Java Streams com API OMDB.

---

## Stack

- **Java 25** + **Spring Boot 4.1.0** (CLI / `CommandLineRunner`, **sem servidor web**)
- **Gradle 9.5.1**
- **Jackson Databind** — desserialização JSON
- **JUnit 5** — testes
- **Sem banco de dados**

---

## Comandos

| Ação | Comando |
|---|---|
| Compilar | `./gradlew compileJava` |
| Build completo | `./gradlew build` |
| Executar testes | `./gradlew test` |
| Rodar a aplicação | `./gradlew bootRun` |

---

## Estrutura de diretórios

```
src/main/java/br/com/api/streaming/
├── StreamingApplication.java        # Entry point — CommandLineRunner
├── principal/
│   └── Principal.java               # Menu interativo + toda orquestração
├── model/
│   ├── DadosEpisodio.java           # Record — DTO de episódio (API OMDB)
│   ├── DadosSerie.java              # Record — DTO de série (API OMDB)
│   ├── DadosTemporada.java          # Record — DTO de temporada (API OMDB)
│   └── Episodio.java                # Entidade de domínio com getters/setters
└── service/
    ├── IConverteDados.java          # Interface genérica JSON → Java
    ├── ConverteDados.java           # Implementação com ObjectMapper
    └── ConsumoApi.java              # Cliente HTTP (java.net.http)
```

---

## Convenções de código

### Nomenclatura
- Métodos, variáveis e parâmetros em **português**: `obterDados`, `exibeMenu`, `nomeSerie`, `totalTemporadas`
- Classes e records em **PascalCase**: `DadosSerie`, `ConsumoApi`
- Constantes em **UPPER_SNAKE_CASE**: `ENDERECO`, `API_KEY`
- Pacotes em **lowercase**: `br.com.api.streaming.model`

### Records para DTOs de API
```java
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(
    @JsonAlias("Title") String titulo,
    @JsonAlias("Episode") Integer numero,
    @JsonAlias("imdbRating") String avaliacao,
    @JsonAlias("Released") String dataLancamento) {}
```

### Interface + implementação para serviços
```java
public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
```

### Java Streams para transformações
```java
List<Episodio> episodios = temporadas.stream()
    .flatMap(t -> t.episodios().stream().map(e -> new Episodio(t.numero(), e)))
    .collect(Collectors.toList());
```

### Formatação
- Indentação: **4 espaços**
- Máximo: **120 caracteres** por linha

---

## Testes

- **Testar**: construção de `Episodio` (avaliação inválida → `0.0`, data inválida → `null`), desserialização JSON com `ConverteDados`, lógica de Streams
- **Não testar** `ConsumoApi` diretamente — depende de rede
- Padrão: **Given/When/Then** com JUnit 5 e AssertJ
- Executar: `./gradlew test`

---

## Commits

Seguir **Conventional Commits em português brasileiro**:

```
<tipo>: <descrição em português (máx 72 chars)>
```

Tipos: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`

Use imperativo: "adiciona", "corrige", "remove", "extrai"

**Nunca** incluir `Co-authored-by:` ou variações nas mensagens.

Exemplos:
```
feat: adiciona filtro de episódios por nota mínima
fix: corrige NullPointerException quando data de lançamento é nula
refactor: extrai construção de URL para método privado em Principal
test: adiciona testes unitários para conversão de avaliação inválida
```

---

## Segurança

- Não duplicar a chave de API OMDB em outros arquivos
- Não logar a chave de API via `System.out.println` ou logger
- Não fazer requisições HTTP para URLs arbitrárias fornecidas pelo usuário
- Não commitar credenciais reais

---

## Restrições operacionais

- **Não adicionar** banco de dados, JPA ou Spring Web sem solicitação explícita
- **Não subir** servidor HTTP — é CLI puro
- **Não iniciar** processos externos sem pedido do usuário
- **Não incluir** `Co-authored-by:` em commits
