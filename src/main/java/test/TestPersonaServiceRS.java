package test;

import domain.Persona;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class TestPersonaServiceRS {
    
    private static final String URL_BASE = "http://localhost:8080/sga-jee-web/webservice";
    private static Client cliente;
    private static WebTarget webTarget;
    private static Persona persona;
    private static List<Persona> personas;
    private static Invocation.Builder invocationBuilder;
    private static Response response;
    
    public static void main(String[] args){
        cliente = ClientBuilder.newClient();
        
        //leer una persona (metodo GET)
        webTarget = cliente.target(URL_BASE).path("/personas");
        //proporcionamos un idPersona valido
        persona = webTarget.path("/4").request(MediaType.APPLICATION_XML)
                .get(Persona.class);
        System.out.println("Persona recuperada: "+persona);
        
        //leer todas las personas 
        personas = webTarget.request(MediaType.APPLICATION_XML)
                .get(Response.class).readEntity(new GenericType<List<Persona>>(){});
        for(Persona personaP: personas){
            System.out.println("Persona: "+personaP);
        }
        
        //agregar una persona
        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombre("Aquiles");
        nuevaPersona.setApellido("Baeza");
        nuevaPersona.setEmail("aquilesbaeza@email.com");
        nuevaPersona.setTelefono("77777777");
        
        invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        response = invocationBuilder.post(Entity.entity(nuevaPersona, MediaType.APPLICATION_XML));
        System.out.println("");
        System.out.println(response.getStatus());
        //Recuperamos la persona recien agregada para despues modificarla y eliminarla
        Persona personaRecuperada = response.readEntity(Persona.class);
        System.out.println("Persona agregada: "+ personaRecuperada);
        
        //modificar la persona (metodo PUT)
        Persona personaModificada = personaRecuperada;
        personaModificada.setApellido("Brinco");
        String pathId = "/" + personaModificada.getIdPersona();
        invocationBuilder = webTarget.path(pathId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.put(Entity.entity(personaModificada, MediaType.APPLICATION_XML));
        System.out.println("");
        System.out.println("response: "+response.getStatus());
        System.out.println("Persona modificada: "+response.readEntity(Persona.class));
        
        //eliminar una persona
        //persona recuperada anteriormente
        Persona personaEliminar = personaRecuperada;
        String pathEliminarId = "/" + personaEliminar.getIdPersona();
        invocationBuilder = webTarget.path(pathEliminarId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.delete();
        System.out.println("");
        System.out.println("response:" + response.getStatus());
        System.out.println("Persona Eliminada" + personaEliminar);
    }
    
    
}
