# Rick and Morty BFF

Back-end Java atuando como BFF, consumindo a API pública do Rick and Morty.

## Tecnologias
- Java 17.0.18
- Spring Boot
- Spring Cache
- Maven

## Como rodar
```bash
.\mvnw spring-boot:run
```

## Endpoints

### Listar personagens (paginado)
GET /personagens?page=1

### Buscar personagem por nome
GET /personagens/{nome}
Exemplo:
GET /personagens/rick ou /personagens/Cool Rick

## Testes

Para rodar os testes unitários e integrados:

```bash
.\mvnw test
