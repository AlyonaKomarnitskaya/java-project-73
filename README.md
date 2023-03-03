### Hexlet tests and linter status:
[![Actions Status](https://github.com/LenaKomarnitskaya/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/LenaKomarnitskaya/java-project-73/actions)
[![Build](https://github.com/LenaKomarnitskaya/java-project-73/actions/workflows/build.yml/badge.svg)](https://github.com/LenaKomarnitskaya/java-project-73/actions/workflows/build.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/e5a3ab54129da28501f6/maintainability)](https://codeclimate.com/github/LenaKomarnitskaya/java-project-73/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/e5a3ab54129da28501f6/test_coverage)](https://codeclimate.com/github/LenaKomarnitskaya/java-project-73/test_coverage)


## Менеджер задач

[Task Manager](https://java-project-73-production-5299.up.railway.app/) - система управления задачами. Она позволяет ставить задачи, назначать исполнителей и менять их статусы. Для работы с системой требуется регистрация и аутентификация.

[API documentation](https://java-project-73-production-5299.up.railway.app/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

### Технологии

   * Java 17
   * Spring Boot, WVC, Data
   * Swagger, Lombok
   * Gradle
   * Liquibase
   * Spring Security, JWT

### Разработка

Для локального запуска необходимо установить:

   * JDK 17
   * Gradle 7.4
   * Make
   
## Локальный запуск приложения
```
make start
```

## Тестирование
```
make test
```

