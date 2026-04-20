# Domínio de Autenticação (auth)

Este domínio é responsável por toda a lógica de autenticação e autorização do sistema, incluindo:

- Cadastro e autenticação de usuários
- Geração e validação de tokens JWT
- Gerenciamento de perfis e permissões de acesso
- Controle de sessões e segurança

## Estrutura
- controller: Endpoints de autenticação
- dto: Objetos de transferência de dados para autenticação
- entity: Entidades relacionadas à autenticação
- enums: Enumerações de perfis e permissões
- exception: Exceções específicas de autenticação
- repository: Persistência de dados de autenticação
- service: Lógica de negócio de autenticação

## Rotas de Autenticação

### Login
Realiza a autenticação do usuário e retorna um token JWT.

- **Endpoint:** `POST /api/auth/login`
- **Body:**
```json
{
  "email": "usuario@empresa.com",
  "senha": "senha123"
}
```
- **Resposta:**
```json
{
  "usuarioId": 1,
  "nome": "Usuário Exemplo",
  "email": "usuario@empresa.com",
  "role": "ROLE_USER",
  "tokenType": "Bearer",
  "accessToken": "<jwt_token>",
  "mensagem": "Login realizado com sucesso."
}
```
- **Exemplo cURL:**
```bash
curl -X POST \
  http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "usuario@empresa.com",
    "senha": "senha123"
  }'
```

---

### Logout
Realiza o logout do usuário, invalidando o token JWT.

- **Endpoint:** `POST /api/auth/logout`
- **Headers:**
  - `Authorization: Bearer <jwt_token>`
- **Exemplo cURL:**
```bash
curl -X POST \
  http://localhost:8080/api/auth/logout \
  -H 'Authorization: Bearer <jwt_token>'
```

---

### Usuário Autenticado
Retorna os dados do usuário autenticado.

- **Endpoint:** `GET /api/auth/me`
- **Headers:**
  - `Authorization: Bearer <jwt_token>`
- **Resposta:**
```json
{
  "nome": "Usuário Exemplo",
  "email": "usuario@empresa.com",
  "role": "ROLE_USER"
}
```
- **Exemplo cURL:**
```bash
curl -X GET \
  http://localhost:8080/api/auth/me \
  -H 'Authorization: Bearer <jwt_token>'
```
