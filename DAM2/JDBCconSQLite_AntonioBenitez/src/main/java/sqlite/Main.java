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
						case 1:
							numTablas = selectEquipos(conn);
							if(numTablas == 0) {
								System.out.println("No hay ningún EQUIPO registrado en la base de datos.");
							}
							break;
						case 2:
							break;
						case 3:
							break;
						case 4:
							break;
						case 5:
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
						case 1:
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
							filas = insertarPieza(conn,datosPieza);
							System.out.printf("Filas insertadas: %d\n",filas);
							break;
						case 2:
							try(PreparedStatement piezas = conn.prepareStatement("SELECT * FROM piezas");
									ResultSet piezasResult = piezas.executeQuery()){
								numTablas = 0;
								while(piezasResult.next()) {
									System.out.printf("Código: %s\t\tTipo: %s\t\tDescripción: %s\t\tUnidades: %d\tCódigo EQUIPO: %s\n",
											piezasResult.getString("codigo"),piezasResult.getString("tipo"),
											piezasResult.getString("descripcion"),piezasResult.getInt("unidades"),
											piezasResult.getString("equipo_codigo"));
									numTablas++;
								}
							}catch(SQLException e) {
								System.out.println(e.getMessage());
							}
							break;
						case 3:
							System.out.println("Volviendo al Menú Principal ...");
							break;
						}
					}
					break;
				case 3: // CONSULTA EQUIPO PIEZAS	======================================================================================================
					String sql = "SELECT * FROM equipos";
					try(PreparedStatement stmt = conn.prepareStatement(sql);
						ResultSet rs = stmt.executeQuery()){
						ArrayList<String> codigos = new ArrayList<String>();
						int contador = 1;
						while(rs.next()) {
							System.out.printf("%d. Código: %s\t\tModelo: %s\t\tDescripción: %s\n", contador,
									rs.getString("codigo"),rs.getString("modelo"),rs.getString("descripcion"));
							codigos.add(rs.getString("codigo"));
							contador++;
						}
						if(codigos.isEmpty()) {
							System.out.println("La tabla EQUIPOS está vacía.");
						}else {
							PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM piezas WHERE equipo_codigo = ?");
							int escogido;
							do{
								escogido = Common.inputInt("Escoja el Equipo para mostrar sus Piezas", entrada);
								System.out.println(escogido);
								if(escogido < 1 || escogido > contador-1) {
									System.out.println("Opción no válida, inténtelo de nuevo.");
								}
							}while(escogido < 1 || escogido > contador-1);
							stmt2.setString(1, codigos.get(escogido-1));
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
						}
					}catch(SQLException e) {
						System.out.println(e.getMessage());
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
	
	//SELECT EQUIPOS - Devuelve el número de tablas recibidas al ejecutar SELECT sobre la tabla EQUIPOS
	private static int selectEquipos(Connection conn) {
		int tablas = 0;
		try(PreparedStatement select = conn.prepareStatement("SELECT * FROM equipos");
			ResultSet rsSelect = select.executeQuery()){
			while(rsSelect.next()) {
				System.out.printf("Código: %s\t\tModelo: %s\t\tDescripción: %s\n",
						rsSelect.getString("codigo"),rsSelect.getString("modelo"),rsSelect.getString("descripcion"));
				tablas++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return tablas;
	}
	
	//INSERTAR PIEZA - Devuelve las filas insertadas al ejecutar un INSERT sobre la tabla PIEZAS
	private static int insertarPieza(Connection conn, String[] datos) {
		int filasInsertadas = 0;
		try(PreparedStatement insertPieza = conn.prepareStatement("INSERT INTO piezas VALUES(?,?,?,?,?)");){
			insertPieza.setString(1, datos[0]);
			insertPieza.setString(2, datos[1]);
			insertPieza.setString(3, datos[2]);
			insertPieza.setInt(4, Integer.parseInt(datos[3]));
			insertPieza.setString(5, datos[4]);
			filasInsertadas = insertPieza.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return filasInsertadas;
	}
}
