package sqlite;

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
		// VARIABLES ------------------------------||
		Scanner entrada = new Scanner(System.in);
		int respuesta = 0, numTablas = 0, filas = 0;
		String equipoEscogido;
		// PROGRAMA -------------------------------||
		String url = String.format("jdbc:sqlite:c:/Users/dam2/Documents/DB.Browser.for.SQLite-v3.13.1-win64/databases/Mantenimiento.db");
		System.out.println("Trying SQL Connection ...");
		try(Connection conn = DriverManager.getConnection(url)){
			System.out.println("SQL Connection Succesful.");
			while(respuesta != 4) {
				respuesta = Common.menu("\n=== MENÚ PRINCIPAL: SQLITE ===\n"
						+ "\n1. CRUD Tabla de Equipos"
						+ "\n2. Alta/Consulta de Piezas"
						+ "\n3. Consulta de Equipo con sus Piezas"
						+ "\n4. Apagar el programa",entrada,1,4);
				switch(respuesta) {
				case 1: // CRUD TABLA EQUIPOS		======================================================================================================
					respuesta = 0;
					while(respuesta != 5) {
						respuesta = Common.menu("\n--- CRUD Tabla de EQUIPOS ---\n"
								+ "\n1. SELECT todos los EQUIPOS"
								+ "\n2. INSERT un nuevo EQUIPO"
								+ "\n3. UPDATE un EQUIPO existente"
								+ "\n4. DELETE un EQUIPO existente"
								+ "\n5. Volver al Menú Principal", entrada, 1, 5);
						switch(respuesta) {
						case 1: // SELECT todos EQUIPOS		--------------------------------------
							ArrayList<String> codigosEquipos = Common.selectEquipos(conn,true);
							if(codigosEquipos.size() == 0) {
								System.out.println("No hay ningún EQUIPO registrado en la base de datos.");
							}
							break;
						case 2: // INSERT nuevo EQUIPO		--------------------------------------
							String[] datosEquipo = new String[3];
							System.out.println("Introduzca el Código del EQUIPO a insertar.");
							datosEquipo[0] = entrada.nextLine();
							System.out.println("Introduzca el Modelo del EQUIPO a insertar.");
							datosEquipo[1] = entrada.nextLine();
							System.out.println("Introduzca la Descripción del EQUIPO a insertar.");
							datosEquipo[2] = entrada.nextLine();
							if(Common.selectEquipos(conn,false).contains(datosEquipo[0])) {
								System.out.println("ERROR: El Código del EQUIPO a insertar ya está registrado.");
							}else {
								filas = Common.insertarEquipo(conn,datosEquipo);
								System.out.printf("Filas insertadas: %d\n",filas);
							}
							break;
						case 3: // UPDATE EQUIPO			--------------------------------------
							equipoEscogido = Common.pickEquipo(conn,entrada);
							try(PreparedStatement update = conn.prepareStatement("UPDATE equipos SET modelo = ?, descripcion = ? WHERE codigo = ?")){
								System.out.println("Introduzca el nuevo campo Modelo.");
								update.setString(1, entrada.nextLine());
								System.out.println("Introduzca el nuevo campo Descripción.");
								update.setString(2, entrada.nextLine());
								update.setString(3, equipoEscogido);
								filas = update.executeUpdate();
								System.out.printf("Filas insertadas: %d\n",filas);
							}catch(SQLException e) {
								System.out.println(e.getMessage());
							}
							break;
						case 4: // DELETE EQUIPO			--------------------------------------
							equipoEscogido = Common.pickEquipo(conn,entrada);
							boolean confirmDelete = Common.booleanCheck("¿Está seguro de que quiere borrar este EQUIPO? (SI/NO)",entrada);
							if(confirmDelete) {
								try(PreparedStatement delete = conn.prepareStatement("DELETE FROM equipos WHERE codigo = ?")){
									delete.setString(1, equipoEscogido);
									filas = delete.executeUpdate();
									System.out.printf("Filas insertadas: %d\n",filas);
								}catch(SQLException e) {
									System.out.println(e.getMessage());
								}
							}else {
								System.out.println("Cancelando operación ...");
							}
							break;
						case 5: // Volver Menu Principal	--------------------------------------
							System.out.println("Volviendo al Menú Principal ...");
							break;
						}
					}
					break;
				case 2: // ALTA CONSULTA PIEZAS		======================================================================================================
					respuesta = 0;
					while(respuesta != 3) {
						respuesta = Common.menu("\n--- Alta y Consulta de PIEZAS ---\n"
								+ "\n1. Alta de PIEZAS"
								+ "\n2. Consulta de PIEZAS"
								+ "\n3. Volver al Menú Principal", entrada, 1, 3);
						switch(respuesta) {
						case 1: // Alta nueva PIEZA			--------------------------------------
							String[] datosPieza = new String[5];
							System.out.println("Introduzca el código de la nueva PIEZA.");
							datosPieza[0] = entrada.nextLine();
							System.out.println("Introduzca el tipo de la nueva PIEZA.");
							datosPieza[1] = entrada.nextLine();
							System.out.println("Introduzca la descripción de la nueva PIEZA.");
							datosPieza[2] = entrada.nextLine();
							datosPieza[3] = String.valueOf(Common.inputInt("Introduzca las unidades de la nueva PIEZA.",entrada));
							System.out.println("Introduzca el código del EQUIPO de la nueva PIEZA.");
							datosPieza[4] = entrada.nextLine();
							if(Common.selectPiezas(conn,false).contains(datosPieza[0])) {
								System.out.println("ERROR: El Código de la PIEZA a insertar ya está registrado.");
							}else if(Common.selectEquipos(conn,false).contains(datosPieza[4]) == false) {
								System.out.println("ERROR: El Código de EQUIPO de la PIEZA a insertar no está registrado.");
							}
							else {
								filas = Common.insertarPieza(conn,datosPieza);
								System.out.printf("Filas insertadas: %d\n",filas);
							}
							break;
						case 2: // Consulta PIEZAS			--------------------------------------
							ArrayList<String> codigosPiezas = Common.selectPiezas(conn,true);
							if(codigosPiezas.size() == 0) {
								System.out.println("No hay ninguna PIEZA registrada en la base de datos.");
							}
							break;
						case 3: // Volver Menu Principal	--------------------------------------
							System.out.println("Volviendo al Menú Principal ...");
							break;
						}
					}
					break;
				case 3: // CONSULTA EQUIPO PIEZAS	======================================================================================================
					equipoEscogido = Common.pickEquipo(conn,entrada);
					if(equipoEscogido.isBlank()) {
						System.out.println("No se ha encontrado ningún EQUIPO registrado, cancelando operación ...");
					}else {
						try(PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM piezas WHERE equipo_codigo = ?")){
							stmt2.setString(1,equipoEscogido);
							ResultSet rs2 = stmt2.executeQuery();
							numTablas = 0;
							while(rs2.next()) {
								System.out.printf("Código: %s\t\tTipo: %s\t\tDescripción: %s\t\tUnidades: %d\n",
										rs2.getString("codigo"),rs2.getString("tipo"),rs2.getString("descripcion"),rs2.getInt("unidades"));
								numTablas++;
							}
							if(numTablas == 0) {
								System.out.println("No se han encontrado PIEZAS para el EQUIPO introducido.");
							}
							stmt2.close();
							rs2.close();
						}catch(SQLException e) {
							System.out.println(e.getMessage());
						}
					}
					break;
				case 4: // APAGAR PROGRAMA			======================================================================================================
					System.out.println("Apagando programa ...");
					break;
				}
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		entrada.close();
	}
}
