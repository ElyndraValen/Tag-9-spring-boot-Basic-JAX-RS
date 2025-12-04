# Spring Boot Basic Tag 9 - Tutorial

## JAX-RS in Spring Boot - Schritt für Schritt

**Von Franz-Martin | Java-Developer.online**

---

## 🎯 Lernziele

Nach diesem Tutorial kannst du:
- ✅ JAX-RS in Spring Boot integrieren
- ✅ REST APIs mit Java Standards bauen
- ✅ @Path, @GET, @POST, @PUT, @DELETE nutzen
- ✅ @PathParam und @QueryParam verstehen
- ✅ Response-Objekte und HTTP-Status-Codes nutzen
- ✅ Exception Handling mit @Provider implementieren

---

## 🟢 GRUNDLAGEN (Level 1-3)

### Schritt 1: Was ist JAX-RS? (45 Min)

#### 1.1 JAX-RS verstehen

**JAX-RS** = Jakarta RESTful Web Services

**Die Analogie:**
```
JDBC = Standard für Datenbank-Zugriff
  → Implementierungen: MySQL Driver, PostgreSQL Driver

JAX-RS = Standard für REST APIs
  → Implementierungen: Jersey, RESTEasy, Apache CXF
```

**Warum JAX-RS wichtig ist:**

1. **Enterprise-Standard**
   - Banken, Versicherungen nutzen Java EE/Jakarta EE
   - JAX-RS ist DER Standard dort
   - Wenn du in diesen Firmen arbeitest: JAX-RS essentiell!

2. **Portabilität**
   - JAX-RS Code läuft auf: Tomcat, WildFly, GlassFish, Payara, Spring Boot
   - Spring MVC Code läuft auf: Spring Boot (nur!)

3. **Migration**
   - Firma hat Java EE Apps mit JAX-RS
   - Migration zu Spring Boot
   - Du musst JAX-RS verstehen!

#### 1.2 Code-Vergleich: Spring MVC vs JAX-RS

**Spring MVC:**
```java
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    
    @GetMapping
    public List<Person> getAll() {
        return personService.getAllPersons();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.ok(person);
    }
    
    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        Person created = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

**JAX-RS:**
```java
@Component
@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    
    @GET
    public List<Person> getAll() {
        return personService.getAllPersons();
    }
    
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Person person = personService.getPersonById(id);
        return Response.ok(person).build();
    }
    
    @POST
    public Response create(Person person) {
        Person created = personService.createPerson(person);
        return Response.status(Response.Status.CREATED)
            .entity(created)
            .build();
    }
}
```

**Unterschiede:**

| Feature | Spring MVC | JAX-RS |
|---------|-----------|---------|
| Class Annotation | `@RestController` | `@Path` |
| HTTP GET | `@GetMapping` | `@GET` |
| HTTP POST | `@PostMapping` | `@POST` |
| Path Variable | `@PathVariable` | `@PathParam` |
| Request Body | `@RequestBody` | Automatisch |
| Response | `ResponseEntity` | `Response` |

---

### Schritt 2: Jersey in Spring Boot integrieren (1 Stunde)

#### 2.1 Dependencies hinzufügen

In `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Jersey (JAX-RS Implementation) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jersey</artifactId>
    </dependency>
</dependencies>
```

**Wichtig:** `spring-boot-starter-jersey` enthält:
- Jersey Core
- Jersey Server
- JAX-RS API
- Integration mit Spring Boot

#### 2.2 Jersey Configuration erstellen

`config/JerseyConfig.java`:

```java
package com.javadeveloper.springboot.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import jakarta.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")  // Base-Path für alle JAX-RS Resources
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Resources werden hier registriert
        System.out.println("✅ Jersey Config initialisiert");
    }
}
```

**Was macht das:**
- `@Configuration` → Spring Component
- `@ApplicationPath("/api")` → Alle Endpoints unter `/api`
- `extends ResourceConfig` → Jersey Configuration

#### 2.3 application.properties konfigurieren

```properties
# Jersey Application Path
spring.jersey.application-path=/api

# Server Port
server.port=8080
```

---

### Schritt 3: Erste JAX-RS Resource erstellen (1.5 Stunden)

#### 3.1 Person Entity erstellen

`model/Person.java`:

```java
package com.javadeveloper.springboot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor
    public Person() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    // ... (siehe Projekt-Code)
}
```

#### 3.2 PersonService erstellen

`service/PersonService.java`:

```java
package com.javadeveloper.springboot.service;

