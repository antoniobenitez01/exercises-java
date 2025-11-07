package jdbc;

public class Viaje 
{
	private int codigo;
	private String destino;
	private int plazasDisponibles;
	
	public Viaje(int codigo, String destino, int plazasDisponibles) {
		this.codigo = codigo;
		this.destino = destino;
		this.plazasDisponibles = plazasDisponibles;
	}
	
	public String getDestino() {
		return this.destino;
	}
	
	public int getPlazas() {
		return this.plazasDisponibles;
	}
	
	public String toString() {
		return String.format("%d. %s - Plazas: %d", this.codigo,this.destino,this.plazasDisponibles);
	}
}
