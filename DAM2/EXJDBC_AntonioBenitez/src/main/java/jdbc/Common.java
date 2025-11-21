package jdbc;

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
	
	//GET VIAJES - Devuelve un ArrayList de todos los VIAJES
	public static ArrayList<Viaje> getViajes(Connection conn){
		ArrayList<Viaje> viajes = new ArrayList<Viaje>();
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM viajes");
				ResultSet rs = sql.executeQuery()){
			while(rs.next()) {
				viajes.add(new Viaje(rs.getInt("codigo"),rs.getString("destino"),rs.getInt("plazas_disponibles")));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return viajes;
	}
	
	//GET RESERVAS - Devuelve un ArrayList de todos los RESERVAS
	public static ArrayList<Reserva> getReservas(Connection conn){
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM reservas");
				ResultSet rs = sql.executeQuery()){
			while(rs.next()) {
				reservas.add(new Reserva(rs.getInt("numero_reserva"),rs.getInt("codigo_viaje"),rs.getInt("codigo_cliente"),
						rs.getInt("plazas_reservadas"),rs.getString("estado")));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return reservas;
	}
	
	//UPDATE PLAZAS - Devuelve las filas modificadas al actualizar las plazas disponibles de un VIAJE
	public static int updatePlazas(int plazas, int codigoViaje, Connection conn) {
		int filas = 0;
		try(PreparedStatement update = conn.prepareStatement("UPDATE viajes SET plazas_disponibles = plazas_disponibles + ? WHERE codigo = ?")){
			System.out.println("Actualizando tabla VIAJES ...");
			update.setInt(1, plazas);
			update.setInt(2, codigoViaje);
			filas = update.executeUpdate();
			System.out.printf("Filas modificadas: %d\n",filas);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return filas;
	}
	
	//PLAZAS LIBRES - Devuelve el número de plazas libres de el VIAJE con el destino introducido
	public static int plazasLibres(String destino, Connection conn) {
		int resultado = -1;
		ArrayList<Viaje> viajes = getViajes(conn);
		for(Viaje v : viajes) {
			if(destino.toLowerCase().equals(v.getDestino().toLowerCase())) {
				resultado = v.getPlazas();
			}
		}
		return resultado;
	}
}
