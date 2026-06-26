---
trigger: always_on
description: Segurança para o projeto streaming — proteção de chave de API OMDB e consumo HTTP seguro
globs: "**/*.java"
---

# Segurança — streaming

## Chave de API OMDB

A chave está atualmente hardcoded em `Principal.java` (`private final String API_KEY = "&apikey=6585022c"`). Isso é aceitável para fins de aprendizado, mas siga estas regras:

- **Não duplique** a chave em outros arquivos
- **Não logue** a chave via `System.out.println` ou logger
- **Não commite** arquivos `.env` com valores reais

**Para externalizar** (quando solicitado), use variável de ambiente via `application.properties`:

```properties
# application.properties
omdb.api-key=${OMDB_API_KEY:6585022c}
```

```java
// Principal.java
@Value("${omdb.api-key}")
private String apiKey;
```

## Consumo HTTP (ConsumoApi)

- Use `ConsumoApi.obterDados()` apenas para a OMDB — não para URLs arbitrárias fornecidas por usuários
- O nome da série digitado no console passa por `.replace(" ", "+")` antes de compor a URL — suficiente para este caso de uso
- **Não faça requisições** para URLs construídas diretamente de input do usuário sem validação

## Tratamento de exceções

`ConsumoApi` envolve `IOException` e `InterruptedException` em `RuntimeException`. Aceitável para CLI de aprendizado. Se ampliar o projeto, use exceções específicas com mensagens descritivas.

## Checklist de segurança pré-commit

- [ ] Nenhuma credencial ou chave de API adicionada ao código
- [ ] Chave OMDB não aparece em logs ou saídas desnecessárias
- [ ] Não há requisições HTTP para URLs controladas pelo usuário sem validação

## Módulos relacionados

- `java-core.md` — padrões gerais do projeto
- `java-checklist.md` — checklist completo pré-commit
