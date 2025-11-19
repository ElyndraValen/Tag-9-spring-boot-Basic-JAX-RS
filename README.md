# Spring Boot Basic Tag 9

## JAX-RS in Spring Boot - REST mit Java Standards

**Von Franz-Martin | Java-Developer.online**

---

## 📋 Übersicht

Dieses Projekt demonstriert die Integration von **JAX-RS** (Jakarta RESTful Web Services) in Spring Boot. 
Du lernst, wie du REST APIs mit **Java Standards** statt Spring-spezifischen Annotations baust.

### Was ist JAX-RS?

**JAX-RS** = Jakarta RESTful Web Services (früher: Java API for RESTful Web Services)

- ✅ **Java Standard** für REST APIs
- ✅ Teil von **Jakarta EE** (früher Java EE)
- ✅ **Vendor-neutral** – nicht an Spring gebunden
- ✅ **Portabel** – läuft auf Tomcat, WildFly, GlassFish, Payara, etc.

### Spring MVC vs JAX-RS

| Feature | Spring MVC | JAX-RS |
|---------|-----------|---------|
| Standard | Spring-spezifisch | Java/Jakarta Standard |
| Class Annotation | `@RestController` | `@Path` |
| HTTP Methods | `@GetMapping`, `@PostMapping` | `@GET`, `@POST` |
| Path Variable | `@PathVariable` | `@PathParam` |
| Request Body | `@RequestBody` | Automatisch |
| Portabilität | Nur Spring Boot | Alle Jakarta EE Server |

---

## 🚀 Quick Start

### 1. Projekt klonen/herunterladen

```bash
cd spring-boot-basic-tag9
```

### 2. Maven Build

```bash
mvn clean install
```

### 3. Anwendung starten

```bash
mvn spring-boot:run
```

### 4. Im Browser öffnen

```
http://localhost:8080/index.html
```

---

## 📍 Verfügbare Endpoints

### JAX-RS REST API

Alle JAX-RS Endpoints sind unter `/api` verfügbar:

#### GET - Alle Personen abrufen
```bash
curl http://localhost:8080/api/persons
```

#### GET - Person nach ID
```bash
curl http://localhost:8080/api/persons/1
```

#### GET - Suche nach Personen
```bash
# Nach Vorname suchen
curl "http://localhost:8080/api/persons/search?firstname=Max"

# Nach Vor- und Nachname suchen
curl "http://localhost:8080/api/persons/search?firstname=Max&lastname=Mustermann"

# Mit Pagination
curl "http://localhost:8080/api/persons/search?page=0&size=10"
```

#### POST - Neue Person erstellen
```bash
curl -X POST http://localhost:8080/api/persons \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "John",
    "lastname": "Doe",
    "email": "john.doe@example.com"
  }'
```

#### PUT - Person aktualisieren
```bash
curl -X PUT http://localhost:8080/api/persons/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "Updated",
    "lastname": "Name",
    "email": "updated@example.com"
  }'
```

#### DELETE - Person löschen
```bash
curl -X DELETE http://localhost:8080/api/persons/5
```

---

## 🎯 Was du hier lernst

### Grundlagen (🟢 Level 1-3)

1. **JAX-RS verstehen**
   - Was ist JAX-RS?
   - Unterschied zu Spring MVC
   - Wann welchen Ansatz nutzen?

2. **Jersey Integration**
   - Dependencies einrichten
   - `JerseyConfig` erstellen
   - Resources registrieren

3. **JAX-RS Annotations**
   - `@Path` - URL-Mapping
   - `@GET`, `@POST`, `@PUT`, `@DELETE` - HTTP-Methoden
   - `@Produces`, `@Consumes` - Content Types

### Professional (🟡 Level 4-5)

4. **Parameter Handling**
   - `@PathParam` - URL-Parameter
   - `@QueryParam` - Query-String-Parameter
   - `@DefaultValue` - Default-Werte

5. **Response Management**
   - `Response` Objekte
   - HTTP-Status-Codes (200, 201, 404, etc.)
   - Strukturierte Error-Responses

6. **Exception Handling**
   - `@Provider` - Exception Mapper
   - Custom Exceptions
   - Globales Error Handling

### Enterprise (🔵 Level 6)

7. **Migration & Best Practices**
   - Spring MVC vs JAX-RS Side-by-Side
   - Beide Ansätze parallel nutzen
   - Migration von Java EE zu Spring Boot

---

## 📦 Projektstruktur

