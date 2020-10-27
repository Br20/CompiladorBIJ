package compilador;

public class Atributo {
	
	private String nombre;
	private Object value;
	
	public Atributo(String nombre, Object value) {
		this.nombre = nombre;
		this.value = value;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String toString() {
		return ": " + this.value.toString();
	}
	
}
