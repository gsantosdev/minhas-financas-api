


# Minhas Finanças API

Uma API para gerenciamento de finanças pessoais.

## Funcionalidades

A API permite o gerenciamento de finanças pessoais, incluindo o cadastro de usuários, criação, atualização e exclusão de lançamentos financeiros (receitas e despesas) e consulta de lançamentos financeiros por período.

### Rotas

A API possui as seguintes rotas:

- `POST /api/usuarios`: cria um novo usuário;
- `POST /api/lancamentos`: cria um novo lançamento financeiro;
- `PUT /api/lancamentos/{id}`: atualiza um lançamento financeiro existente;
- `DELETE /api/lancamentos/{id}`: exclui um lançamento financeiro existente;
- `GET /api/lancamentos`: lista todos os lançamentos financeiros;
- `GET /api/lancamentos/{id}`: busca um lançamento financeiro por ID;
- `GET /api/lancamentos?usuarioId={usuarioId}&mes={mes}&ano={ano}`: lista os lançamentos financeiros de um usuário em um determinado mês e ano.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
