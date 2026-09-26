# Furna de Lampião

Sistema para gerenciamento de cavernas, setores, expedições e profissionais envolvidos nas atividades espeleológicas.

## Stack

- Java 11
- JPA 2.2
- Hibernate 5.6
- PostgreSQL 15
- Maven
- Lombok
- Docker / Docker Compose

## Estrutura

```text
.
├── src
│   ├── main
│   │   ├── java/com
│   │   │   └── furnadelampiao
│   │   │        ├── domain
│   │   │        ├── enums
│   │   │        ├── repository
│   │   │        ├── service
│   │            ├── seed
│   │   │        │   
│   │   │        └── Main.java
│   │   └── resources
│   │       └── META-INF
│   │           └── persistence.xml
│   └── test
├── docker-compose.yml
├── pom.xml
└── README.md
```
## Diagrama de classes
![SystemClassDiagram](docs/SystemClassDiagram.svg)

## Seed
```bash
mvn compile exec:java "-Dexec.mainClass=com.furnadelampiao.seed.SeedRunner"
```
ou 
```bash
make seed
```

## Docker
desenvolvimento
```bash
make db-up
```
```bash
make db-down
```


