package restful;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Deportista {
	private int id;
	private String nombre;
	private boolean activo;
	private String deporte;
	private String genero;
	
	public Deportista() {
		
	}
	
	public Deportista(int id, String nombre, boolean activo, String deporte, String genero) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.activo = activo;
		this.deporte = deporte;
		this.genero = genero;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public String getDeporte() {
		return deporte;
	}
	public void setDeporte(String deporte) {
		this.deporte = deporte;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
}