package viajebus;

public class Cliente 
{
	private int codigo;
	private String nombre;
	
	public Cliente(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}
	
	public String toString() {
		return String.format("%d. %s",this.codigo, this.nombre);
	}
}
