package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
	public static void main(String[] args) 
	{
		
		// VARIABLES	------------------------------||
		
		Scanner entrada = new Scanner(System.in);
		int respuesta = 0, entradas, filas;
		
		// PROGRAMA		------------------------------||
		
		String url = String.format("jdbc:sqlite:viajesbus.db");
		System.out.println("Trying SQL Connection ...");
		
		try(Connection conn = DriverManager.getConnection(url)){
			System.out.println("SQL Connection succesful.");
			while (respuesta != 4) {
				
				//	Actualizar datos ArrayList
				ArrayList<Viaje> viajes = Common.getViajes(conn);
				ArrayList<Reserva> reservas = Common.getReservas(conn);
				
				respuesta = Common.menu("\n=== EXÁMEN JDBC ====\n1. Mostrar plazas libres"
						+ "\n2. Crear Reserva\n3. Cancelar Reserva\n4. Apagar el programa",entrada,1,4);
				switch(respuesta) {
				case 1:						// MOSTRAR PLAZAS LIBRES
					if(viajes.isEmpty()) {
						System.out.println("ERROR: No hay ningún Viaje registrado.");
					}else {
						System.out.println("\n--- Destinos disponibles ---\n");
						for(Viaje v : viajes) {
							System.out.printf("- %s\n",v.getDestino());
						}
						System.out.println("\nIntroduzca el Destino que quiere consultar.");
						String destino = entrada.nextLine();
						int resultado = Common.plazasLibres(destino,conn);
						if(resultado == -1) {
							System.out.println("El destino introducido no está registrado.");
						}else {
							System.out.printf("%s - Plazas disponibles: %d %s\n",destino,resultado,resultado == 1 ? "plaza" : "plazas");
						}
					}
					break;
				case 2:						// CREAR RESERVA
					System.out.println("\n--- Crear RESERVA ---\n");
					for(Viaje v : viajes) {
						System.out.printf("%s\n",v.toString());
					}
					int viajeInput = 0;
					while(viajeInput < 1 || viajeInput > viajes.size()) {
						viajeInput = Common.inputInt("Seleccione el viaje a elegir", entrada);
						if(viajeInput < 1 || viajeInput > viajes.size()) {
							System.out.println("Opción no válida. Inténtelo de nuevo.");
						}else {
							int cliente = Common.inputInt("Introduzca el código de cliente.",entrada);
							int plazas = Common.inputInt("Introduzca cuantas plazas se van a reservar", entrada);
							String estado = "A";
							if(plazas > viajes.get(viajeInput-1).getPlazas()) {
								estado = "E";
							}
							try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO reservas VALUES (?,?,?,?,?)")){
								stmt.setInt(1, reservas.size()+1);
								stmt.setInt(2,viajeInput);
								stmt.setInt(3,cliente);
								stmt.setInt(4,plazas);
								stmt.setString(5, estado);
								System.out.println("Insertando en tabla RESERVAS ...");
								filas = stmt.executeUpdate();
								System.out.printf("Filas modificadas: %d\n",filas);
								// Actualizar datos RESERVAS
								reservas = Common.getReservas(conn);
								Reserva reservaInsertada = reservas.get(reservas.size()-1);
								// Actualizar datos PLAZAS de tabla VIAJES
								if(reservaInsertada.getEstado().equals("A")) {
									Common.updatePlazas(reservaInsertada.getPlazasReservadas() * -1,reservaInsertada.getCodigoViaje(),conn);
								}
							}catch(SQLException e) {
								System.out.println(e.getMessage());
							}
						}
					}
					break;
				case 3:						// CANCELAR RESERVA
					System.out.println("\n--- Cancelar RESERVA ---\n");
					try(PreparedStatement smtm = conn.prepareStatement("SELECT * FROM reservas");
							ResultSet rs = smtm.executeQuery()){
						entradas = 0;
						while(rs.next()) {
							System.out.printf("%d.\tCódigo viaje: %d\tCódigo cliente: %d\tPlazas reservadas: %d\tEstado: %s\n",
									rs.getInt("numero_reserva"),rs.getInt("codigo_viaje"),rs.getInt("codigo_cliente"),
									rs.getInt("plazas_reservadas"),rs.getString("estado"));
							entradas++;
						}
						if(entradas == 0) {
							System.out.println("ERROR: No hay ninguna RESERVA registrada.");
						}else {
							respuesta = 0;
							while(respuesta < 1 || respuesta > entradas) {
								respuesta = Common.inputInt("Introduzca el código de la RESERVA a cancelar.", entrada);
								if(respuesta < 1 || respuesta > entradas) {
									System.out.println("Opción no válida. Inténtelo de nuevo");
								}else {
									boolean choice = Common.booleanCheck("¿Está seguro de que desea cancelar esta reserva? (SI/NO)", entrada);
									if(choice) {
										Reserva reservaCancelada = reservas.get(respuesta-1);
										try(PreparedStatement stmt = conn.prepareStatement("UPDATE reservas SET estado = ? WHERE numero_reserva = ?")){
											stmt.setString(1, "C");
											stmt.setInt(2, respuesta);
											System.out.println("Actualizando tabla RESERVAS ...");
											filas = stmt.executeUpdate();
											System.out.printf("Filas modificadas: %d\n",filas);
											// Actualizar datos PLAZAS de tabla VIAJES
											if(reservaCancelada.getEstado().equals("A")) {
												Common.updatePlazas(reservaCancelada.getPlazasReservadas(),reservaCancelada.getCodigoViaje(),conn);
											}
										}catch(SQLException e) {
											System.out.println(e.getMessage());
										}
									}else {
										System.out.println("Cancelando operación ...");
									}
								}
							}
						}
					}catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					respuesta = 0;
					break;
				case 4:						// APAGANDO PROGRAMA
					System.out.println("Apagando programa ...");
					break;
				}
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		entrada.close();					// Close Scanner
	}
}
