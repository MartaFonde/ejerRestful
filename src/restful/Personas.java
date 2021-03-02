package restful;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;


@Path("/personas")
public class Personas {
	static ArrayList<Persona> personas = new ArrayList<Persona>();
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response guardar(Persona p) {		
		personas.add(p);
		return Response.ok(p).build();	//mostra o obxecto
		//return Response.status(Status.ACCEPTED).build();
	}
	
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public ArrayList<Persona> listar(){
//		return personas; --	devolver mediante código
//	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response listar(){
		if(personas.size() > 0) {
			List<Persona> list = personas;
			GenericEntity<List<Persona>> entity = new GenericEntity<List<Persona>>(list) {};
			return Response.ok(entity).build();
		}else {
			return Response.status(Status.NO_CONTENT).build();
		}		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	@Path("/{nombre}")
	public Response ver(@PathParam("nombre") String nombre) {
		ArrayList<Persona> personasPatron = new ArrayList<Persona>();
		for(int i = 0; i < personas.size(); i++) {
			if(personas.get(i).nombre.equals(nombre)) {
				personasPatron.add(personas.get(i));
			}
		}
		
		if(personasPatron.size() > 0) {
			return Response.ok(personasPatron).build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscar")
	public Response ver2(@DefaultValue("Carme") @QueryParam("nombre") String nombre){
		ArrayList<Persona> personasPatron = new ArrayList<Persona>();
		for(int i = 0; i < personas.size(); i++) {
			if(personas.get(i).nombre.toUpperCase().contains(nombre.toUpperCase())) {
				personasPatron.add(personas.get(i));
			}
		}		
		if(personasPatron.size() > 0) {
			return Response.ok(personasPatron).build();	//devolver unha lista con json non dá fallo
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}		
	}
	
	@PUT
	@Consumes("application/x-www-form-urlencoded")
	public Response insertarForm(@FormParam("id") int id, @FormParam("nombre") String nombre,
			@FormParam("casado") boolean casado, @FormParam("sexo") String sexo) {
		personas.add(new Persona(id, nombre, casado, sexo));
		return Response.status(Status.ACCEPTED).build();
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("/add")	//se non lle poño path crease ambigüidade co método guardar --error 500
	public Response add(Persona[] pers) {
		if(pers.length > 0) {
			for(int i=0; i < pers.length; i++) {
				personas.add(pers[i]);
			}
			return Response.status(Status.ACCEPTED).build();
		}else {
			return Response.status(Status.BAD_REQUEST).build();
		}		
	}
				
	@DELETE	
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")	
	public Response borrar(@PathParam("id") int id) {		
		for(int i = 0; i < personas.size(); i++) {
			if(personas.get(i).id == id) {
				return Response.ok(personas.remove(i)).build(); //mostra que persona elimina
			}
		}
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("/XML/{id}")
	public Response mostrarPersona(@PathParam("id") int id) {
		for(int i = 0; i<personas.size(); i++) {
			if(personas.get(i).id == id) {
				return Response.ok(personas.get(i)).build();
			}
		}
		return Response.status(Status.NOT_FOUND).build();
	}
	

	@GET	
	@Path("/galego")	//falta
	public ArrayList<Persona> atributosGalego() {		
		return personas;
	}			
		
}