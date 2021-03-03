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
	
	@POST 		
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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
			//O formato XML presenta problemas á devolución nun Response de listas de valores (arrayList)
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
			if(personas.get(i).getNombre().equals(nombre)) {
				personasPatron.add(personas.get(i));
			}
		}		
		if(personasPatron.size() > 0) {
			return Response.ok(personasPatron).build();	//devolvemos lista en json, non hai que convertila en generic
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}		
	}
	
	
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	@Path("buscar/{cadena}")
//	public ArrayList<Persona> ver2(@PathParam("cadena") String nombre){
//		//System.out.println(nombre+ "1");
//		ArrayList<Persona> personasPatron = new ArrayList<Persona>();
//		for(int i = 0; i < personas.size(); i++) {
//			if(personas.get(i).nombre.toUpperCase().contains(nombre.toUpperCase())) {
//				personasPatron.add(personas.get(i));
//			}
//		}		
//		return personasPatron;		
//	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscar")
	public Response ver2(@DefaultValue("aa") @QueryParam("nombre") String nombre){	//= método (GET) conflicto de nomes de funct
		ArrayList<Persona> personasPatron = new ArrayList<Persona>();
		for(int i = 0; i < personas.size(); i++) {
			if(personas.get(i).getNombre().toLowerCase().contains(nombre.toLowerCase())) {
				personasPatron.add(personas.get(i));
			}
		}		
		if(personasPatron.size() > 0) {
			return Response.ok(personasPatron).build();	
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}		
	}
	
	//CASE SENSITIVE: Query string Value --  buscar?nombre=aA !=  buscar?nombre=aa
	//CASE INSENSIIVE: Query string KEY -- buscar?nomBre=aa == buscar?nomBre=aa
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarForm(@FormParam("id") int id, @FormParam("nombre") String nombre,
			@FormParam("casado") boolean casado, @FormParam("sexo") String sexo) {
		personas.add(new Persona(id, nombre, casado, sexo));
		return Response.status(Status.ACCEPTED).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")	
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
			if(personas.get(i).getId() == id) {
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
			if(personas.get(i).getId() == id) {
				return Response.ok(personas.get(i)).build();
			}
		}
		return Response.status(Status.NOT_FOUND).build();
	}	
	//Se a representación devolta é JSON @XmlAttribute é ignorada. 
	

	@GET	
	@Path("/galego")	
	public ArrayList<Persona> atributosGalego() {	 
		//@XmlElement(name="nome") en getNombre() clase Persona
		return personas;
	}			
	//https://howtodoinjava.com/jaxb/jaxb-annotations/		
}