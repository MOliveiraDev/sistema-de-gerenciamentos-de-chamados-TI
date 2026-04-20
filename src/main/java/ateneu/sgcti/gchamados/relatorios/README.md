# Domínio de Relatórios de Chamados (gchamados/relatorios)

Este subdomínio é responsável pela geração e exportação de relatórios relacionados aos chamados de TI.

## Estrutura
- controller: Endpoints de relatórios
- dto: Objetos de transferência de dados para relatórios
- service: Lógica de negócio para geração de relatórios

## Rotas de Relatórios

### Relatório por Período
Gera um relatório de chamados em um intervalo de datas.

- **Endpoint:** `GET /api/chamados/relatorios/por-periodo`
- **Permissão:** ADMIN
- **Parâmetros (query):**
  - `inicio` (obrigatório, formato: yyyy-MM-dd)
  - `fim` (obrigatório, formato: yyyy-MM-dd)
- **Exemplo cURL:**
```bash
curl -X GET 'http://localhost:8080/api/chamados/relatorios/por-periodo?inicio=2024-01-01&fim=2024-01-31' -H 'Authorization: Bearer <jwt_token>'
```

---

### Relatório por Status
Gera um relatório agrupando chamados por status.

- **Endpoint:** `GET /api/chamados/relatorios/por-status`
- **Permissão:** ADMIN
- **Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/chamados/relatorios/por-status -H 'Authorization: Bearer <jwt_token>'
```

---

### Relatório por Técnico
Gera um relatório agrupando chamados por técnico responsável.

- **Endpoint:** `GET /api/chamados/relatorios/por-tecnico`
- **Permissão:** ADMIN
- **Exemplo cURL:**
```bash
curl -X GET http://localhost:8080/api/chamados/relatorios/por-tecnico -H 'Authorization: Bearer <jwt_token>'
```

---

### Exportar Relatório em PDF
Exporta um relatório de chamados em formato PDF, com filtros opcionais.

- **Endpoint:** `GET /api/chamados/relatorios/pdf`
- **Permissão:** ADMIN
- **Parâmetros (query, todos opcionais):**
  - `status`
  - `prioridade`
  - `tecnicoId`
  - `solicitanteId`
  - `inicio` (formato: yyyy-MM-dd)
  - `fim` (formato: yyyy-MM-dd)
- **Exemplo cURL:**
```bash
curl -X GET 'http://localhost:8080/api/chamados/relatorios/pdf?status=ABERTO&inicio=2024-01-01&fim=2024-01-31' \
  -H 'Authorization: Bearer <jwt_token>' \
  --output relatorio-chamados.pdf
```

