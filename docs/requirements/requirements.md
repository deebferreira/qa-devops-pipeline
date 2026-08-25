# Requisitos Funcionais — QA Pipeline Lab

## 1. Objetivo

Este documento descreve os requisitos funcionais, critérios de aceite e cenários de teste da API de gerenciamento de tarefas do projeto **QA Pipeline Lab**.

A aplicação terá como objetivo permitir o gerenciamento de tarefas por meio de uma API REST.

Cada requisito funcional será identificado por `RF`, cada critério de aceite por `AC` e cada cenário de teste por `TS`.

A rastreabilidade seguirá o modelo:

`RF → AC → TS`

---

# RF-001 — Criar tarefa

O sistema deve permitir a criação de uma nova tarefa por meio do endpoint:

`POST /tasks`

Uma tarefa deverá possuir os seguintes dados:

* título;
* descrição;
* prioridade;
* status;
* data de criação;
* data de atualização.

O identificador da tarefa deverá ser gerado automaticamente pelo sistema.

## AC-001 — Criar tarefa com dados válidos

O sistema deve permitir a criação de uma tarefa quando os dados obrigatórios forem válidos.

### TS-001 — Criar tarefa com título e prioridade válidos

```gherkin
Funcionalidade: Criação de tarefas

Cenário: Criar tarefa com dados válidos
  Dado que o cliente possui um título válido
  E informa uma prioridade válida
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
  E deve retornar os dados da tarefa criada
  E deve retornar um identificador para a tarefa
```

---

## AC-002 — Título obrigatório

O campo `title` deve ser obrigatório durante a criação da tarefa.

### TS-002 — Criar tarefa sem informar o título

```gherkin
Cenário: Tentar criar tarefa sem título
  Dado que o cliente não informa o campo "title"
  E informa os demais campos obrigatórios corretamente
  Quando enviar uma requisição POST para "/tasks"
  Então a API não deve criar a tarefa
  E deve retornar o status HTTP 400
  E deve informar que o título é obrigatório
```

---

## AC-003 — Título não pode ser vazio

O sistema não deve aceitar títulos vazios.

### TS-003 — Criar tarefa com título vazio

```gherkin
Cenário: Tentar criar tarefa com título vazio
  Dado que o cliente informa o campo "title" com valor vazio
  E informa uma prioridade válida
  Quando enviar uma requisição POST para "/tasks"
  Então a API não deve criar a tarefa
  E deve retornar o status HTTP 400
  E deve informar que o título não pode ser vazio
```

---

## AC-004 — Limite máximo do título

O campo `title` deve possuir no máximo 100 caracteres.

### TS-004 — Criar tarefa com título contendo exatamente 100 caracteres

```gherkin
Cenário: Criar tarefa com título no limite máximo permitido
  Dado que o cliente informa um título contendo exatamente 100 caracteres
  E informa uma prioridade válida
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
```

### TS-005 — Criar tarefa com título contendo 101 caracteres

```gherkin
Cenário: Tentar criar tarefa com título acima do limite permitido
  Dado que o cliente informa um título contendo 101 caracteres
  E informa uma prioridade válida
  Quando enviar uma requisição POST para "/tasks"
  Então a API não deve criar a tarefa
  E deve retornar o status HTTP 400
  E deve informar que o título excede o limite permitido
```

---

## AC-005 — Prioridade obrigatória

O campo `priority` deve ser obrigatório.

### TS-006 — Criar tarefa sem prioridade

```gherkin
Cenário: Tentar criar tarefa sem prioridade
  Dado que o cliente informa um título válido
  E não informa o campo "priority"
  Quando enviar uma requisição POST para "/tasks"
  Então a API não deve criar a tarefa
  E deve retornar o status HTTP 400
  E deve informar que a prioridade é obrigatória
```

---

## AC-006 — Valores permitidos para prioridade

O campo `priority` deve aceitar somente os valores:

* LOW
* MEDIUM
* HIGH

### TS-007 — Criar tarefa com prioridade LOW

```gherkin
Cenário: Criar tarefa com prioridade LOW
  Dado que o cliente informa um título válido
  E informa a prioridade "LOW"
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
  E a tarefa deve possuir prioridade "LOW"
```

