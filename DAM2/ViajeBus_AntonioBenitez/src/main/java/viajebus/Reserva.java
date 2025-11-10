package viajebus;

public class Reserva 
{
	private static final String[] ESTADOS = {"A","E","C"};
	
	private int numReserva;
	private int codigoViaje;
	private int codigoCliente;
	private int plazasReservadas;
	private String estado;
	
	public Reserva(int numReserva, int codigoViaje, int codigoCliente, int plazasReservadas, String estado) {
		this.numReserva = numReserva;
		this.codigoViaje = codigoViaje;
		this.codigoCliente = codigoCliente;
		this.plazasReservadas = plazasReservadas;
		this.estado = estado;
	}
	
	public int getCodigoCliente() {
		return this.codigoCliente;
	}
	
	public String toString() {
		return String.format("%d. Código VIAJE: %d\tCódigo CLIENTE: %d\tPlazas reservadas: %d %s\tEstado: %s",
				this.numReserva,this.codigoViaje,this.codigoCliente,this.plazasReservadas,this.plazasReservadas == 1 ? "plaza" : "plazas",this.estado);
	}
}
