# Projeto - Agendamento de Envio de Mensagens

## Ambiente para execução 
* [Java 11+](http://openjdk.java.net/projects/jdk/11/)
* [Docker](https://www.docker.com/)

## Tecnologias utilizadas na aplicação
* [Quarkus](https://quarkus.io/)
* [MySQL 8](https://www.mysql.com/)
* [Hibernate](http://hibernate.org/)
* [RestEasy](https://resteasy.github.io/)
* [MicroProfile](https://start.microprofile.io/)
* [Lombok](https://projectlombok.org/)

## Tecnologias utilizadas nos testes
* [H2](http://h2database.com/html/main.html)
* [Rest Assured](http://rest-assured.io/)

## Facilidades de utilização das versões do Java

* [jabba](https://github.com/shyiko/jabba)

## Endpoints



## Para executar a aplicação no modo de desenvolvimento

Você pode executar sua aplicação com o comando abaixo, 
no modo de desenvolvimento e isso habilita o modo de 'live coding':
```
./gradlew quarkusDev
```

## Empacotar e executar a aplicação

Empacotar: `./gradlew quarkusBuild`, gera o arquivo `comunicacao-scheduling-1.0.0-SNAPSHOT-runner.jar` no diretório `build`.

Executar: `java -jar build/comunicacao-scheduling-1.0.0-SNAPSHOT-runner.jar`.

Se você quiser construir um uber-jar, adicione a opção `--uber-jar` na linha de comando:
```
./gradlew quarkusBuild --uber-jar
```

## Criando um executavel nativo

Execute o comando: `./gradlew build -Dquarkus.package.type=native`.

Se você não tiver o GraalVM instalado, você pode criar o executavel usando um container com o comando: `./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true`.

Depois, é só executar com o comando: `./build/comunicacao-1.0.0-SNAPSHOT-runner`

Se tiver dúvidas, consulte o endereço: https://quarkus.io/guides/gradle-tooling#building-a-native-executable.