### TS-008 — Criar tarefa com prioridade MEDIUM

```gherkin
Cenário: Criar tarefa com prioridade MEDIUM
  Dado que o cliente informa um título válido
  E informa a prioridade "MEDIUM"
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
  E a tarefa deve possuir prioridade "MEDIUM"
```

### TS-009 — Criar tarefa com prioridade HIGH

```gherkin
Cenário: Criar tarefa com prioridade HIGH
  Dado que o cliente informa um título válido
  E informa a prioridade "HIGH"
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
  E a tarefa deve possuir prioridade "HIGH"
```

### TS-010 — Criar tarefa com prioridade inválida

```gherkin
Cenário: Tentar criar tarefa com prioridade inválida
  Dado que o cliente informa um título válido
  E informa uma prioridade diferente de "LOW", "MEDIUM" ou "HIGH"
  Quando enviar uma requisição POST para "/tasks"
  Então a API não deve criar a tarefa
  E deve retornar o status HTTP 400
```

---

## AC-007 — Status inicial da tarefa

Uma nova tarefa deve ser criada com o status inicial `TODO`.

### TS-011 — Validar status inicial da tarefa

```gherkin
Cenário: Criar tarefa sem informar status
  Dado que o cliente informa os dados obrigatórios corretamente
  E não informa o campo "status"
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
  E a tarefa criada deve possuir o status "TODO"
```

---

## AC-008 — Descrição opcional

O campo `description` deve ser opcional.

### TS-012 — Criar tarefa sem descrição

```gherkin
Cenário: Criar tarefa sem descrição
  Dado que o cliente informa um título válido
  E informa uma prioridade válida
  E não informa uma descrição
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
```

---

## AC-009 — Limite máximo da descrição

O campo `description` deve possuir no máximo 500 caracteres.

### TS-013 — Criar tarefa com descrição contendo exatamente 500 caracteres

```gherkin
Cenário: Criar tarefa com descrição no limite máximo permitido
  Dado que o cliente informa uma descrição contendo exatamente 500 caracteres
  E informa os demais campos obrigatórios corretamente
  Quando enviar uma requisição POST para "/tasks"
  Então a API deve criar a tarefa
  E deve retornar o status HTTP 201
```

### TS-014 — Criar tarefa com descrição contendo 501 caracteres

```gherkin
Cenário: Tentar criar tarefa com descrição acima do limite permitido
  Dado que o cliente informa uma descrição contendo 501 caracteres
  E informa os demais campos obrigatórios corretamente
  Quando enviar uma requisição POST para "/tasks"
  Então a API não deve criar a tarefa
  E deve retornar o status HTTP 400
```

---

# RF-002 — Listar tarefas

O sistema deve permitir a consulta de todas as tarefas cadastradas por meio do endpoint:

`GET /tasks`

## AC-010 — Listar tarefas existentes

Quando existirem tarefas cadastradas, a API deve retornar a lista de tarefas.

### TS-015 — Consultar lista com tarefas cadastradas

```gherkin
Funcionalidade: Consulta de tarefas

Cenário: Listar tarefas existentes
  Dado que existem tarefas cadastradas
  Quando o cliente enviar uma requisição GET para "/tasks"
  Então a API deve retornar o status HTTP 200
  E deve retornar uma lista de tarefas
  E a lista deve conter as tarefas cadastradas
```

---

## AC-011 — Lista vazia

Quando não existirem tarefas cadastradas, a API deve retornar uma lista vazia.

### TS-016 — Consultar tarefas quando não existem registros

```gherkin
Cenário: Listar tarefas quando não existem tarefas cadastradas
  Dado que não existem tarefas cadastradas
  Quando o cliente enviar uma requisição GET para "/tasks"
  Então a API deve retornar o status HTTP 200
  E deve retornar uma lista vazia
```

---

# RF-003 — Consultar tarefa por ID

O sistema deve permitir consultar uma tarefa específica por meio do endpoint:

`GET /tasks/{id}`

## AC-012 — Consultar tarefa existente

Quando o identificador informado pertencer a uma tarefa existente, a API deve retornar seus dados.

