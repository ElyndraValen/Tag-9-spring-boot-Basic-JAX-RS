# Spring Boot Datenbank-Initialisierung

## Der Mechanismus

### Auto-Configuration
Spring Boot bietet eine automatische Datenbank-Initialisierung über die **`DataSourceInitializationConfiguration`** Klasse.

**Ablauf:**
1. Spring Boot startet den ApplicationContext
2. **DataSource Bean** wird erstellt (z.B. H2, PostgreSQL, MySQL)
3. **`DataSourceScriptDatabaseInitializer`** Bean wird erstellt
4. In der `@PostConstruct` Phase werden SQL-Scripts automatisch ausgeführt
5. Anwendung ist bereit für Requests

### Zusammenspiel mit Hibernate/JPA

**Problem:** Standardmäßig wird `data.sql` **VOR** Hibernate ausgeführt!

```
ApplicationContext Start
    ↓
DataSource erstellt
    ↓
data.sql wird ausgeführt  ← FEHLER: Tabelle existiert noch nicht!
    ↓
Hibernate erstellt Tabellen (ddl-auto=update)
    ↓
Anwendung bereit
```

**Lösung:** `spring.jpa.defer-datasource-initialization=true`

```
ApplicationContext Start
    ↓
DataSource erstellt
    ↓
Hibernate erstellt Tabellen (ddl-auto=update)
    ↓
data.sql wird ausgeführt  ← JETZT funktioniert es!
    ↓
Anwendung bereit
```

## Standard-Dateien

Spring Boot sucht automatisch nach folgenden Dateien im **Classpath** (`src/main/resources/`):

| Datei | Zweck | Wann ausgeführt |
|-------|-------|-----------------|
| `schema.sql` | DDL: CREATE TABLE, ALTER TABLE, etc. | Vor data.sql |
| `data.sql` | DML: INSERT, UPDATE, DELETE | Nach schema.sql |

**Wichtig:** Diese Dateien werden **nur gefunden**, wenn sie direkt unter `src/main/resources/` liegen (nicht in Unterordnern), ODER wenn sie explizit konfiguriert sind.

## Konfiguration

### Eigene Dateinamen und Pfade

```properties
# Eigene Schema-Datei(en)
spring.sql.init.schema-locations=classpath:db/mein-schema.sql,classpath:db/tables.sql

# Eigene Daten-Datei(en) - mehrere möglich!
spring.sql.init.data-locations=classpath:db/mein-daten.sql,classpath:db/testdata.sql

# Oder externe Datei (außerhalb des JARs)
spring.sql.init.data-locations=file:/opt/app/init-data.sql
```

### Plattform-spezifische Scripts

Sehr nützlich, wenn die Anwendung mit verschiedenen Datenbanken laufen soll:

```properties
# Plattform setzen
spring.sql.init.platform=h2
```

**Spring Boot sucht dann nach:**
- `schema-h2.sql`
- `data-h2.sql`

**Beispiel-Struktur:**
```
src/main/resources/
├── application.properties
├── data-h2.sql          # Für H2 (CURRENT_TIMESTAMP, etc.)
├── data-postgres.sql    # Für PostgreSQL (NOW(), etc.)
└── data-mysql.sql       # Für MySQL (spezifische Syntax)
```

**In application.properties:**
```properties
# Für lokale Entwicklung mit H2
spring.sql.init.platform=h2

# Für Produktion mit PostgreSQL (z.B. über Profil)
spring.sql.init.platform=postgres
```

### Initialisierungs-Modus

```properties
# Wann sollen SQL-Scripts ausgeführt werden?

spring.sql.init.mode=always      # Immer (auch bei externen DBs)
spring.sql.init.mode=embedded    # Nur bei embedded DBs (H2, HSQL, Derby) - DEFAULT
spring.sql.init.mode=never       # Nie ausführen (deaktiviert)
```

**Standard:** `embedded` - Scripts werden nur bei In-Memory-DBs ausgeführt

### Weitere Optionen

```properties
# Bei SQL-Fehlern trotzdem weitermachen
spring.sql.init.continue-on-error=true

# Encoding der SQL-Dateien
spring.sql.init.encoding=UTF-8

# Separator für SQL-Statements (Standard: ;)
spring.sql.init.separator=;

# SQL-Script erst nach Hibernate-Initialisierung
spring.jpa.defer-datasource-initialization=true
```

## Best Practices

### 1. Für Entwicklung (In-Memory DB)

```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=embedded

# data.sql wird automatisch gefunden und ausgeführt
```

