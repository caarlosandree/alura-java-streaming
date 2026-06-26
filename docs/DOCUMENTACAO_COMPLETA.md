# Documentação Completa - Projeto Streaming com Java Streams

## 📚 Índice

1. [Spring Boot](#spring-boot)
2. [Estrutura do Projeto](#estrutura-do-projeto)
3. [Conceitos Fundamentais](#conceitos-fundamentais)
4. [Java Streams](#java-streams)
5. [Expressões Lambda](#expressões-lambda)
6. [Operações de Stream](#operações-de-stream)
7. [Exemplos Práticos](#exemplos-práticos)
8. [Boas Práticas](#boas-práticas)

---

## Spring Boot

### O que é Spring Boot?

Spring Boot é um framework que simplifica a criação de aplicações Java baseadas em Spring. Ele fornece configurações automáticas e reduz a necessidade de configuração manual, permitindo que você se concentre na lógica de negócio.

### Por que usar Spring Boot?

- **Configuração automática**: Detecta automaticamente dependências e configura a aplicação
- **Embedded servers**: Não precisa de um servidor externo (Tomcat, Jetty, etc.)
- **Dependências simplificadas**: Gerencia versões compatíveis automaticamente
- **Produtividade**: Reduz boilerplate e acelera o desenvolvimento

### Estrutura do projeto Spring Boot

```
projeto/
├── src/
│   ├── main/
│   │   ├── java/          # Código-fonte da aplicação
│   │   └── resources/     # Arquivos de configuração (application.properties)
│   └── test/
│       └── java/          # Testes unitários
├── build.gradle           # Configuração do Gradle (dependências, plugins)
└── settings.gradle        # Configurações globais do projeto
```

### Dependências do nosso projeto

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'com.fasterxml.jackson.core:jackson-databind'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

- **spring-boot-starter**: Dependência base do Spring Boot
- **jackson-databind**: Biblioteca para desserializar JSON em objetos Java
- **spring-boot-starter-test**: Ferramentas para testes (JUnit 5, AssertJ, etc.)

### CommandLineRunner

No nosso projeto, usamos `CommandLineRunner` para criar uma aplicação CLI (Command Line Interface):

```java
@SpringBootApplication
public class StreamingApplication implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal();
        principal.exibeMenu();
    }
}
```

Isso permite que a aplicação execute código no startup sem necessidade de um servidor HTTP.

---

## Estrutura do Projeto

### Organização de pacotes

```
br.com.api.streaming/
├── StreamingApplication.java    # Entry point da aplicação
├── principal/
│   └── Principal.java           # Menu interativo e orquestração
├── model/
│   ├── DadosSerie.java          # DTO da série (API OMDB)
│   ├── DadosTemporada.java      # DTO da temporada (API OMDB)
│   ├── DadosEpisodio.java       # DTO do episódio (API OMDB)
│   └── Episodio.java            # Entidade de domínio
└── service/
    ├── IConverteDados.java      # Interface para conversão JSON
    ├── ConverteDados.java       # Implementação com Jackson
    └── ConsumoApi.java          # Cliente HTTP para OMDB
```

### Padrão de Camadas

1. **Service**: Responsável pela comunicação com APIs externas e conversão de dados
2. **Model**: Define as estruturas de dados (DTOs e entidades)
3. **Principal**: Orquestra o fluxo da aplicação e exibe resultados

---

## Conceitos Fundamentais

### Records (Java 16+)

Records são uma forma concisa de criar classes imutáveis para armazenar dados:

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
    @JsonAlias("Title") String titulo,
    @JsonAlias("totalSeasons") Integer totalTemporadas,
    @JsonAlias("imdbRating") String avaliacao) {
}
```

**Características:**
- Imutáveis (não podem ser alterados após criação)
- Getters automáticos (sem `get` prefixo): `dados.titulo()`
- `equals()`, `hashCode()` e `toString()` gerados automaticamente
- Perfeitos para DTOs (Data Transfer Objects)

### Jackson - Desserialização JSON

Jackson é uma biblioteca que converte JSON em objetos Java:

```java
ObjectMapper mapper = new ObjectMapper();
DadosSerie dados = mapper.readValue(json, DadosSerie.class);
```

**Anotações importantes:**

- `@JsonIgnoreProperties(ignoreUnknown = true)`: Ignora campos JSON que não existem na classe
- `@JsonAlias("Title")`: Mapeia o campo JSON "Title" para o atributo `titulo`

### Interfaces e Implementação

Usamos interfaces para definir contratos:

```java
public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}

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

**Benefícios:**
- Desacoplamento: A classe `Principal` não precisa conhecer `ConverteDados`
- Testabilidade: Fácil criar mocks para testes
- Flexibilidade: Pode trocar implementação sem alterar o código que usa

---

## Java Streams

### O que é uma Stream?

Uma Stream é uma sequência de elementos que suporta operações agregadas (filter, map, reduce, etc.). **Não é uma coleção** — é uma forma funcional de processar dados.

### Características principais

- **Lazy evaluation**: Operações intermediárias não executam até uma operação terminal
- **Imutável**: Não modifica a coleção original
- **Funcional**: Usa expressões lambda e referências de método
- **Eficiente**: Otimizada para processamento em cadeia

### Criando Streams

```java
// De uma coleção
List<String> lista = Arrays.asList("a", "b", "c");
Stream<String> stream = lista.stream();

// De um array
String[] array = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(array);

// Usando Stream.of()
Stream<String> stream = Stream.of("a", "b", "c");

// De um intervalo de números
IntStream stream = IntStream.range(1, 10); // 1 a 9
```

### Operações Terminais vs Intermediárias

**Operações Intermediárias** (retornam uma Stream):
- `filter()`, `map()`, `flatMap()`, `sorted()`, `distinct()`, `limit()`, `skip()`

**Operações Terminais** (retornam um resultado):
- `forEach()`, `collect()`, `reduce()`, `count()`, `findFirst()`, `anyMatch()`, `allMatch()`

```java
// Intermediárias: filter, map, sorted
// Terminal: forEach
lista.stream()
    .filter(e -> e > 5)           // Intermediária
    .map(e -> e * 2)              // Intermediária
    .sorted()                      // Intermediária
    .forEach(System.out::println); // Terminal
```

---

## Expressões Lambda

### O que é uma Lambda?

Uma expressão lambda é uma forma concisa de escrever uma função anônima em Java. Ela permite passar comportamento como argumento.

### Sintaxe

```java
(parâmetros) -> { corpo }
```

**Exemplos:**

```java
// Sem parâmetros
() -> System.out.println("Olá");

// Um parâmetro (parênteses opcionais)
x -> x * 2
(x) -> x * 2

// Múltiplos parâmetros
(x, y) -> x + y

// Corpo com múltiplas linhas
(x) -> {
    int resultado = x * 2;
    return resultado;
}

// Uma linha (return implícito)
(x) -> x * 2
```

### Interfaces Funcionais

Uma interface funcional tem **exatamente um método abstrato**. Lambdas implementam interfaces funcionais:

```java
// Interface funcional
@FunctionalInterface
public interface Operacao {
    int executar(int a, int b);
}

// Usando lambda
Operacao soma = (a, b) -> a + b;
int resultado = soma.executar(5, 3); // 8
```

### Exemplos no nosso projeto

```java
// forEach com lambda
temporadas.forEach(t -> System.out.println(t));

// filter com lambda
.filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))

// map com lambda
.map(e -> e.titulo().toUpperCase())

// Comparator com lambda
.sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
```

---

## Operações de Stream

### 1. forEach

**O que faz:** Executa uma ação para cada elemento da stream.

**Quando usar:** Para exibir dados, efeitos colaterais.

**Sintaxe:**
```java
stream.forEach(elemento -> ação);
```

**Exemplos:**

```java
// Exibir cada temporada
temporadas.forEach(t -> System.out.println(t));

// Exibir título de cada episódio
episodios.forEach(e -> System.out.println(e.getTitulo()));

// Usando referência de método
episodios.forEach(System.out::println);
```

**Referência de método:** `System.out::println` é equivalente a `e -> System.out.println(e)`

---

### 2. filter

**O que faz:** Filtra elementos que atendem a uma condição.

**Quando usar:** Para remover elementos indesejados.

**Sintaxe:**
```java
stream.filter(elemento -> condição);
```

**Exemplos:**

```java
// Filtrar episódios com avaliação válida
dadosEpisodios.stream()
    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))

// Filtrar episódios lançados após uma data
episodios.stream()
    .filter(e -> e.getLancamento().isAfter(dataBusca))

// Filtrar números maiores que 5
numeros.stream()
    .filter(n -> n > 5)
```

**Importante:** `filter()` retorna uma Stream, não executa nada. Precisa de uma operação terminal.

---

### 3. map

**O que faz:** Transforma cada elemento em outro.

**Quando usar:** Para converter ou extrair dados.

**Sintaxe:**
```java
stream.map(elemento -> transformação);
```

**Exemplos:**

```java
// Converter título para maiúsculo
dadosEpisodios.stream()
    .map(e -> e.titulo().toUpperCase())

// Extrair apenas os números dos episódios
episodios.stream()
    .map(e -> e.getNumero())

// Converter String para Integer
strings.stream()
    .map(Integer::parseInt)
```

**Diferença entre map e forEach:**
- `map()`: Transforma e retorna uma nova Stream
- `forEach()`: Executa ação, não retorna nada

```java
// map: transforma
List<String> maiusculas = lista.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList()); // Precisa coletar

// forEach: apenas executa
lista.forEach(System.out::println); // Não retorna nada
```

---

### 4. flatMap

**O que faz:** Achata estruturas aninhadas em uma única stream.

**Quando usar:** Para trabalhar com coleções dentro de coleções.

**Sintaxe:**
```java
stream.flatMap(elemento -> elemento.subColecao().stream());
```

**Exemplo no projeto:**

```java
// Estrutura: Temporada contém lista de Episódios
// Queremos: Uma lista única de todos os Episódios

List<DadosEpisodio> dadosEpisodios = temporadas.stream()
    .flatMap(t -> t.episodios().stream())
    .collect(Collectors.toList());
```

**Visualização:**

```
Temporada 1: [Ep1, Ep2, Ep3]
Temporada 2: [Ep4, Ep5, Ep6]
Temporada 3: [Ep7, Ep8, Ep9]

flatMap transforma em:
[Ep1, Ep2, Ep3, Ep4, Ep5, Ep6, Ep7, Ep8, Ep9]
```

**Combinando flatMap com map:**

```java
List<Episodio> episodios = temporadas.stream()
    .flatMap(t -> t.episodios().stream()
        .map(e -> new Episodio(t.numero(), e))
    )
    .collect(Collectors.toList());
```

Isso cria `Episodio` (entidade de domínio) a partir de `DadosEpisodio` (DTO).

---

### 5. sorted

**O que faz:** Ordena os elementos.

**Quando usar:** Para exibir dados em ordem específica.

**Sintaxe:**
```java
stream.sorted(Comparator.comparing(função));
stream.sorted(Comparator.comparing(função).reversed());
```

**Exemplos:**

```java
// Ordenar por avaliação (crescente)
episodios.stream()
    .sorted(Comparator.comparing(Episodio::getAvaliacao))

// Ordenar por avaliação (decrescente)
episodios.stream()
    .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())

// Ordenar por título (alfabético)
episodios.stream()
    .sorted(Comparator.comparing(Episodio::getTitulo))

// Ordenar por múltiplos critérios
episodios.stream()
    .sorted(Comparator
        .comparing(Episodio::getTemporada)
        .thenComparing(Episodio::getNumero))
```

**Comparator.comparing():**
- Recebe uma função que extrai o valor a comparar
- `Episodio::getAvaliacao` é uma referência de método equivalente a `e -> e.getAvaliacao()`

---

### 6. limit

**O que faz:** Limita a quantidade de elementos.

**Quando usar:** Para pegar apenas os primeiros N elementos.

**Sintaxe:**
```java
stream.limit(n);
```

**Exemplo no projeto:**

```java
// Top 10 episódios com melhor avaliação
dadosEpisodios.stream()
    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
    .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
    .limit(10)
    .forEach(System.out::println);
```

---

### 7. collect

**O que faz:** Coleta os elementos da stream em uma coleção.

**Quando usar:** Para converter stream em List, Set, Map, etc.

**Sintaxe:**
```java
stream.collect(Collectors.toList());
stream.collect(Collectors.toSet());
stream.collect(Collectors.toMap(chave, valor));
```

**Exemplos:**

```java
// Converter stream em List
List<Episodio> lista = episodios.stream()
    .filter(e -> e.getAvaliacao() > 8.0)
    .collect(Collectors.toList());

// Converter em Set (sem duplicatas)
Set<String> titulos = episodios.stream()
    .map(Episodio::getTitulo)
    .collect(Collectors.toSet());

// Converter em Map
Map<Integer, List<Episodio>> porTemporada = episodios.stream()
    .collect(Collectors.groupingBy(Episodio::getTemporada));
```

---

### 8. Outras operações úteis

**skip:** Pula os primeiros N elementos
```java
episodios.stream()
    .skip(5)        // Pula os 5 primeiros
    .limit(10)      // Pega os próximos 10
```

**distinct:** Remove duplicatas
```java
episodios.stream()
    .map(Episodio::getTitulo)
    .distinct()
```

**count:** Conta elementos
```java
long total = episodios.stream()
    .filter(e -> e.getAvaliacao() > 8.0)
    .count();
```

**findFirst:** Retorna o primeiro elemento
```java
Optional<Episodio> primeiro = episodios.stream()
    .filter(e -> e.getTitulo().contains("Pilot"))
    .findFirst();
```

**anyMatch / allMatch:** Verifica condições
```java
boolean temAlgumaAvaliacaoAlta = episodios.stream()
    .anyMatch(e -> e.getAvaliacao() > 9.0);

boolean todosTemAvaliacaoAlta = episodios.stream()
    .allMatch(e -> e.getAvaliacao() > 8.0);
```

---

### 9. peek

**O que faz:** "Espia" os elementos da stream sem alterá-los, executando uma ação de efeito colateral.

**Quando usar:** Para depuração, logging e entender o fluxo de dados na stream.

**Sintaxe:**
```java
stream.peek(elemento -> ação);
```

**Importante:** `peek()` é uma operação **intermediária** — ela processa os dados mas não finaliza a stream, permitindo que mais operações sejam feitas depois dela.

**Exemplos:**

```java
// Espiar cada elemento antes de filtrar
episodios.stream()
    .peek(e -> System.out.println("Antes do filtro: " + e.getTitulo()))
    .filter(e -> e.getAvaliacao() > 8.0)
    .peek(e -> System.out.println("Depois do filtro: " + e.getTitulo()))
    .forEach(System.out::println);

// Depurar valores em transformações
numeros.stream()
    .peek(n -> System.out.println("Elemento original: " + n))
    .map(n -> n * 2)
    .peek(n -> System.out.println("Depois do map: " + n))
    .forEach(System.out::println);
```

**Exemplo completo do projeto:**

```java
dadosEpisodios.stream()
    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
    .peek(e -> System.out.println("Primeiro filtro (N/A): " + e.avaliacao()))
    .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
    .peek(e -> System.out.println("Segundo filtro (ordem decrescente): " + e.avaliacao()))
    .limit(10)
    .peek(e -> System.out.println("Terceiro filtro (limit 10): " + e))
    .map(e -> e.titulo().toUpperCase())
    .peek(e -> System.out.println("Quarto filtro (título em maiúsculo): " + e))
    .forEach(System.out::println);
```

**Saída esperada:**
```
Primeiro filtro (N/A): 9.5
Segundo filtro (ordem decrescente): 9.5
Terceiro filtro (limit 10): DadosEpisodio[titulo=Pilot, numero=1, avaliacao=9.5, ...]
Quarto filtro (título em maiúsculo): PILOT
PILOT
Primeiro filtro (N/A): 9.3
Segundo filtro (ordem decrescente): 9.3
...
```

**Diferença entre peek() e forEach():**

| Aspecto | peek() | forEach() |
|---------|--------|-----------|
| Tipo | Operação intermediária | Operação terminal |
| Retorna | Stream | void |
| Permite encadeamento | Sim | Não |
| Uso | Depuração, logging | Executar ação final |

```java
// peek: continua a stream
lista.stream()
    .peek(System.out::println)
    .map(String::toUpperCase)
    .forEach(System.out::println);

// forEach: finaliza a stream
lista.stream()
    .forEach(System.out::println);
    // Não pode fazer mais nada depois
```

---

## Para saber mais: Entendendo o Fluxo

### O que são Streams?

Streams nos permitem manipular coleções de elementos (como Lists ou Sets) de forma mais sofisticada e eficiente do que os métodos tradicionais. Eles são especialmente úteis quando precisamos realizar operações em massa nesses elementos, como **filtrar**, **transformar** ou **agregar**.

### Vantagens das Streams

- **Código mais elegante**: Sintaxe funcional e declarativa
- **Fácil de ler**: Expressa a intenção claramente
- **Menos erros**: Reduz loops aninhados e índices manuais
- **Paralelização**: Permite executar operações em paralelo com `.parallelStream()`
- **Lazy evaluation**: Operações intermediárias só executam quando necessário

### Exemplo prático: Soma com peek

```java
List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);

int soma = numeros.stream()
    .peek(n -> System.out.println("Elemento: " + n))
    .map(n -> n * 2)
    .peek(n -> System.out.println("Conteúdo depois do map: " + n))
    .reduce(0, (total, numero) -> total + numero);

System.out.println("A soma dos números é: " + soma);
```

**Saída:**
```
Elemento: 1
Conteúdo depois do map: 2
Elemento: 2
Conteúdo depois do map: 4
Elemento: 3
Conteúdo depois do map: 6
Elemento: 4
Conteúdo depois do map: 8
Elemento: 5
Conteúdo depois do map: 10
A soma dos números é: 30
```

**O que acontece:**
1. `peek()` exibe cada número original
2. `map()` multiplica por 2
3. `peek()` exibe o resultado da multiplicação
4. `reduce()` soma todos os valores (2 + 4 + 6 + 8 + 10 = 30)

### Por que usar peek?

O `peek()` é essencial para:

1. **Depuração**: Entender o que está acontecendo em cada etapa
2. **Logging**: Registrar valores intermediários
3. **Validação**: Verificar se os dados estão corretos
4. **Aprendizado**: Visualizar o fluxo de dados na stream

### Regra de ouro

Use `peek()` sempre que quiser "espiar" o que está passando pela stream **sem alterar os dados**. É como colocar um ponto de parada em um debugger, mas sem parar a execução.

---

## Exemplos Práticos

### Exemplo 1: Buscar e exibir série

```java
// Passo 1: Buscar dados da série na API
var nomeSerie = "Breaking Bad";
var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

// Passo 2: Converter JSON para objeto Java
DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
System.out.println(dados);
```

**O que acontece:**
1. `ConsumoApi.obterDados()` faz requisição HTTP para OMDB
2. `ConverteDados.obterDados()` usa Jackson para desserializar JSON
3. `DadosSerie` é um record com os dados da série

---

### Exemplo 2: Buscar todas as temporadas

```java
List<DadosTemporada> temporadas = new ArrayList<>();

for (int i = 1; i <= dados.totalTemporadas(); i++) {
    json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") 
        + "&season=" + i + API_KEY);
    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
    temporadas.add(dadosTemporada);
}

// Exibir com forEach
temporadas.forEach(t -> System.out.println(t));
```

**O que acontece:**
1. Loop tradicional busca cada temporada (1 até totalTemporadas)
2. Cada temporada é convertida e adicionada à lista
3. `forEach` exibe cada temporada

---

### Exemplo 3: Exibir títulos de episódios (loops aninhados vs forEach)

**Abordagem tradicional:**
```java
for (int i = 0; i < dados.totalTemporadas(); i++) {
    List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
    for (int j = 0; j < episodiosTemporada.size(); j++) {
        System.out.println(episodiosTemporada.get(j).titulo());
    }
}
```

**Abordagem com forEach aninhado:**
```java
temporadas.forEach(t -> 
    t.episodios().forEach(e -> 
        System.out.println(e.titulo())
    )
);
```

**Abordagem com flatMap (mais elegante):**
```java
temporadas.stream()
    .flatMap(t -> t.episodios().stream())
    .map(DadosEpisodio::titulo)
    .forEach(System.out::println);
```

---

### Exemplo 4: Top 10 episódios por avaliação

```java
List<DadosEpisodio> dadosEpisodios = temporadas.stream()
    .flatMap(t -> t.episodios().stream())
    .collect(Collectors.toList());

System.out.println("Top 10 episódios com melhor avaliação");
dadosEpisodios.stream()
    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
    .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
    .limit(10)
    .map(e -> e.titulo().toUpperCase())
    .forEach(System.out::println);
```

**Passo a passo:**
1. `flatMap()`: Achata todas as temporadas em uma lista única de episódios
2. `filter()`: Remove episódios sem avaliação ("N/A")
3. `sorted()`: Ordena por avaliação em ordem decrescente
4. `limit(10)`: Pega apenas os 10 primeiros
5. `map()`: Converte título para maiúsculo
6. `forEach()`: Exibe cada um

---

### Exemplo 5: Converter DTO para entidade de domínio

```java
List<Episodio> episodios = temporadas.stream()
    .flatMap(t -> t.episodios().stream()
        .map(e -> new Episodio(t.numero(), e))
    )
    .collect(Collectors.toList());
```

**O que acontece:**
1. `flatMap()` achata temporadas em episódios
2. `map()` transforma cada `DadosEpisodio` em `Episodio`, passando o número da temporada
3. `collect()` coleta tudo em uma List

**Classe Episodio:**
```java
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
            this.avaliacao = 0.0; // Fallback se avaliação for "N/A"
        }
        try {
            this.lancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeException dtEx) {
            this.lancamento = null; // Fallback se data for inválida
        }
    }
}
```

---

### Exemplo 6: Filtrar episódios por ano de lançamento

```java
System.out.println("A partir de que ano você deseja ver os episódios? ");
var ano = leitura.nextInt();
LocalDate dataBusca = LocalDate.of(ano, 1, 1);

DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

episodios.stream()
    .filter(e -> e.getLancamento().isAfter(dataBusca))
    .forEach(e -> System.out.println(
        "Temporada: " + e.getTemporada() +
        " - Episódio: " + e.getNumero() +
        " - Titulo: " + e.getTitulo() +
        " - Avaliação: " + e.getAvaliacao() +
        " - Lançamento: " + e.getLancamento().format(formatador)
    ));
```

**O que acontece:**
1. Usuário digita um ano
2. Cria `LocalDate` com 1º de janeiro daquele ano
3. `filter()` mantém apenas episódios lançados após essa data
4. `forEach()` exibe cada episódio formatado

---

## Boas Práticas

### 1. Use Streams para transformações, não para efeitos colaterais

❌ **Evite:**
```java
lista.stream()
    .forEach(e -> {
        e.setValor(e.getValor() * 2);  // Modificando estado
        System.out.println(e);
    });
```

✅ **Prefira:**
```java
lista.stream()
    .map(e -> {
        Elemento novo = new Elemento(e);
        novo.setValor(novo.getValor() * 2);
        return novo;
    })
    .forEach(System.out::println);
```

### 2. Prefira referências de método a lambdas simples

❌ **Evite:**
```java
lista.forEach(e -> System.out.println(e));
```

✅ **Prefira:**
```java
lista.forEach(System.out::println);
```

### 3. Combine operações em uma única chain

❌ **Evite:**
```java
Stream<Episodio> stream1 = episodios.stream().filter(e -> e.getAvaliacao() > 8.0);
List<Episodio> lista1 = stream1.collect(Collectors.toList());
Stream<Episodio> stream2 = lista1.stream().sorted(Comparator.comparing(Episodio::getTitulo));
List<Episodio> resultado = stream2.collect(Collectors.toList());
```

✅ **Prefira:**
```java
List<Episodio> resultado = episodios.stream()
    .filter(e -> e.getAvaliacao() > 8.0)
    .sorted(Comparator.comparing(Episodio::getTitulo))
    .collect(Collectors.toList());
```

### 4. Use tipos específicos quando apropriado

```java
// Para números, use IntStream, LongStream, DoubleStream
IntStream.range(1, 10)
    .filter(n -> n % 2 == 0)
    .forEach(System.out::println);

// Mais eficiente que
Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
    .filter(n -> n % 2 == 0)
    .forEach(System.out::println);
```

### 5. Cuidado com operações terminais

Lembre-se: **Uma stream só pode ser consumida uma vez**

❌ **Evite:**
```java
Stream<Episodio> stream = episodios.stream();
stream.forEach(System.out::println);
stream.forEach(System.out::println); // Erro! Stream já foi consumida
```

✅ **Prefira:**
```java
episodios.stream().forEach(System.out::println);
episodios.stream().forEach(System.out::println); // OK, nova stream
```

### 6. Use Optional para valores que podem ser nulos

```java
Optional<Episodio> primeiro = episodios.stream()
    .filter(e -> e.getTitulo().contains("Pilot"))
    .findFirst();

if (primeiro.isPresent()) {
    System.out.println(primeiro.get());
} else {
    System.out.println("Nenhum episódio encontrado");
}

// Ou mais conciso:
primeiro.ifPresent(System.out::println);
```

### 7. Evite operações custosas em streams infinitas

```java
// ❌ Evite (pode ser infinito)
Stream.iterate(1, n -> n + 1)
    .filter(n -> n % 2 == 0)
    .forEach(System.out::println);

// ✅ Use limit
Stream.iterate(1, n -> n + 1)
    .filter(n -> n % 2 == 0)
    .limit(10)
    .forEach(System.out::println);
```

### 8. Tratamento de exceções em lambdas

```java
// ❌ Evite (não compila)
lista.stream()
    .map(e -> Integer.parseInt(e)) // Pode lançar exceção
    .forEach(System.out::println);

// ✅ Trate a exceção
lista.stream()
    .map(e -> {
        try {
            return Integer.parseInt(e);
        } catch (NumberFormatException ex) {
            return 0;
        }
    })
    .forEach(System.out::println);
```

---

## Resumo Visual

### Fluxo de dados no projeto

```
API OMDB
    ↓
ConsumoApi.obterDados() → JSON (String)
    ↓
ConverteDados.obterDados() → DadosSerie, DadosTemporada, DadosEpisodio
    ↓
Java Streams (filter, map, flatMap, sorted, limit)
    ↓
Episodio (entidade de domínio)
    ↓
Exibição no console
```

### Operações de Stream em ordem de uso

```
stream()
    ↓
[Operações Intermediárias]
    ├── filter()
    ├── map()
    ├── flatMap()
    ├── sorted()
    ├── limit()
    ├── skip()
    └── distinct()
    ↓
[Operação Terminal]
    ├── forEach()
    ├── collect()
    ├── count()
    ├── findFirst()
    ├── anyMatch()
    └── allMatch()
```

---

## Referências Rápidas

### Quando usar cada operação

| Operação | Quando usar | Retorna | Tipo |
|----------|-----------|---------|------|
| `filter()` | Remover elementos indesejados | Stream | Intermediária |
| `map()` | Transformar elementos | Stream | Intermediária |
| `flatMap()` | Achatar estruturas aninhadas | Stream | Intermediária |
| `sorted()` | Ordenar elementos | Stream | Intermediária |
| `limit()` | Pegar primeiros N | Stream | Intermediária |
| `peek()` | Espiar/debugar elementos | Stream | Intermediária |
| `forEach()` | Executar ação em cada elemento | void | Terminal |
| `collect()` | Converter em coleção | Coleção | Terminal |
| `count()` | Contar elementos | long | Terminal |
| `findFirst()` | Pegar primeiro | Optional | Terminal |

### Referências de método comuns

```java
System.out::println          // Equivalente a: e -> System.out.println(e)
String::toUpperCase          // Equivalente a: s -> s.toUpperCase()
Integer::parseInt            // Equivalente a: s -> Integer.parseInt(s)
Episodio::getTitulo          // Equivalente a: e -> e.getTitulo()
Episodio::new                // Equivalente a: (t, d) -> new Episodio(t, d)
```

---

## Conclusão

Este projeto demonstra os conceitos fundamentais de:

1. **Spring Boot**: Framework para simplificar desenvolvimento Java
2. **Jackson**: Desserialização de JSON
3. **Interfaces e Implementação**: Padrão de design para desacoplamento
4. **Java Streams**: Processamento funcional de coleções
5. **Expressões Lambda**: Sintaxe concisa para funções anônimas
6. **Operações de Stream**: filter, map, flatMap, sorted, limit, forEach, collect

Esses conceitos são fundamentais para desenvolvimento Java moderno e aplicáveis em qualquer projeto!
