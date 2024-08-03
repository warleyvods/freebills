# FreeBills - API

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=warleyvods_freebills&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=warleyvods_freebills)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=warleyvods_freebills&metric=coverage)](https://sonarcloud.io/summary/new_code?id=warleyvods_freebills)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=warleyvods_freebills&metric=bugs)](https://sonarcloud.io/summary/new_code?id=warleyvods_freebills)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=warleyvods_freebills&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=warleyvods_freebills)

## Overview

Freebills is an innovative personal finance system designed to simplify and optimize your account management. With Freebills, you can manage your finances more easily and efficiently using our powerful API, which can also be deployed on-premise for greater control and customization.
## Features

- **Transaction Recording**: Register both income and expense transactions effortlessly.
- **Account Transfers**: Facilitate transfers between accounts with ease.
- **Transaction Categories**: Categorize your transactions for better tracking and management.
- **Multiple Accounts: Manage** various types of accounts including checking, investment, and savings accounts.
- **Settings Dashboard**: Customize your experience with a comprehensive settings panel.
- **Reports**: Generate detailed reports to gain insights into your financial activities.


## Technical Features

- Java 21
- Spring Boot 3.2.5
- Spring Security
- JPA and Hibernate
- Flyway
- OpenFeign
- Redis
- MapStruct
- Springdoc OpenAPI: Integrates OpenAPI 3 documentation
- OAuth2 Client
- Testing Frameworks: JUnit 5, Testcontainers
- SonarQube
- JaCoCo
- AWS S3 - Upload Pre-sign

## Getting Started

To get a local copy up and running, follow these steps:

1. Clone the repository.
2. Install the required dependencies.
3. Set up your database.
4. Run the application.

## Docker

Freebills is very easy to install and deploy in a Docker container.

By default look for the `run.yml` file in the project root, it will contain the docker-compose responsible for uploading the backend.
And run this command:

```sh
docker-compose up -d
```


## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Warley Silva
