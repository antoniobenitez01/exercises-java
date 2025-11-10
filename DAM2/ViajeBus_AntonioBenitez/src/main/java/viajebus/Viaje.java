package viajebus;

public class Viaje 
{
	private int codigo;
	private String destino;
	private int plazasDisp;
	
	public Viaje(int codigo, String destino, int plazasDisp) {
		this.codigo = codigo;
		this.destino = destino;
		this.plazasDisp = plazasDisp;
	}
	
	public int getPlazasDisp() {
		return this.plazasDisp;
	}
	
	public String toString() {
		return String.format("%d. %s: %d %s",this.codigo,this.destino,this.plazasDisp,this.plazasDisp == 1 ? "plaza" : "plazas");
	}
}
