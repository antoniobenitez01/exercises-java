package sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Common 
{
	//MENU - Muestra el menú principal y devuelve el entero de la opción elegida
	public static int menu(String mensaje, Scanner entrada, int min, int max)
	{
		System.out.println(mensaje);
		int respuesta = 0;
		while(respuesta < min || respuesta > max)
		{
			respuesta = inputInt("Introduzca la opción a elegir.", entrada);
			if(respuesta < min || respuesta > max)
			{
				System.out.println("Respuesta no válida, inténtelo de nuevo.");
			}
		}
		return respuesta;
	}
	
	//INPUT INT - Recoge un mensaje String y un objeto Scanner, comprueba si se introduce un valor Integer correctamente
	public static int inputInt(String mensaje, Scanner entrada)
	{
		int num = 0;
		boolean inputTrue = false;
		do
		{
			//EXCEPCIÓN - InputMismatchException - Bucle que asegura que el valor introducido es un valor decimal
			try 
			{
				System.out.println(mensaje);
				num = entrada.nextInt();
				entrada.nextLine(); // Depuración Scanner
				inputTrue = true;
			} catch(InputMismatchException excepcion1)
			{
				System.out.println("Valor introducido incorrecto.");
				entrada.nextLine(); // Depuración Scanner
			}
		}while(inputTrue == false);
		return num;
	}
	
	//BOOLEAN CHECK - Devuelve un boolean en base a la respuesta SI o NO introducida por teclado
	public static boolean booleanCheck(String mensaje, Scanner entradaTeclado)
	{
		boolean resultado = false, flag = false;
		do
		{
			System.out.println(mensaje);
			String respuesta = entradaTeclado.nextLine();
			if(respuesta.toLowerCase().equals("si") || respuesta.toLowerCase().equals("sí"))
			{
				resultado = true;
				flag = true;
			}
			else if(respuesta.toLowerCase().equals("no"))
			{
				resultado = false;
				flag = true;
			}
			else
			{
				System.out.println("Respuesta no válida. Inténtelo de nuevo.");
				flag = false;
			}
		}while(flag == false);
		return resultado;
	}
	
	//INPUT DOUBLE - Recoge un mensaje String y un objeto Scanner, comprueba si se introduce un valor Double correctamente
	public static double inputDouble(String mensaje, Scanner entrada)
	{
		double num = 0;
		boolean inputTrue = false;
		do
		{
			//EXCEPCIÓN - InputMismatchException - Bucle que asegura que el valor introducido es un valor decimal
			try 
			{
				System.out.println(mensaje);
				num = entrada.nextDouble();
				entrada.nextLine(); // Depuración Scanner
				inputTrue = true;
			} catch(InputMismatchException excepcion1)
			{
				System.out.println("Valor introducido incorrecto.");
				entrada.nextLine(); // Depuración Scanner
			}
		}while(inputTrue == false);
		return num;
	}
	
	//PICK EQUIPO - Devuelve el código del EQUIPO elegido por teclado
	public static String pickEquipo(Connection conn, Scanner entrada) {
		String codigo = "";
		ArrayList<String> codigos = selectEquipos(conn,true);
		if(codigos.isEmpty()) {
			System.out.println("La tabla EQUIPOS está vacía.");
		}else {
			int escogido;
			do{
				escogido = Common.inputInt("Escoja el EQUIPO a continuación.", entrada);
				System.out.println(escogido);
				if(escogido < 1 || escogido > codigos.size()) {
					System.out.println("Opción no válida, inténtelo de nuevo.");
				}
			}while(escogido < 1 || escogido > codigos.size());
			codigo = codigos.get(escogido-1);
		}
		return codigo;
	}
	
	//SELECT EQUIPOS - Devuelve los códigos recibidos al ejecutar SELECT sobre la tabla EQUIPOS
	public static ArrayList<String> selectEquipos(Connection conn, boolean show) {
		ArrayList<String> codigos = null;
		String sql = "SELECT * FROM equipos";
		try(PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()){
				codigos = new ArrayList<String>();
				int contador = 1;
				while(rs.next()) {
					if(show) {
						System.out.printf("%d. Código: %s\t\tModelo: %s\t\tDescripción: %s\n", contador,
								rs.getString("codigo"),rs.getString("modelo"),rs.getString("descripcion"));
					}
					codigos.add(rs.getString("codigo"));
					contador++;
				}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return codigos;
	}
	
	//SELECT PIEZA - Devuelve los códigos recibidos al ejecutar SELECT sobre la tabla PIEZA
	public static ArrayList<String> selectPiezas(Connection conn, boolean show) {
		ArrayList<String> codigos = null;
		String sql = "SELECT * FROM piezas";
		try(PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()){
				codigos = new ArrayList<String>();
				int contador = 1;
				while(rs.next()) {
					if(show) {
						System.out.printf("%d. Código: %s\t\tTipo: %s\t\tDescripción: %s\t\tUnidades: %d\n", contador,
								rs.getString("codigo"),rs.getString("tipo"),rs.getString("descripcion"),rs.getInt("unidades"));
					}
					codigos.add(rs.getString("codigo"));
					contador++;
				}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return codigos;
	}
	
	//INSERTAR EQUIPO - Devuelve las filas insertadas al ejecutar un INSERT sobre la tabla EQUIPOS
	public static int insertarEquipo(Connection conn, String[] datos) {
		int filasInsertadas = 0;
		try(PreparedStatement insertEquipo = conn.prepareStatement("INSERT INTO equipos VALUES(?,?,?)");){
			insertEquipo.setString(1, datos[0]);
			insertEquipo.setString(2, datos[1]);
			insertEquipo.setString(3, datos[2]);
			filasInsertadas = insertEquipo.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return filasInsertadas;
	}
	
	//INSERTAR PIEZA - Devuelve las filas insertadas al ejecutar un INSERT sobre la tabla PIEZAS
	public static int insertarPieza(Connection conn, String[] datos) {
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
