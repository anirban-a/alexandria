# Alexandria

Front-end can be found [here](https://github.com/JessHua159/alexandria-ui).

## Setup

### Development Setup:

1. Install IntelliJ CE.
2. Install Oracle OpenJDK 18
3. Set Language level 17 in Project settings
4. Install `Maven`:
    1. `brew install maven` for macOS.
    2. Maven [for windows](https://mkyong.com/maven/how-to-install-maven-in-windows/)

5. Build the project: `mvn clean install`

### Setup to run the application:

6. Create a file named `application-local.yml`:
   ```
   spring:
       cloud:
           azure:
               cosmos:
                   endpoint:
                   key: 
                   database:
                   populate-query-metrics: true
   ```
7. Download and install Docker desktop.
8. Run local elastic-search cluster on docker: `docker run -p 9200:9200 \
   -e "discovery.type=single-node" \
   docker.elastic.co/elasticsearch/elasticsearch:7.10.0`.
9. Verify that ES cluster is running: `http://localhost:9200`.

10. Set application run configuration to `local` using the following
    flag: `spring.profiles.active=local`.

**Note**: Always run `mvn spring-javaformat:apply` to format code across codebase and to keep all
formatting consistent.

To run from the terminal:

```mvn spring-boot:run -Dspring-boot.run.profiles=local```

### Steps to run the application from `.jar` :

1. Create an account on MS Azure.
2. Create a CosmosDB resource, follow the instructions
   mentioned [here](https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/quickstart-portal#create-account)
   from
   Microsoft.
3. Download and install Docker desktop, follow the instructions
   mentioned [here](https://docs.docker.com/get-docker/) on
   official Docker website.
4. Start docker desktop
5. Go to your terminal/command prompt and run: `docker run -p 9200:9200 \
   -e "discovery.type=single-node" \
   docker.elastic.co/elasticsearch/elasticsearch:7.10.0`.
6. Verify that ES cluster is running: `http://localhost:9200`.
7. Make sure you have Java 17 or above installed.
8. Create a directory where you want to place the `.jar`.
9. Create an email account (Doesn't have to be Gmail, it can be anything) which you would want the
   application to use to
   send emails to users.
10. In that directory create a file named `application.yml`. Your `application.yml` should look like
    this:

```
spring:
  cloud:
    azure:
      cosmos:
        endpoint: <your cosmos DB end-point>
        key: <your cosmos DB key>
        database: <your database Name>
        populate-query-metrics: true
application:
  security:
    jwt:
      key: <choose a random long string>
  email:
    password: <email account password>
    accountId: noreply.your_email_id@gmail.com
ui:
  port: <Fron-end service port number>
```

11. For the port number of front-end service refer to the README of
    that [service](https://github.com/JessHua159/alexandria-ui).
12. To run this service, from your command prompt/terminal, fire the
    following: `java -jar alexandria-0.0.1-SNAPSHOT.jar --spring.config.location=application.yml`
