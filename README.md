# Alexandria
<hr>

## Setup
1. Install IntelliJ CE.
2. Install Oracle OpenJDK 18
3. Set Language level 17 in Project settings
4. Install `Maven`:
   1. `brew install maven` for macOS.
   2. Maven [for windows](https://mkyong.com/maven/how-to-install-maven-in-windows/)
 
5. Build the project: `mvn clean install`

6. Create a file named `application-local.yml`:
   ```
   spring:
       cloud:
           azure:
               cosmos:
                   endpoint: https://csci-4440-sdd.documents.azure.com:443/
                   key: 
                   database:
                   populate-query-metrics: true
   ```
7. Download and install Docker desktop.
8. Run local elastic-search cluster on docker: `docker run -p 9200:9200 \
   -e "discovery.type=single-node" \
   docker.elastic.co/elasticsearch/elasticsearch:7.10.0`.
9. Verify that ES cluster is running: `http://localhost:9200`.

10.   Set application run configuration to `local` using the following flag: `spring.profiles.active=local`. 

**Note**: Always run `mvn spring-javaformat:apply` to format code across codebase and to keep all formatting consistent.

To run from the terminal:

```mvn spring-boot:run -Dspring-boot.run.profiles=local```