**Ablauf:**
- Bei jedem Start wird die DB neu erstellt (create-drop)
- Hibernate erstellt Tabellen aus Entities
- `data.sql` fügt Testdaten ein
- Bei Stop wird DB gelöscht

### 2. Für Produktion (Persistente DB)

```properties
# application-prod.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=never

# Verwende stattdessen: Flyway oder Liquibase!
```

**Warum `never`?**
- `data.sql` würde bei jedem Start versuchen, Daten einzufügen
- → Duplicate Key Errors
- Flyway/Liquibase haben Versions-Tracking und führen Migrations nur einmal aus

### 3. Mit Flyway/Liquibase (Empfohlen für Produktion)

**Flyway:**
```properties
spring.flyway.enabled=true
spring.sql.init.mode=never  # SQL-Init deaktivieren
```

```
src/main/resources/db/migration/
├── V1__create_persons_table.sql
├── V2__add_email_column.sql
└── V3__insert_initial_data.sql
```

**Liquibase:**
```properties
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml
spring.sql.init.mode=never
```

## Häufige Probleme und Lösungen

### Problem 1: "Table 'persons' doesn't exist"

**Ursache:** `data.sql` wird vor Hibernate ausgeführt

**Lösung:**
```properties
spring.jpa.defer-datasource-initialization=true
```

### Problem 2: "Duplicate entry for key 'PRIMARY'"

**Ursache:** `data.sql` wird bei jedem Start ausgeführt, Daten existieren bereits

**Lösungen:**
- **Option A:** In-Memory DB verwenden (`jdbc:h2:mem:`)
- **Option B:** `spring.sql.init.mode=never` setzen
- **Option C:** Zu Flyway/Liquibase migrieren

### Problem 3: SQL-Script wird nicht gefunden

**Prüfen:**
1. Liegt die Datei unter `src/main/resources/`?
2. Heißt sie exakt `data.sql` (case-sensitive)?
3. Ist `spring.sql.init.mode=never` gesetzt?
4. Bei Custom-Namen: Ist `spring.sql.init.data-locations` konfiguriert?

### Problem 4: Syntax-Fehler bei verschiedenen Datenbanken

**Lösung:** Plattform-spezifische Scripts verwenden

```properties
spring.sql.init.platform=h2  # oder postgres, mysql, etc.
```

**Beispiel:**
```sql
-- data-h2.sql
INSERT INTO persons (created_at) VALUES (CURRENT_TIMESTAMP);

-- data-postgres.sql
INSERT INTO persons (created_at) VALUES (NOW());
```

## Alternativen zur SQL-Script-Initialisierung

### 1. CommandLineRunner

```java
@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(String... args) throws Exception {
        // Nur seeden, wenn DB leer ist
        if (personRepository.count() == 0) {
            personRepository.save(new Person("Max", "Mustermann"));
            personRepository.save(new Person("Erika", "Musterfrau"));
        }
    }
}
```

**Vorteile:**
- Programmatische Logik möglich (if/else, Schleifen, etc.)
- Kann prüfen, ob Daten bereits existieren
- Typsicher durch Entities

### 2. @PostConstruct in einem Service

```java
@Service
public class DataInitService {

    @Autowired
    private PersonRepository personRepository;

    @PostConstruct
    public void init() {
        if (personRepository.count() == 0) {
            // Daten initialisieren
        }
    }
}
```

### 3. Flyway/Liquibase (Empfohlen für Produktion)

**Dependency:**
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

**Migrations mit Versionierung:**
```
V1__create_schema.sql
V2__insert_initial_data.sql
V3__add_new_column.sql
```

**Vorteile:**
- Versions-Tracking (wird nur einmal ausgeführt)
- Rollback-Support
- Team-fähig (keine Merge-Konflikte bei Migrations)
- Audit-Trail (wer hat wann welche Migration ausgeführt)

## Zusammenfassung

| Aspekt | Details |
|--------|---------|
| **Mechanismus** | `DataSourceScriptDatabaseInitializer` Bean |
| **Wann** | ApplicationContext-Start, nach DataSource-Erstellung |
| **Standard-Dateien** | `schema.sql`, `data.sql` |
| **Pfad** | `src/main/resources/` (Classpath-Root) |
| **Konfigurierbar** | Ja, über `spring.sql.init.*` Properties |
| **Best Practice** | In-Memory: SQL-Scripts / Produktion: Flyway/Liquibase |

---

**Erstellt für:** Spring Boot Basic - Tag 9
**Datum:** 2025-11-19
