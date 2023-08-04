# Microsserviço de Administração do Catálogo de Vídeos - Parte I

O microsserviço de Administração do Catálogo de Vídeos é a aplicação _backend_ responsável por gerenciar os vídeos, incluindo as categorias, os gêneros e os membros do elenco.

Dentro da dinâmica do sistema:

1. A aplicação _Backend Admin_ do Catálogo de Vídeos vai falar com o banco de dados, salvar os dados dos vídeos, dos gêneros, das categorias e membros do elenco;
2. A aplicação _Frontend Admin_ do Catálogo de Vídeos vai falar com a _API_ do _backend_ para realizar as ações de cadastro;
3. A aplicação _Encoder_ de Vídeos vai acessar os vídeos que forem enviados via _Backend_ de Administração de Vídeos, fazer os _encoding_ e salvar os dados em um _bucket_ no _Google Cloud Storage_.

Esta primeira parte contempla o desenvolvimento para o Agregado (segundo _Domain-Driven Design_ (_DDD_)) de Categorias.

Com relação ao _software design_, a aplicação segue uma arquitetura _middle-out_, baseada nos modelos de _Clean Architecture_ e _DDD_.

Estão envolvidas, na aplicação, tecnologias de:

- Backend

  - Java (JDK 17)
  - Spring Boot 3
  - Gradle (gerenciador de dependências)
  - Spring Data & JPA
  - MySQL
  - Flyway (gerenciamento do banco de dados)
  - H2 (testes integrados de persistência)
  - JUnit Jupiter (testes unitários)
  - Mockito Junit Jupiter (testes integrados)
  - Testcontainers MySQL (testes end-to-end)
  - Springdoc-openapi (documentação da API)

O desenvolvimento da aplicação é baseado na metodologia _TDD_ (_Test-Driven Development_), sendo desenvolvidos:

- Testes unitários para a camada de _domain_ (ou _Entities_, segundo _Clean Architecture_) e de _application_ (ou _Use Cases_, segundo _Clean Architecture_);
- Testes de integração de persistência e _web_ para a camada de _infrastructure_ (ou _Frameworks_, segundo _Clean Architecture_);
- E, por fim, testes _end-to-end_ e de regressão manual via _Postman_.
