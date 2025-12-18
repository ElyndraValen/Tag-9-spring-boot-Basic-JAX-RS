/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.javadeveloper.springboot.model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *
 * @author Trainer
 * Diese Wrapperklasse ist ein muss für die XML Ausgabe 
 * ein Root Elemnt wird bei XML immer benötigt!
 * Im Gegensatz zu Json
 */
@XmlRootElement(name = "persons")
public class PersonList {

    private List<Person> persons;

    public PersonList() {}

    public PersonList(List<Person> persons) {
        this.persons = persons;
    }

    @XmlElement(name = "person")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}

