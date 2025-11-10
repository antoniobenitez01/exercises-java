package viajebus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	//CONNECTION SQL - Intenta conexión SQL con parámetros devolviendo true si se conecta correctamente
	public static Connection connectionSQL(String url, String user, String password)
	{
		System.out.println("Trying SQL Connection ...");
		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection(url,user,password);
			System.out.println("SQL Connection Succesful.");
		}catch(SQLException ex)
		{
			System.out.println("SQL Exception: " + ex.getMessage());
		}
		return conn;
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
}