import com.javadeveloper.springboot.model.Person;
import com.javadeveloper.springboot.repository.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public Person updatePerson(Long id, Person updatedPerson) {
        Person person = getPersonById(id);
        if (person != null) {
            person.setFirstname(updatedPerson.getFirstname());
            person.setLastname(updatedPerson.getLastname());
            person.setEmail(updatedPerson.getEmail());
            return personRepository.save(person);
        }
        return null;
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
}
```

#### 3.3 JAX-RS Resource erstellen

`resource/PersonResource.java`:

```java
package com.javadeveloper.springboot.resource;

import com.javadeveloper.springboot.model.Person;
import com.javadeveloper.springboot.service.PersonService;
import org.springframework.stereotype.Component;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Component  // ← WICHTIG: Spring Bean!
@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private final PersonService personService;

    // Constructor Injection funktioniert!
    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    // GET /api/persons
    @GET
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    // GET /api/persons/{id}
    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        Person person = personService.getPersonById(id);
        if (person == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(person).build();
    }

    // POST /api/persons
    @POST
    public Response createPerson(Person person) {
        Person created = personService.createPerson(person);
        return Response
            .status(Response.Status.CREATED)
            .entity(created)
            .build();
    }

    // PUT /api/persons/{id}
    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, Person person) {
        Person updated = personService.updatePerson(id, person);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    // DELETE /api/persons/{id}
    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.deletePerson(id);
        return Response.noContent().build();
    }
}
```

**WICHTIG:**
1. `@Component` → Spring verwaltet die Resource als Bean
2. `@Path("/persons")` → URL-Mapping
3. `@Produces` → Was wird zurückgegeben (JSON)
4. `@Consumes` → Was wird akzeptiert (JSON)

#### 3.4 Resource in JerseyConfig registrieren

```java
@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(PersonResource.class);  // ← Resource registrieren!
        System.out.println("✅ PersonResource registriert");
    }
}
```

#### 3.5 Testen!

**App starten:**
```bash
mvn spring-boot:run
```

**GET alle Personen:**
```bash
curl http://localhost:8080/api/persons
```

**GET eine Person:**
```bash
curl http://localhost:8080/api/persons/1
```

**POST neue Person:**
```bash
curl -X POST http://localhost:8080/api/persons \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "John",
    "lastname": "Doe",
    "email": "john.doe@example.com"
  }'
```

✅ **Grundlagen abgeschlossen!**

---

## 🟡 PROFESSIONAL (Level 4-5)

### Schritt 4: Parameter-Annotations (1.5 Stunden)

#### 4.1 @PathParam - URL-Parameter

**Syntax:**
```java
@GET
@Path("/{id}")
public Person getById(@PathParam("id") Long id) {
    return personService.getPersonById(id);
}
```

**URL:** `/api/persons/42` → `id = 42`

**Mehrere Parameter:**
```java
@GET
@Path("/{id}/address/{addressId}")
public Address getAddress(
    @PathParam("id") Long personId,
    @PathParam("addressId") Long addressId
) {
    // ...
}
```

**URL:** `/api/persons/1/address/5` → `personId = 1`, `addressId = 5`

#### 4.2 @QueryParam - Query-String-Parameter

**Syntax:**
```java
@GET
@Path("/search")
public Response search(
    @QueryParam("firstname") String firstname,
    @QueryParam("lastname") String lastname,
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("size") @DefaultValue("10") int size
) {
    List<Person> results = personService.search(firstname, lastname, page, size);
    return Response.ok(results).build();
}
```

**URLs:**
```
/api/persons/search?firstname=Max
/api/persons/search?firstname=Max&lastname=Mustermann
/api/persons/search?page=0&size=20
```

#### 4.3 @DefaultValue - Default-Werte

**Ohne Default:**
```java
@QueryParam("page") int page  // null wenn nicht angegeben!
```

**Mit Default:**
```java
@QueryParam("page") @DefaultValue("0") int page  // 0 wenn nicht angegeben
```

**Beispiel:**
```java
@GET
public List<Person> getAll(
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("size") @DefaultValue("10") int size
) {
    return personService.getAll(page, size);
}
```

**URLs:**
```
/api/persons              → page=0, size=10 (defaults)
/api/persons?page=2       → page=2, size=10
/api/persons?size=20      → page=0, size=20
```

---

### Schritt 5: Exception Handling (1.5 Stunden)

#### 5.1 Custom Exception erstellen

`exception/PersonNotFoundException.java`:

```java
package com.javadeveloper.springboot.exception;

