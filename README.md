# Sistema de Gerenciamento de Chamados de TI

Sistema completo para registro, acompanhamento e resolução de chamados técnicos, com operações CRUD, autenticação JWT, controle de perfis, e geração de relatórios em PDF.

## Funcionalidades
- Cadastro e autenticação de usuários (JWT)
- Perfis: Administrador, Técnico, Solicitante
- Gerenciamento de chamados (criação, atualização, exclusão, histórico)
- Atribuição de técnicos aos chamados
- Filtros por status, prioridade, técnico e solicitante
- Geração de relatórios analíticos e exportação em PDF

## Estrutura dos Domínios
- **auth**: Autenticação e autorização
- **gsolicitantes**: Gerenciamento de usuários solicitantes
- **gtecnicos**: Gerenciamento de técnicos
- **gchamados**: Gerenciamento de chamados
  - **relatorios**: Subdomínio para relatórios de chamados
- **shared**: Configurações e utilitários compartilhados

## Como Executar
1. Clone o repositório
2. Configure o banco de dados e variáveis de ambiente (ex: JWT secret)
3. Execute o projeto com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acesse a API em `http://localhost:8080`

## Documentação das Rotas
Consulte os arquivos `README.md` dentro de cada domínio para exemplos detalhados de uso das rotas (incluindo cURL), permissões e formatos de requisição/resposta.

# Arquitetura

## Arquitetura do Sistema
Visão geral da arquitetura em monolito modular: o cliente se comunica via API Interface, que repassa as requisições para os módulos internos (Autenticação, Gerenciamento de Técnicos, Gerenciamento de Usuários Solicitantes e Gerenciamento de Chamados), todos compartilhando um único Banco de Dados.

![Arquitetura do sistema](assets/SGCTI%20Arquitetura%20do%20sistema.jpg)

---

## Atributos do Sistema (Modelo de Dados)
Diagrama das tabelas do banco de dados e seus relacionamentos: `usuarios_tbl` é a base central, referenciada por `solicitantes_tbl` e `tecnicos_tbl` via FK. A tabela `chamados_tbl` se relaciona com solicitantes e técnicos.

![Atributos do sistema](assets/SGCTI%20Atributos%20do%20sistema.jpg)

---

## Hierarquia de Perfis
Estrutura hierárquica dos perfis do sistema: o **Administrador** está no topo, seguido pelos técnicos de **Suporte TI**, e na base os **Usuários** solicitantes.

![Hierarquia](assets/SGCTI%20Hierarquia.jpg)

---

## Permissões do Administrador
O Administrador possui acesso total ao sistema: registrar, editar e excluir chamados; cadastrar, editar e excluir técnicos e usuários; atribuir técnicos a chamados; atualizar status; visualizar histórico; filtrar chamados; e gerar/exportar relatórios em PDF.

![Permissões do administrador](assets/SGCTI%20Permissões%20do%20administrador.jpg)

---

## Permissões do Suporte TI (Técnico)
O técnico de Suporte TI pode: atender chamados, atribuir técnicos, filtrar chamados por status, visualizar histórico de chamados, atualizar o status do chamado, e listar técnicos e usuários.

![Permissões do suporte ti](assets/SGCTI%20Permissões%20do%20suporte%20ti.jpg)

---

## Permissões do Usuário (Solicitante)
O usuário solicitante possui acesso limitado: registrar chamados, visualizar os próprios chamados e filtrar por status.

![Permissões do usuário](assets/SGCTI%20Permissões%20do%20usuário.jpg)

---

## Responsabilidades dos Domínios
Mapa completo das funcionalidades organizadas por domínio: **Autenticação** (login/logout), **Gerenciamento de Técnicos**, **Gerenciamento de Usuários** e **Gerenciamento de Chamados** — incluindo o subdomínio de **Relatórios** (por período, por técnico e por status), com exportação em PDF.

![Responsabilidades dos Domínios](assets/SGCTI%20Responsabilidades%20dos%20Domínios.jpg)

## Licença
Projeto acadêmico. Uso livre para fins de estudo.
