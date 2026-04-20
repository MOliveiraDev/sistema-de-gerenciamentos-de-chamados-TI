# Domínio de Gerenciamento de Técnicos (gtecnicos)

Este domínio é responsável pelo gerenciamento dos técnicos de TI, incluindo:

- Cadastro, atualização e exclusão de técnicos
- Listagem e busca de técnicos
- Atribuição de técnicos aos chamados
- Visualização de chamados atribuídos

## Estrutura
- controller: Endpoints de técnicos
- dto: Objetos de transferência de dados para técnicos
- entity: Entidades relacionadas a técnicos
- exception: Exceções específicas de técnicos
- repository: Persistência de dados de técnicos
- service: Lógica de negócio de técnicos

## Rotas de Técnicos

### Listar Técnicos
Lista todos os técnicos cadastrados.

- **Endpoint:** `GET /api/tecnicos`
- **Permissão:** ADMIN, TECNICO
- **Resposta:**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@empresa.com"
  }
]
```
- **Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/tecnicos -H 'Authorization: Bearer <jwt_token>'
```

---

### Buscar Técnico por ID
Busca um técnico pelo seu identificador.

- **Endpoint:** `GET /api/tecnicos/{id}`
- **Permissão:** ADMIN
- **Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@empresa.com"
}
```
- **Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/tecnicos/1 -H 'Authorization: Bearer <jwt_token>'
```

---

### Cadastro de Técnico
Cadastra um novo técnico.

- **Endpoint:** `POST /api/tecnicos`
- **Permissão:** ADMIN
- **Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@empresa.com",
  "senha": "senha123"
}
```
- **Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@empresa.com"
}
```
- **Exemplo cURL:**
```bash
curl -X POST \
  http://localhost:8080/api/tecnicos \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "nome": "João Silva",
    "email": "joao@empresa.com",
    "senha": "senha123"
  }'
```

---

### Atualizar Técnico
Atualiza os dados de um técnico existente.

- **Endpoint:** `PUT /api/tecnicos/{id}`
- **Permissão:** ADMIN
- **Body:**
```json
{
  "nome": "João Silva Atualizado",
  "email": "joao@empresa.com"
}
```
- **Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva Atualizado",
  "email": "joao@empresa.com"
}
```
- **Exemplo cURL:**
```bash
curl -X PUT \
  http://localhost:8080/api/tecnicos/1 \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <jwt_token>' \
  -d '{
    "nome": "João Silva Atualizado",
    "email": "joao@empresa.com"
  }'
```

---

### Excluir Técnico
Remove um técnico do sistema.

- **Endpoint:** `DELETE /api/tecnicos/{id}`
- **Permissão:** ADMIN
- **Exemplo cURL:**
```bash
curl -X DELETE http://localhost:8080/api/tecnicos/1 -H 'Authorization: Bearer <jwt_token>'
```