```
spring-boot-basic-tag9/
├── src/
│   ├── main/
│   │   ├── java/com/javadeveloper/springboot/
│   │   │   ├── SpringBootBasicTag9Application.java  # Main Application
│   │   │   ├── config/
│   │   │   │   └── JerseyConfig.java                # JAX-RS Configuration
│   │   │   ├── model/
│   │   │   │   └── Person.java                      # Entity Model
│   │   │   ├── repository/
│   │   │   │   └── PersonRepository.java            # JPA Repository
│   │   │   ├── service/
│   │   │   │   └── PersonService.java               # Business Logic
│   │   │   ├── resource/
│   │   │   │   └── PersonResource.java              # JAX-RS Resource ⭐
│   │   │   ├── exception/
│   │   │   │   ├── PersonNotFoundException.java
│   │   │   │   └── PersonNotFoundExceptionMapper.java
│   │   │   └── dto/
│   │   │       └── ErrorResponse.java
│   │   └── resources/
│   │       ├── application.properties               # Configuration
│   │       ├── data.sql                             # Test-Daten
│   │       └── static/
│   │           ├── index.html                       # Test-Seite 🎨
│   │           └── css/
│   │               └── style.css                    # java-developer.online Style
├── pom.xml                                          # Maven Dependencies
└── README.md
```

---

## 💻 Code-Highlights

### PersonResource.java - JAX-RS Resource

```java
@Component  // Spring Bean!
@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private final PersonService personService;

    // Constructor Injection funktioniert!
    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        Person person = personService.getPersonById(id);
        return Response.ok(person).build();
    }

    @POST
    public Response createPerson(Person person) {
        Person created = personService.createPerson(person);
        return Response
            .status(Response.Status.CREATED)
            .entity(created)
            .build();
    }

    @PUT
    @Path("/{id}")
    public Person updatePerson(@PathParam("id") Long id, Person person) {
        return personService.updatePerson(id, person);
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.deletePerson(id);
        return Response.noContent().build();
    }
}
```

### JerseyConfig.java - Configuration

```java
@Configuration
@ApplicationPath("/api")  // Base-Path für alle JAX-RS Resources
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Resources registrieren
        register(PersonResource.class);
        
        // Exception Mapper registrieren
        register(PersonNotFoundExceptionMapper.class);
    }
}
```

### PersonNotFoundExceptionMapper.java - Exception Handling

```java
@Provider  // JAX-RS erkennt diese Klasse als Exception Handler
public class PersonNotFoundExceptionMapper 
    implements ExceptionMapper<PersonNotFoundException> {

    @Override
    public Response toResponse(PersonNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
            Response.Status.NOT_FOUND.getStatusCode(),
            exception.getMessage(),
            System.currentTimeMillis()
        );

        return Response
            .status(Response.Status.NOT_FOUND)
            .entity(error)
            .build();
    }
}
```

---

## 🛠️ Technologien

- **Spring Boot 3.2.0**
- **Jersey (JAX-RS Implementation)**
- **Spring Data JPA**
- **H2 Database** (In-Memory)
- **Java 21**
- **Maven**

### Dependencies

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Jersey (JAX-RS) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jersey</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

---

## 🔍 Wichtige Konzepte

### 1. @Component auf JAX-RS Resource

**WICHTIG:** Die Resource muss `@Component` haben, damit Spring sie als Bean verwaltet!

```java
@Component  // ← Ohne das funktioniert DI nicht!
@Path("/persons")
public class PersonResource {
    // Constructor Injection funktioniert jetzt!
}
```

### 2. Response-Objekte vs. Direct Return

**Option 1: Direct Return (einfacher)**
```java
@GET
public List<Person> getAll() {
    return personService.getAllPersons();
}
```

**Option 2: Response-Objekt (mehr Kontrolle)**
```java
@GET
public Response getAll() {
    List<Person> persons = personService.getAllPersons();
    return Response.ok(persons).build();
}
```

### 3. HTTP-Status-Codes

```java
Response.Status.OK                  // 200
Response.Status.CREATED             // 201
Response.Status.NO_CONTENT          // 204
Response.Status.BAD_REQUEST         // 400
Response.Status.NOT_FOUND           // 404
Response.Status.INTERNAL_SERVER_ERROR // 500
```

### 4. Content Negotiation

```java
@GET
@Path("/flexible")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public List<Person> getAllFlexible() {
    // Client entscheidet via Accept-Header!
    return personService.getAllPersons();
}
```

Client-Request:
```bash
# JSON anfordern
curl -H "Accept: application/json" http://localhost:8080/api/persons/flexible

# XML anfordern (wenn konfiguriert)
curl -H "Accept: application/xml" http://localhost:8080/api/persons/flexible
```

---

## 🧪 Testing

