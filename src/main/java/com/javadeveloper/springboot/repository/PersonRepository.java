package com.javadeveloper.springboot.repository;

import com.javadeveloper.springboot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Person Repository
 * Spring Data JPA Repository für Person Entity
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Suche nach Personen mit Firstname oder Lastname
     */
    @Query("SELECT p FROM Person p WHERE " +
           "(:firstname IS NULL OR LOWER(p.firstname) LIKE LOWER(CONCAT('%', :firstname, '%'))) AND " +
           "(:lastname IS NULL OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', :lastname, '%')))")
    List<Person> searchByName(
        @Param("firstname") String firstname, 
        @Param("lastname") String lastname
    );
    
    /**
     * Finde Person by Email
     */
    Person findByEmail(String email);
}
