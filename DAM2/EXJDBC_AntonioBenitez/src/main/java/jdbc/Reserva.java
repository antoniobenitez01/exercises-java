package jdbc;

public class Reserva 
{
	private int numeroReserva;
	private int codigoViaje;
	private int codigoCliente;
	private int plazasReservadas;
	private String estado;
	
	public Reserva(int numeroReserva, int codigoViaje, int codigoCliente, int plazasReservadas, String estado) {
		this.numeroReserva = numeroReserva;
		this.codigoViaje = codigoViaje;
		this.codigoCliente = codigoCliente;
		this.plazasReservadas = plazasReservadas;
		this.estado = estado;
	}
	
	public String getEstado() {
		return this.estado;
	}
	
	public int getCodigoViaje() {
		return this.codigoViaje;
	}
	
	public int getPlazasReservadas() {
		return this.plazasReservadas;
	}
}
