# API Cadastro de Médicos (Voll.med)

API REST desenvolvida no curso da Alura para o sistema **Voll.med** — plataforma fictícia de agendamento de consultas médicas.

## O que faz

- Cadastro e listagem de médicos
- Especialidades médicas
- Endereços e dados de contato
- Validação de dados com Bean Validation
- Documentação automática com SpringDoc OpenAPI

## Tecnologias

- Java 17
- Spring Boot 3.3
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway (migrações)
- SpringDoc OpenAPI (Swagger)

## Como rodar

```bash
git clone https://github.com/Agsterr/Api_Cadastro_De_Medicos.git
cd Api_Cadastro_De_Medicos
mvn spring-boot:run
```

Configure o banco PostgreSQL em `application.properties` antes de iniciar.

## Documentação da API

Com a aplicação rodando:

- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Autor

**Agster Junior da Costa Santos** — projeto de estudo Alura