### TS-017 — Consultar tarefa existente por ID

```gherkin
Cenário: Consultar tarefa existente
  Dado que existe uma tarefa cadastrada
  E o cliente possui o identificador dessa tarefa
  Quando enviar uma requisição GET para "/tasks/{id}"
  Então a API deve retornar o status HTTP 200
  E deve retornar os dados da tarefa correspondente
```

---

## AC-013 — Consultar tarefa inexistente

Quando o identificador informado não existir, a API deve informar que o recurso não foi encontrado.

### TS-018 — Consultar tarefa com ID inexistente

```gherkin
Cenário: Consultar tarefa inexistente
  Dado que não existe uma tarefa para o identificador informado
  Quando o cliente enviar uma requisição GET para "/tasks/{id}"
  Então a API deve retornar o status HTTP 404
  E deve informar que a tarefa não foi encontrada
```

---

# RF-004 — Atualizar tarefa

O sistema deve permitir atualizar os dados de uma tarefa existente por meio do endpoint:

`PUT /tasks/{id}`

## AC-014 — Atualizar tarefa existente com dados válidos

A API deve permitir alterar os dados de uma tarefa existente quando os novos valores forem válidos.

### TS-019 — Atualizar tarefa existente

```gherkin
Funcionalidade: Atualização de tarefas

Cenário: Atualizar tarefa com dados válidos
  Dado que existe uma tarefa cadastrada
  E o cliente informa novos dados válidos
  Quando enviar uma requisição PUT para "/tasks/{id}"
  Então a API deve atualizar a tarefa
  E deve retornar o status HTTP 200
  E deve retornar os dados atualizados
```

---

## AC-015 — Validar título durante atualização

As mesmas regras de título utilizadas na criação devem ser aplicadas na atualização.

### TS-020 — Atualizar tarefa utilizando título vazio

```gherkin
Cenário: Tentar atualizar tarefa com título vazio
  Dado que existe uma tarefa cadastrada
  E o cliente informa um título vazio
  Quando enviar uma requisição PUT para "/tasks/{id}"
  Então a API não deve atualizar a tarefa
  E deve retornar o status HTTP 400
```

### TS-021 — Atualizar tarefa com título acima de 100 caracteres

```gherkin
Cenário: Tentar atualizar tarefa com título acima do limite
  Dado que existe uma tarefa cadastrada
  E o cliente informa um título contendo mais de 100 caracteres
  Quando enviar uma requisição PUT para "/tasks/{id}"
  Então a API não deve atualizar a tarefa
  E deve retornar o status HTTP 400
```

---

## AC-016 — Atualizar tarefa inexistente

A API não deve permitir atualização de uma tarefa inexistente.

### TS-022 — Atualizar tarefa utilizando ID inexistente

```gherkin
Cenário: Tentar atualizar tarefa inexistente
  Dado que não existe uma tarefa para o identificador informado
  Quando o cliente enviar uma requisição PUT para "/tasks/{id}"
  Então a API deve retornar o status HTTP 404
  E não deve criar uma nova tarefa automaticamente
```

---

# RF-005 — Alterar status da tarefa

O sistema deve permitir alterar o status de uma tarefa por meio do endpoint:

`PATCH /tasks/{id}/status`

Os status permitidos são:

* TODO
* IN_PROGRESS
* DONE

## AC-017 — Alterar status para IN_PROGRESS

Uma tarefa existente deve poder ter seu status alterado para `IN_PROGRESS`.

### TS-023 — Alterar status de TODO para IN_PROGRESS

```gherkin
Funcionalidade: Alteração de status

Cenário: Alterar tarefa para IN_PROGRESS
  Dado que existe uma tarefa com status "TODO"
  Quando o cliente solicitar a alteração do status para "IN_PROGRESS"
  Então a API deve atualizar o status da tarefa
  E deve retornar o status HTTP 200
  E a tarefa deve possuir o status "IN_PROGRESS"
```

---

## AC-018 — Alterar status para DONE

Uma tarefa existente deve poder ter seu status alterado para `DONE`.

### TS-024 — Alterar status de IN_PROGRESS para DONE

