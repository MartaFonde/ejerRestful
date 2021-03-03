package restful;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/deportistas")
public class Deportistas {	
	String bd = "ad_tema6";
	String driver = "org.mariadb.jdbc.Driver";
	String url = "jdbc:mariadb://localhost:3306/"+bd;
	String username = "root";
	String password = "";
	
	ArrayList<Deportista> deportistas = new ArrayList<>();;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/")
	public Response todos() {	
		return getDeportistas("select * from deportistas", false);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/{id}")
	public Response jugadorId(@PathParam("id") int id) {
		return getDeportistas("select * from deportistas where id="+id, false);		
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/deporte/{nombreDeporte}")
	public Response porDeporte(@PathParam("nombreDeporte") String deporte) {
		return getDeportistas("select * from deportistas where deporte like '"+deporte+"'", false);	
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/activos")
	public Response activos() {
		return getDeportistas("select * from deportistas where activo=true", false);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/retirados")
	public Response retirados() {
		return getDeportistas("select * from deportistas where activo=false", false);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/masculinos")
	public Response masculinos() {
		return getDeportistas("select * from deportistas where genero='masculino'", false); 
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/femeninos")
	public Response femeninos() {
		return getDeportistas("select * from deportistas where genero='femenino'", false);
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/xg")
	public Response porGenero() {			
		Response res = getDeportistas("select * from deportistas where genero='femenino'", false);
		if(deportistas != null) {
			Object[] fem =  deportistas.toArray();
			res = getDeportistas("select * from deportistas where genero='masculino'", false);
			if(deportistas != null) {
				Object[] masc =  deportistas.toArray();		
				Object[][] depor = {fem, masc};
				return Response.ok(depor).build();				
			}else {
				return res;
			}
		}else {
			return res;
		}
	}
		
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/deporte/{nombreDeporte}/activos")
	public Response porDeporteActivos(@PathParam("nombreDeporte") String nombreDeporte) {
		return getDeportistas("select * from deportistas where deporte='"+nombreDeporte+"'"+"and activo=true", false);
	}
		
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/sdepor")
	public Response numDeportistas() {
		Response res = getDeportistas("select * from deportistas group by nombre", false);
		if(deportistas != null) {
			return Response.status(Status.ACCEPTED).entity("Número de deportistas: "+deportistas.size()).build();
		}else {
			return res;
		}		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deportes")
	public Response listarDeportes() {
		ArrayList<String> deportes = new ArrayList<>();
		try {
			Class.forName(driver);
			try(Connection c = DriverManager.getConnection(url, username, password);
					Statement st=c.createStatement()){			
					ResultSet rs=st.executeQuery("select distinct deporte from deportistas order by deporte asc");
					while(rs.next()) {
						deportes.add(rs.getString(1));		
					}														
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
				return Response.status(Status.BAD_REQUEST).entity("Error en la consulta SQL").build();
			}					
		}catch(ClassNotFoundException exc) {
			System.out.println(exc.getMessage());
			return Response.status(Status.BAD_REQUEST).entity("Error de conexión").build();
		}
		if(deportes.size() > 0) {			
			return Response.ok(deportes).build();
		}else {
			return Response.status(Status.NO_CONTENT).build();
		}		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public Response creaDeportista(Deportista depor) {
		deportistas.add(depor);
		return getDeportistas("insert into deportistas values ("+depor.getId()+",'"+
				depor.getNombre()+"', "+depor.isActivo()+", '"+depor.getGenero()+"','"+depor.getDeporte()+"')", true);		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/adds")
	public Response creaDeportistas(Deportista[] depor) {
		Response res = Response.status(Status.BAD_REQUEST).build();
		for(int i=0; i<depor.length; i++) {
			res = getDeportistas("insert into deportistas values ("+depor[i].getId()+",'"+
					depor[i].getNombre()+"', "+depor[i].isActivo()+", '"+depor[i].getGenero()+"','"+
					depor[i].getDeporte()+"')", true);
			if(deportistas == null) {
				return res;
			}		
		}	
		return Response.status(Status.ACCEPTED).entity("Filas afectadas: "+depor.length).build();
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public Response actualizaDeportista(Deportista depor) {		
		return getDeportistas("update deportistas set nombre='"+depor.getNombre()+
				"', activo="+depor.isActivo()+", genero='"+depor.getGenero()+
				"',deporte='"+depor.getDeporte()+"' where id="+depor.getId(), true);								
	}
	
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/del/{id}")
	public Response borraDeportista(@PathParam ("id") int id) {		
		return getDeportistas("delete from deportistas where id="+id, true);							
	}
	
	
	public Response getDeportistas(String query, boolean update){	
		deportistas = new ArrayList<>();	
		try {
			Class.forName(driver);
			try(Connection c = DriverManager.getConnection(url, username, password);
					Statement st=c.createStatement()){
				
				if(!update) {
					ResultSet rs=st.executeQuery(query);
					while(rs.next()) {
						deportistas.add(new Deportista(rs.getInt(1), rs.getString(2), 
								rs.getBoolean(3), rs.getString(4), rs.getString(5)));				
					}	
					List<Deportista> list = deportistas;
					GenericEntity<List<Deportista>> entity = new GenericEntity<List<Deportista>>(list) {};					
					if(deportistas.size() > 0) return Response.ok(entity).build();
					else return Response.status(Status.NOT_FOUND).build();
				}else {
					int filasAfectadas = st.executeUpdate(query); 
					if(filasAfectadas > 0) 
						return Response.status(Status.OK).entity("Filas afectadas: "+filasAfectadas).build();
					else 
						return Response.status(Status.BAD_REQUEST).entity("Error al añadir deportista").build();
				}												
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
				deportistas = null;
				return Response.status(Status.BAD_REQUEST).entity("Error al realizar la consulta").build();
			}					
		}catch(ClassNotFoundException exc) {
			System.out.println(exc.getMessage());
			deportistas = null;
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error de conexión").build();
		}			
	}
	
}
