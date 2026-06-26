# streaming

Aplicação CLI Java que consome a API OMDB para buscar e filtrar séries e episódios via Java Streams. Projeto de aprendizado do curso Alura — Java Streams com API OMDB.

## Stack

- **Java 25** + **Spring Boot 4.1.0** (modo CLI — `CommandLineRunner`, sem servidor web)
- **Gradle 9.5.1**
- **Jackson Databind** — desserialização de JSON da API OMDB
- **JUnit 5** (via `spring-boot-starter-test`) — testes
- **Sem banco de dados**

## Comandos canônicos

| Ação | Comando |
|---|---|
| Compilar | `./gradlew compileJava` |
| Build completo | `./gradlew build` |
| Executar testes | `./gradlew test` |
| Rodar a aplicação | `./gradlew bootRun` |

## Estrutura de diretórios

```
src/main/java/br/com/api/streaming/
├── StreamingApplication.java        # Entry point — implementa CommandLineRunner, chama Principal.exibeMenu()
├── principal/
│   └── Principal.java               # Menu interativo + orquestração completa (busca, filtros, exibição)
├── model/
│   ├── DadosEpisodio.java           # Record — DTO de episódio desserializado da API OMDB
│   ├── DadosSerie.java              # Record — DTO de série desserializado da API OMDB
│   ├── DadosTemporada.java          # Record — DTO de temporada desserializado da API OMDB
│   └── Episodio.java                # Entidade de domínio com getters/setters e lógica de conversão
└── service/
    ├── IConverteDados.java          # Interface genérica de conversão JSON → objeto Java
    ├── ConverteDados.java           # Implementação com ObjectMapper (Jackson)
    └── ConsumoApi.java              # Cliente HTTP — java.net.http.HttpClient para a OMDB
```

## Convenções de código

- **Records** para todos os DTOs de API (`DadosSerie`, `DadosTemporada`, `DadosEpisodio`) com `@JsonIgnoreProperties(ignoreUnknown = true)` e `@JsonAlias`
- **Classes com getters/setters** para entidades de domínio (`Episodio`)
- **Interface + implementação** para serviços (`IConverteDados` → `ConverteDados`)
- **Nomes em português** para métodos, variáveis e parâmetros (`obterDados`, `exibeMenu`, `nomeSerie`, `totalTemporadas`)
- **Java Streams** para toda transformação e filtragem de coleções
- Indentação: 4 espaços; máximo 120 caracteres por linha

## Restrições operacionais

- **Não adicione** banco de dados, JPA, Spring Web ou endpoints HTTP sem solicitação explícita
- **Não suba** servidor HTTP — este projeto é CLI puro via `CommandLineRunner`
- **Não espalhe** a chave de API OMDB em outros arquivos nem a logue
- **Não inicie** processos externos sem pedido explícito do usuário
- Mensagens de commit em **português brasileiro**, seguindo Conventional Commits
- **Nunca** inclua `Co-authored-by:` em mensagens de commit

## Regras complementares

Regras detalhadas disponíveis em `.windsurf/rules/`:
- `java-core.md` — arquitetura, nomenclatura e padrões de código
- `java-testing.md` — estratégia de testes para o projeto
- `java-checklist.md` — checklist pré-commit
- `commit.md` — padrão de commits convencional
- `security.md` — segurança e proteção de chaves de API
