-- Spring Boot Basic Tag 9 - Test Daten

INSERT INTO persons (id, firstname, lastname, email, created_at) VALUES 
(1, 'Max', 'Mustermann', 'max.mustermann@example.com', CURRENT_TIMESTAMP),
(2, 'Erika', 'Musterfrau', 'erika.musterfrau@example.com', CURRENT_TIMESTAMP),
(3, 'Tom', 'Miller', 'tom.miller@example.com', CURRENT_TIMESTAMP),
(4, 'Anna', 'Schmidt', 'anna.schmidt@example.com', CURRENT_TIMESTAMP),
(5, 'Peter', 'Müller', 'peter.mueller@example.com', CURRENT_TIMESTAMP);

-- Sequenz für neue Einträge anpassen
ALTER TABLE persons ALTER COLUMN id RESTART WITH 6;
