package com.javadeveloper.springboot.resource;

import com.javadeveloper.springboot.model.Person;
import com.javadeveloper.springboot.service.PersonService;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Person JAX-RS Resource
 * 
 * Demonstriert alle wichtigen JAX-RS Annotations:
 * - @Path, @GET, @POST, @PUT, @DELETE
 * - @Produces, @Consumes
 * - @PathParam, @QueryParam, @DefaultValue
 * - Response-Objekte mit HTTP-Status
 * 
 * @Component - macht diese Resource zu einer Spring Bean!
 */
@Component
@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private final PersonService personService;

    // Constructor Injection funktioniert!
    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    /**
     * GET /api/persons
     * Alle Personen abrufen
     */
    @GET
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    /**
     * GET /api/persons/{id}
     * Eine Person nach ID abrufen
     * 
     * Demonstriert:
     * - @PathParam für URL-Parameter
     * - Response.Status für HTTP-Status-Codes
     * - PersonNotFoundException wird automatisch zu 404
     */
    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        Person person = personService.getPersonById(id);
        return Response.ok(person).build();
    }

    /**
     * GET /api/persons/search
     * Suche nach Personen
     * 
     * Demonstriert:
     * - @QueryParam für Query-String-Parameter
     * - @DefaultValue für Default-Werte
     * 
     * Beispiel: /api/persons/search?firstname=Max&page=0&size=20
     */
    @GET
    @Path("/search")
    public Response search(
            @QueryParam("firstname") String firstname,
            @QueryParam("lastname") String lastname,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        
        List<Person> results = personService.search(firstname, lastname, page, size);
        return Response.ok(results).build();
    }

    /**
     * POST /api/persons
     * Neue Person erstellen
     * 
     * Demonstriert:
     * - @POST für HTTP POST
     * - Automatisches Request-Body Binding
     * - Response.Status.CREATED (201)
     */
    @POST
    public Response createPerson(Person person) {
        Person created = personService.createPerson(person);
        return Response
                .status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    /**
     * PUT /api/persons/{id}
     * Person aktualisieren
     * 
     * Demonstriert:
     * - @PUT für HTTP PUT
     * - Kombination @PathParam + Request Body
     */
    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, Person person) {
        Person updated = personService.updatePerson(id, person);
        return Response.ok(updated).build();
    }

    /**
     * DELETE /api/persons/{id}
     * Person löschen
     * 
     * Demonstriert:
     * - @DELETE für HTTP DELETE
     * - Response.Status.NO_CONTENT (204)
     */
    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.deletePerson(id);
        return Response.noContent().build();
    }

    /**
     * GET /api/persons/flexible
     * Demonstriert Content Negotiation
     * 
     * Client kann via Accept-Header wählen:
     * - Accept: application/json → JSON
     * - Accept: application/xml  → XML (wenn JAXB konfiguriert)
     */
    @GET
    @Path("/flexible")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Person> getAllFlexible() {
        return personService.getAllPersons();
    }
}
