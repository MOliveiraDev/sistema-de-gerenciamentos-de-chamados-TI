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

## Arquitetura
Veja a pasta `assets/` para diagramas e imagens da arquitetura do sistema:
- [Arquitetura do sistema](assets/SGCTI%20Arquitetura%20do%20sistema.jpg)
- [Atributos do sistema](assets/SGCTI%20Atributos%20do%20sistema.jpg)
- [Hierarquia](assets/SGCTI%20Hierarquia.jpg)
- [Permissões do administrador](assets/SGCTI%20Permissões%20do%20administrador.jpg)
- [Permissões do suporte ti](assets/SGCTI%20Permissões%20do%20suporte%20ti.jpg)
- [Permissões do usuário](assets/SGCTI%20Permissões%20do%20usuário.jpg)
- [Responsabilidades dos Domínios](assets/SGCTI%20Responsabilidades%20dos%20Domínios.jpg)

## Licença
Projeto acadêmico. Uso livre para fins de estudo.
