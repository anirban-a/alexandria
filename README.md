# Alexandria
<hr>

## Setup
1. Install IntelliJ CE.
2. Install Oracle OpenJDK 18
3. Set Language level 17 in Project settings
4. Install `Maven`:
   1. `brew install maven` for MacOS.
   2. Maven [for windows](https://mkyong.com/maven/how-to-install-maven-in-windows/)
 
5. Build the project: `mvn clean install`

Note: Always run `mvn spring-javaformat:apply` to format code across codebase and to keep all formatting consistent.