### Mit cURL

Siehe [Verfügbare Endpoints](#-verfügbare-endpoints) oben.

### Mit Postman/Insomnia

1. Import der Collection (optional)
2. Base URL: `http://localhost:8080/api`
3. Teste alle Endpoints

### Mit der Test-Seite

```
http://localhost:8080/index.html
```

Die Test-Seite bietet eine interaktive UI zum Testen aller Endpoints!

---

## 📊 H2 Console

Zugriff auf die H2 Database Console:

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:tag9db
Username: sa
Password: (leer)
```

---

## 🔥 Best Practices

### 1. Exception Handling

**Immer** Custom Exceptions mit ExceptionMapper nutzen:

```java
// Exception werfen
if (person == null) {
    throw new PersonNotFoundException(id);
}

// ExceptionMapper wandelt automatisch in 404 Response um
```

### 2. Response-Objekte

Nutze `Response` für bessere Kontrolle:

```java
return Response
    .status(Response.Status.CREATED)
    .entity(created)
    .build();
```

### 3. @Component nicht vergessen!

```java
@Component  // ← WICHTIG für Spring DI!
@Path("/persons")
public class PersonResource { }
```

### 4. Resources registrieren

```java
public JerseyConfig() {
    register(PersonResource.class);  // ← Nicht vergessen!
}
```

---

## 🆘 Troubleshooting

### Problem: Jersey startet nicht

**Lösung:** Prüfe `@Configuration` und `@ApplicationPath` in `JerseyConfig`:

```java
@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig { }
```

### Problem: Resource nicht gefunden

**Lösung:** Resource muss registriert UND `@Component` haben:

```java
// 1. Registrieren
public JerseyConfig() {
    register(PersonResource.class);
}

// 2. @Component auf Resource
@Component
@Path("/persons")
public class PersonResource { }
```

### Problem: 404 bei allen Endpoints

**Lösung:** Prüfe `application.properties`:

```properties
spring.jersey.application-path=/api
```

### Problem: Constructor Injection funktioniert nicht

**Lösung:** `@Component` auf Resource setzen!

```java
@Component  // ← Das fehlt!
@Path("/persons")
public class PersonResource { }
```

---

## ❓ FAQ

**Q: Wann JAX-RS, wann Spring MVC?**

**A:** 
- **JAX-RS** für: Portabilität, Enterprise-Standard, Migration von Java EE
- **Spring MVC** für: Neue Spring Boot Apps, Spring-Ökosystem nutzen

**Q: Kann ich beide in einer App nutzen?**

**A:** Ja! `/api/*` mit JAX-RS, `/spring/*` mit Spring MVC. Beide parallel möglich!

**Q: Funktioniert Spring DI in JAX-RS?**

**A:** Ja, wenn die Resource `@Component` ist! Constructor Injection funktioniert.

**Q: Was ist besser?**

**A:** Keins ist "besser" – beide haben Use Cases. JAX-RS = Standard & portabel, Spring MVC = Spring-integriert.

---

## 📚 Weiterführende Links

- [JAX-RS Spec](https://jakarta.ee/specifications/restful-ws/)
- [Jersey Documentation](https://eclipse-ee4j.github.io/jersey/)
- [Spring Boot Jersey Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.jersey)
- [Java-Developer.online Blog](https://www.java-developer.online)

---

## 📅 Spring Boot Basic Kurs

Dies ist **Tag 9 von 10** im Spring Boot Basic Kurs:

| Tag | Thema | Status |
|-----|-------|--------|
| 1 | Spring Boot Intro | ✅ |
| 2 | Spring Container & DI | ✅ |
| 3 | @Controller & Thymeleaf | ✅ |
| 4 | Thymeleaf Forms & MVC | ✅ |
| 5 | Konfiguration & Logging | ✅ |
| 6 | DI & AOP im Detail | ✅ |
| 7 | Scopes in Spring | ✅ |
| 8 | WebSockets | ✅ |
| **9** | **JAX-RS in Spring Boot** | **✅ DU BIST HIER!** |
| 10 | Integration & Abschluss | → Morgen! |

---

## 🎉 Nächste Schritte

1. ✅ Projekt starten und testen
2. ✅ Alle Endpoints durchprobieren
3. ✅ Code verstehen und anpassen
4. ✅ Eigene Resource erstellen
5. → **Tag 10**: Integration & Abschluss!

---

## 📝 Lizenz

Dieses Projekt ist für Lernzwecke erstellt.

© 2025 Java-Developer.online | Franz-Martin

---

## 🔥 Keep Coding, Keep Learning!

**Happy Coding! 💙**