public class PersonNotFoundException extends RuntimeException {

    private final Long personId;

    public PersonNotFoundException(Long id) {
        super("Person not found with id: " + id);
        this.personId = id;
    }

    public Long getPersonId() {
        return personId;
    }
}
```

#### 5.2 ErrorResponse DTO erstellen

`dto/ErrorResponse.java`:

```java
package com.javadeveloper.springboot.dto;

public class ErrorResponse {

    private int status;
    private String message;
    private long timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    // ...
}
```

#### 5.3 ExceptionMapper erstellen

`exception/PersonNotFoundExceptionMapper.java`:

```java
package com.javadeveloper.springboot.exception;

import com.javadeveloper.springboot.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider  // ← JAX-RS erkennt diese Klasse!
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

#### 5.4 ExceptionMapper registrieren

`config/JerseyConfig.java`:

```java
@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(PersonResource.class);
        register(PersonNotFoundExceptionMapper.class);  // ← Registrieren!
    }
}
```

#### 5.5 Exception in Service nutzen

`service/PersonService.java`:

```java
public Person getPersonById(Long id) {
    return personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));
}
```

#### 5.6 Testen!

**Request:**
```bash
curl http://localhost:8080/api/persons/999
```

**Response (404):**
```json
{
  "status": 404,
  "message": "Person not found with id: 999",
  "timestamp": 1697543210567
}
```

✅ **Professional Level abgeschlossen!**

---

## 🔵 ENTERPRISE FEATURES (Level 6)

### Schritt 6: Spring MVC vs JAX-RS Side-by-Side (2 Stunden)

#### 6.1 Beide Ansätze parallel nutzen

Du kannst beide Ansätze in einer App nutzen!

**JAX-RS Resource:**
```java
@Component
@Path("/jaxrs/persons")  // ← /api/jaxrs/persons
public class PersonJaxRsResource {
    // JAX-RS Code...
}
```

**Spring MVC Controller:**
```java
@RestController
@RequestMapping("/spring/persons")  // ← /spring/persons
public class PersonSpringController {
    // Spring MVC Code...
}
```

**Beide laufen parallel!** ✅

#### 6.2 Wann was nutzen?

**JAX-RS nutzen wenn:**
- ✅ Migration von Java EE zu Spring Boot
- ✅ Portabilität wichtig (mehrere Server-Typen)
- ✅ Enterprise-Standard gefordert
- ✅ Team kennt Java EE

**Spring MVC nutzen wenn:**
- ✅ Neue Spring Boot App
- ✅ Spring-Ökosystem voll nutzen
- ✅ Team kennt nur Spring
- ✅ Keine Migrations-Anforderung

#### 6.3 Migration-Pattern

**Schritt 1: Parallel betreiben**
```
/api/jaxrs/*     → JAX-RS (Legacy Code)
/api/spring/*    → Spring MVC (Neu)
```

**Schritt 2: Schrittweise migrieren**
- Endpoint für Endpoint von JAX-RS zu Spring MVC
- Tests laufen weiter
- Keine Big-Bang-Migration!

**Schritt 3: Legacy deprecaten**
- JAX-RS Endpoints als @Deprecated markieren
- Clients migrieren
- Alte Endpoints entfernen

✅ **Enterprise Features abgeschlossen!**

---

## 🎉 Geschafft!

Du hast jetzt:
- ✅ JAX-RS in Spring Boot integriert
- ✅ REST APIs mit Java Standards gebaut
- ✅ Alle wichtigen Annotations verstanden
- ✅ Exception Handling implementiert
- ✅ Migration-Pattern gelernt

---

## 📚 Nächste Schritte

1. **Übung:** Erstelle eine zweite Resource (z.B. `AddressResource`)
2. **Erweitern:** Füge Validierung hinzu (@Valid)
3. **Testen:** Schreibe Unit Tests für deine Resources
4. **Vergleichen:** Baue dieselbe API mit Spring MVC
5. **Tag 10:** Integration & Abschluss!

---

## 🔥 Keep Coding, Keep Learning!

**Happy Coding! 💙**

© 2025 Java-Developer.online | Franz-Martin
