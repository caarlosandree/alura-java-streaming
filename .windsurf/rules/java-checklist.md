---
trigger: always_on
description: Checklist pré-commit para o projeto streaming — CLI Java com Spring Boot e Java Streams
globs: "**/*.java"
---

# Checklist pré-commit — streaming

Antes de commitar qualquer alteração, revise todos os itens abaixo.

## Build e compilação

- [ ] O projeto compila sem erros (`./gradlew compileJava`)
- [ ] Não há warnings críticos não justificados
- [ ] Não há imports não utilizados
- [ ] Não há código comentado ou arquivos de debug

## Qualidade de código

- [ ] Nomes de métodos, variáveis e parâmetros estão em **português**
- [ ] Novos DTOs de API usam `record` com `@JsonIgnoreProperties(ignoreUnknown = true)` e `@JsonAlias`
- [ ] Novos serviços têm interface correspondente (padrão `IConverteDados`)
- [ ] Lógica de transformação de coleções usa **Java Streams**
- [ ] Tratamento de `NumberFormatException` e `DateTimeException` nos pontos de parsing

## Segurança

- [ ] A chave de API OMDB não foi duplicada em novos arquivos
- [ ] A chave de API não aparece em logs ou `System.out.println` desnecessário
- [ ] Nenhuma credencial real foi adicionada ao código-fonte

## Testes

- [ ] Nova lógica de negócio (filtros, mapeamentos, conversões) tem teste unitário correspondente
- [ ] Os testes existentes ainda passam (`./gradlew test`)

## Commit

- [ ] Mensagem segue Conventional Commits em português (`feat:`, `fix:`, `refactor:`, etc.)
- [ ] Descrição curta com no máximo **72 caracteres**
- [ ] Nenhuma linha `Co-authored-by:` incluída

## Módulos relacionados

- `java-core.md` — padrões de código
- `java-testing.md` — estratégia de testes
- `commit.md` — padrão de commits
