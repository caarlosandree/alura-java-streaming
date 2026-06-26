---
trigger: model_decision
description: Padrão de commits para o projeto streaming — Conventional Commits em português brasileiro, sem Co-authored-by
globs: "**/*"
---

# Padrão de Commits — streaming

## Formato

```
<tipo>: <descrição curta em português>

<corpo opcional — explica o quê e por quê, não o como>
```

## Tipos

| Tipo | Quando usar |
|---|---|
| `feat` | Nova funcionalidade ou novo filtro/operação |
| `fix` | Correção de bug ou comportamento inesperado |
| `refactor` | Refatoração sem mudar comportamento (ex: extrair método) |
| `test` | Adicionar ou corrigir testes |
| `docs` | Documentação (README, CLAUDE.md, comentários) |
| `chore` | Build, dependências, configuração do Gradle |
| `style` | Formatação, espaços, ponto-e-vírgula |

## Regras absolutas

- Mensagem **sempre em português brasileiro**
- Descrição com máximo **72 caracteres**
- Use **imperativo**: "adiciona", "corrige", "remove", "melhora", "extrai"
- **Nunca** inclua linha `Co-authored-by:` ou variações

## Exemplos do projeto

```
feat: adiciona filtro de episódios por avaliação mínima

refactor: extrai lógica de ordenação para método auxiliar em Principal

fix: corrige NullPointerException quando data de lançamento é nula

test: adiciona testes unitários para Episodio com data inválida

chore: atualiza Spring Boot para 4.1.0

feat: adiciona exibição de episódios por temporada usando flatMap
```

## Corpo detalhado (quando necessário)

Use quando a mudança envolver múltiplas partes ou a motivação não for óbvia:

```
refactor: reorganiza lógica de busca em Principal

- Extrai construção de URL para método privado montarUrl()
- Separa exibição de top 5 em método exibeTopEpisodios()
- Facilita testes unitários das partes individuais
```