```gherkin
Cenário: Alterar tarefa para DONE
  Dado que existe uma tarefa com status "IN_PROGRESS"
  Quando o cliente solicitar a alteração do status para "DONE"
  Então a API deve atualizar o status da tarefa
  E deve retornar o status HTTP 200
  E a tarefa deve possuir o status "DONE"
```

---

## AC-019 — Rejeitar status inválido

A API deve aceitar somente os status definidos pelo sistema.

### TS-025 — Alterar tarefa para status inválido

```gherkin
Cenário: Tentar alterar tarefa para status inválido
  Dado que existe uma tarefa cadastrada
  Quando o cliente solicitar a alteração para um status não permitido
  Então a API não deve alterar a tarefa
  E deve retornar o status HTTP 400
```

---

## AC-020 — Alterar status de tarefa inexistente

A API não deve permitir alterar o status de uma tarefa inexistente.

### TS-026 — Alterar status utilizando ID inexistente

```gherkin
Cenário: Tentar alterar status de tarefa inexistente
  Dado que não existe uma tarefa para o identificador informado
  Quando o cliente solicitar a alteração do status
  Então a API deve retornar o status HTTP 404
```

---

# RF-006 — Excluir tarefa

O sistema deve permitir excluir uma tarefa por meio do endpoint:

`DELETE /tasks/{id}`

## AC-021 — Excluir tarefa existente

Quando a tarefa existir, ela deve ser removida do sistema.

### TS-027 — Excluir tarefa existente

```gherkin
Funcionalidade: Exclusão de tarefas

Cenário: Excluir tarefa existente
  Dado que existe uma tarefa cadastrada
  Quando o cliente enviar uma requisição DELETE para "/tasks/{id}"
  Então a tarefa deve ser removida
  E a API deve retornar o status HTTP 204
```

---

## AC-022 — Tarefa não deve existir após exclusão

Após excluir uma tarefa, ela não deve mais ser encontrada.

### TS-028 — Consultar tarefa após exclusão

```gherkin
Cenário: Consultar tarefa que foi excluída
  Dado que uma tarefa foi excluída anteriormente
  Quando o cliente enviar uma requisição GET para "/tasks/{id}"
  Então a API deve retornar o status HTTP 404
  E deve informar que a tarefa não foi encontrada
```

---

## AC-023 — Excluir tarefa inexistente

Ao tentar excluir uma tarefa inexistente, a API deve informar que o recurso não foi encontrado.

### TS-029 — Excluir tarefa com ID inexistente

```gherkin
Cenário: Tentar excluir tarefa inexistente
  Dado que não existe uma tarefa para o identificador informado
  Quando o cliente enviar uma requisição DELETE para "/tasks/{id}"
  Então a API deve retornar o status HTTP 404
```

---

# Matriz de rastreabilidade

| Requisito                        | Critérios de aceite | Cenários de teste |
| -------------------------------- | ------------------- | ----------------- |
| RF-001 — Criar tarefa            | AC-001 a AC-009     | TS-001 a TS-014   |
| RF-002 — Listar tarefas          | AC-010 a AC-011     | TS-015 a TS-016   |
| RF-003 — Consultar tarefa por ID | AC-012 a AC-013     | TS-017 a TS-018   |
| RF-004 — Atualizar tarefa        | AC-014 a AC-016     | TS-019 a TS-022   |
| RF-005 — Alterar status          | AC-017 a AC-020     | TS-023 a TS-026   |
| RF-006 — Excluir tarefa          | AC-021 a AC-023     | TS-027 a TS-029   |

---

# Regras de negócio e validação consolidadas

## Título

* obrigatório;
* não pode ser vazio;
* tamanho máximo de 100 caracteres.

## Descrição

* opcional;
* tamanho máximo de 500 caracteres.

## Prioridade

Valores permitidos:

* LOW;
* MEDIUM;
* HIGH.

## Status

Valores permitidos:

* TODO;
* IN_PROGRESS;
* DONE.

Uma nova tarefa deverá iniciar com status:

`TODO`

## Identificador

O identificador da tarefa deverá ser gerado automaticamente pelo sistema.

## Datas

O sistema deverá manter:

* data de criação;
* data da última atualização.

---
