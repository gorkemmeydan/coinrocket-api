## CoinRocket

A full-stack web application for people to track their crypto assets with history and follow the current market by prices, trending coins and news. This repo shows the API.

## Technologies used

Spring Boot/Cloud, Hibernate, PostgreSQL, Redis, Docker and Java is used for the REST API.

### Security

For security, Oauth2 access and refresh opaque JWT tokens are used with Spring Security. Authorization and Resource server is divided in order to increase security.

### Fault Tolerance

Netflix Hystrix is used for latency and fault tolerance to isolate api endpoints. Previous results are cached for fallback. Circuit breaker pattern is enabled. Realtime monitoring is provided with Hystrix Dashboard.

### Documentation

Documentation is done with OpenAPI/Swagger.

### Testing

Unit and integration tests are done with Junit and 100% code coverage is achieved.

### Docker

docker-compose.yml in resources folder to setup the Postgres and Redis.

```bash
docker compose up
```

### Front-end

Fron-end is written with React.js, Next.js and TypeScript. Front-end can be seen [here](https://github.com/gorkemmeydan/coinrocket-interface)
