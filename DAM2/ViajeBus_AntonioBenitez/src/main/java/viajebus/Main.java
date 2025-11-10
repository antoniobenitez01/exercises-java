package viajebus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
	public static void main(String[] args) 
	{
		// VARIABLES			==================================
		Scanner entrada = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/viajes_bus", user = "viajes", password = "123";
		Connection conn = Common.connectionSQL(url,user,password);
		ArrayList<Viaje> viajes;
		ArrayList<Cliente> clientes;
		ArrayList<Reserva> reservas;
		// PROGRAMA				==================================
		if(conn != null) {
			int respuesta = 0;
			while(respuesta != 6) {
				// ACTUALIZACIÓN DATOS LOCALES
				viajes = ViajeDAO.getViajes(conn);
				clientes = ClienteDAO.getClientes(conn);
				reservas = ReservaDAO.getReservas(conn);
				// MENÚ
				respuesta = Common.menu("\n=== VIAJES BUS ===\n\n1. Mostrar VIAJES\n2. Mostrar CLIENTES\n3. Mostrar RESERVAS"
						+ "\n4. Crear RESERVA\n5. Cancelar RESERVA\n6. Apagar Programa",entrada,1,6);
				switch(respuesta) {
				case 1:									// MOSTRAR VIAJES
					System.out.println("\n--- VIAJES Disponibles ---\n");
					showList(viajes,"No hay ningún VIAJE registrado.");
					break;
				case 2:									// MOSTRAR CLIENTES
					System.out.println("\n--- CLIENTES Registrados ---\n");
					showList(clientes,"No hay ningún CLIENTE registrado.");
					break;
				case 3:									// MOSTRAR RESERVAS
					System.out.println("\n--- RESERVAS Registradas ---\n");
					showList(reservas,"No hay ninguna RESERVA registrada.");
					break;
				case 4:									// CREAR RESERVA
					int codigoViaje = checkViaje(conn,entrada,"Introduzca el CÓDIGO VIAJE.");
					int codigoCliente = checkCliente(conn,entrada);
					int plazas = checkPlazas(entrada);
					System.out.printf("Filas insertadas: %d\n",ReservaDAO.insertReserva(conn, codigoViaje, codigoCliente, plazas));
					break;
				case 5:									// CANCELAR RESERVA
					if(reservas.isEmpty()) {
						System.out.println("No hay ninguna RESERVA registrada para cancelar.");
					}else {
						System.out.println("\n--- RESERVAS Registradas ---\n");
						showList(reservas,"LOGIC ERROR ! Too bad !");
						int toCancelar = checkViaje(conn,entrada,"\nIntroduzca el CÓDIGO VIAJE que desea cancelar.");
						boolean cancelar = Common.booleanCheck("¿Está seguro de que desea cancelar la RESERVA? (SI/NO)",entrada);
						if(cancelar) {
							ReservaDAO.cancelarReserva(conn, toCancelar,reservas.get(toCancelar-1).getCodigoCliente());
						}else {
							System.out.println("Operación canceladas. Volviendo al menú principal ...");
						}
					}
					break;
				case 6:									// APAGAR PROGRAMA
					System.out.println("Apagando programa ...");
					break;
				}
			}
		}		
		entrada.close();								// Close Scanner
	}
	
	// CHECK VIAJE - Verificación de que el CÓDIGO VIAJE existe en la BBDD
	public static int checkViaje(Connection conn, Scanner entrada,String mensaje) {
		int input = -1;
		Viaje encontrado = null;
		while(encontrado == null) {
			input = Common.inputInt(mensaje, entrada);
			encontrado = ViajeDAO.searchViaje(conn, input);
			if(encontrado == null){
				System.out.println("ERROR: El CÓDIGO VIAJE introducido no existe. Inténtelo de nuevo.");
			}
		}
		return input;
	}
	
	// CHECK CLIENTE - Verificación de que el CÓDIGO CLIENTE existe en la BBDD
	public static int checkCliente(Connection conn, Scanner entrada) {
		boolean checked = false;
		int input = -1;
		while(checked == false) {
			input = Common.inputInt("Introduzca el CÓDIGO CLIENTE.", entrada);
			checked = ClienteDAO.searchCliente(conn, input);
			if(checked == false){
				System.out.println("ERROR: El CÓDIGO CLIENTE introducido no existe. Inténtelo de nuevo.");
			}
		}
		return input;
	}
	
	// CHECK PLAZAS - Verificación de que el número de plazas introducido es mayor que 0
	public static int checkPlazas(Scanner entrada) {
		int plazas = 0;
		while(plazas <= 0) {
			plazas = Common.inputInt("Introduzca el número de plazas a reservar.", entrada);
			if(plazas <= 0) {
				System.out.println("ERROR: Las plazas reservadas deben ser mayor que 0.");
			}
		}
		return plazas;
	}
	
	// SHOW LIST - Muestra los objetos de la Lista pasada por parámetro
	public static <T> void showList(ArrayList<T> list, String mensaje) {
		if(list.isEmpty()) {
			System.out.println(mensaje);
		}else {
			for(Object object: list) {
				System.out.printf("%s\n",object.toString());
			}
		}
	}
}
