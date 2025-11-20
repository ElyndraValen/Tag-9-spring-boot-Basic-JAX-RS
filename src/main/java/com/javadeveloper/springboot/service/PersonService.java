package com.javadeveloper.springboot.service;

import com.javadeveloper.springboot.exception.PersonNotFoundException;
import com.javadeveloper.springboot.model.Person;
import com.javadeveloper.springboot.repository.PersonRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Person Service
 * Business Logic für Person-Verwaltung
 */
@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Alle Personen abrufen
     */
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    /**
     * Alle Personen mit Pagination
     */
    public List<Person> getAllPersons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return personRepository.findAll(pageable).getContent();
    }

    /**
     * Person nach ID abrufen
     */
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    /**
     * Neue Person erstellen
     */
    public Person createPerson(Person person) {
        Person p=personRepository.save(person);
        return p;
    }

    /**
     * Person aktualisieren
     */
    public Person updatePerson(Long id, Person updatedPerson) {
        Person person = getPersonById(id);
        
        person.setFirstname(updatedPerson.getFirstname());
        person.setLastname(updatedPerson.getLastname());
        person.setEmail(updatedPerson.getEmail());
        
        return personRepository.save(person);
    }

    /**
     * Person löschen
     */
    public void deletePerson(Long id) {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }

    /**
     * Suche nach Personen
     */
    public List<Person> search(String firstname, String lastname, int page, int size) {
        if (firstname == null && lastname == null) {
            return getAllPersons(page, size);
        }
        return personRepository.searchByName(firstname, lastname);
    }
}
