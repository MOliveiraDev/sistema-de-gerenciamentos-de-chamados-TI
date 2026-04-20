# Domínio de Gerenciamento de Chamados (gchamados)

Este domínio é responsável pelo ciclo de vida dos chamados de TI, incluindo:

- Criação, atualização e exclusão de chamados
- Atribuição de técnicos aos chamados
- Filtragem e listagem de chamados por status, prioridade, técnico ou solicitante
- Visualização do histórico de chamados
- Gerenciamento de status dos chamados

## Estrutura
- controller: Endpoints de chamados
- dto: Objetos de transferência de dados para chamados
- entity: Entidades relacionadas a chamados
- enums: Enumerações de status e tipos de chamados
- exception: Exceções específicas de chamados
- relatorios: Subdomínio para relatórios
- repository: Persistência de dados de chamados
- service: Lógica de negócio de chamados

## Rotas de Chamados

### Listar Chamados
Lista chamados com filtros opcionais por status, prioridade, técnico ou solicitante.

- **Endpoint:** `GET /api/chamados`
- **Permissão:** ADMIN, TECNICO, SOLICITANTE
- **Parâmetros (query):**
  - `status` (opcional)
  - `prioridade` (opcional)
  - `tecnicoId` (opcional)
  - `solicitanteId` (opcional)
- **Exemplo cURL:**
```bash
curl -X GET 'http://localhost:8080/api/chamados?status=ABERTO&prioridade=ALTA' -H 'Authorization: Bearer <jwt_token>'
```

---

### Buscar Chamado por ID
Busca um chamado pelo seu identificador.

- **Endpoint:** `GET /api/chamados/{id}`
- **Permissão:** ADMIN, SOLICITANTE
- **Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/chamados/1 -H 'Authorization: Bearer <jwt_token>'
```

---

### Registrar Chamado
Cria um novo chamado.

- **Endpoint:** `POST /api/chamados`
- **Permissão:** ADMIN, SOLICITANTE
- **Body:**
```json
{
  "titulo": "Problema no computador",
  "descricao": "O computador não liga.",
  "prioridade": "ALTA"
}
```
- **Exemplo cURL:**
```bash
curl -X POST \
  http://localhost:8080/api/chamados \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "titulo": "Problema no computador",
    "descricao": "O computador não liga.",
    "prioridade": "ALTA"
  }'
```

---

### Atualizar Chamado
Atualiza os dados de um chamado existente.

- **Endpoint:** `PUT /api/chamados/{id}`
- **Permissão:** ADMIN, TECNICO
- **Body:**
```json
{
  "titulo": "Problema no computador - atualizado",
  "descricao": "O computador não liga após atualização.",
  "prioridade": "MEDIA"
}
```
- **Exemplo cURL:**
```bash
curl -X PUT \
  http://localhost:8080/api/chamados/1 \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "titulo": "Problema no computador - atualizado",
    "descricao": "O computador não liga após atualização.",
    "prioridade": "MEDIA"
  }'
```

---

### Excluir Chamado
Remove um chamado do sistema.

- **Endpoint:** `DELETE /api/chamados/{id}`
- **Permissão:** ADMIN, TECNICO
- **Exemplo cURL:**
```bash
curl -X DELETE http://localhost:8080/api/chamados/1 -H 'Authorization: Bearer <jwt_token>'
```

---

### Atribuir Técnico ao Chamado
Atribui um técnico a um chamado.

- **Endpoint:** `PATCH /api/chamados/{id}/atribuir-tecnico`
- **Permissão:** ADMIN, TECNICO
- **Body:**
```json
{
  "tecnicoId": 2
}
```
- **Exemplo cURL:**
```bash
curl -X PATCH \
  http://localhost:8080/api/chamados/1/atribuir-tecnico \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "tecnicoId": 2
  }'
```

---

### Atualizar Status do Chamado
Atualiza o status de um chamado.

- **Endpoint:** `PATCH /api/chamados/{id}/status`
- **Permissão:** ADMIN, TECNICO
- **Body:**
```json
{
  "status": "EM_ANDAMENTO"
}
```
- **Exemplo cURL:**
```bash
curl -X PATCH \
  http://localhost:8080/api/chamados/1/status \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "status": "EM_ANDAMENTO"
  }'
```

---

### Atender Chamado
Marca o chamado como atendido por um técnico.

- **Endpoint:** `PATCH /api/chamados/{id}/atender`
- **Permissão:** ADMIN, TECNICO
- **Body:**
```json
{
  "tecnicoId": 2
}
```
- **Exemplo cURL:**
```bash
curl -X PATCH \
  http://localhost:8080/api/chamados/1/atender \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "tecnicoId": 2
  }'
```

---

### Listar Histórico do Chamado
Lista o histórico de alterações de um chamado.

- **Endpoint:** `GET /api/chamados/{id}/historico`
- **Permissão:** ADMIN, TECNICO, SOLICITANTE
- **Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/chamados/1/historico -H 'Authorization: Bearer <jwt_token>'
```
