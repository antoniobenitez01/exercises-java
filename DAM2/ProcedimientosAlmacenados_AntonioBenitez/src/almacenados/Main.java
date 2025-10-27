package almacenados;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Main 
{
	public static void main(String[] args) 
	{
		// VARIABLES ------------------------------||
		Properties props = new Properties();
		PreparedStatement stmt;
		ResultSet rs;
		Scanner entrada = new Scanner(System.in);
		int respuesta = 0, entradas;
		String nombre, contrasena, completo;
		// PROGRAMA -------------------------------||
		try {
			props.load(new FileInputStream("mysql.cnf"));
			String url = String.format("jdbc:mysql://%s:%s/%s",
							props.getProperty("ip"),props.getProperty("port"),props.getProperty("dbname"));
			String user = "almacenados";
			String password = "123";
			Connection conn = Common.connectionSQL(url,user,password);
			if(conn != null) 
			{
				while(respuesta != 4) {
					respuesta = Common.menu("=== PROCEDIMIENTOS ALMACENADOS ==="
							+ "\n1. Insertar nuevo Usuario\n2. Número de entradas de un Usuario"
							+ "\n3. Proceso de Login de Usuario\n4. Apagar programa",entrada,1,4);
					switch(respuesta) {
					case 1:
						System.out.println("Introduzca el nombre del usuario a insertar.");
						nombre = entrada.nextLine();
						System.out.println("Introduzca el nombre completo del usuario a insertar.");
						completo = entrada.nextLine();
						System.out.println("Introduzca la contraseña del usuario a insertar.");
						contrasena = entrada.nextLine();
						insertarUsuario(conn,nombre,completo,contrasena);
						break;
					case 2:
						entradas = -1;
						System.out.println("Introduzca el nombre del usuario.");
						nombre = entrada.nextLine();
						entradas = getEntradas(conn,nombre);
						if(entradas > -1) {
							System.out.printf("El usuario %s tiene %d %s.\n",nombre,entradas,entradas == 1 ? "entrada" : "entradas");
						}
						break;
					case 3:
						System.out.println("Introduzca el nombre de usuario.");
						nombre = entrada.nextLine();
						System.out.println("Introduzca la contraseña");
						contrasena = entrada.nextLine();
						String resultado = userLogin(conn,nombre,contrasena);
						System.out.println(resultado);
						break;
					case 4:
						System.out.println("Apagando programa ...");
						try {
							conn.close();
						}catch(SQLException e) {
							System.out.println(e.getMessage());
						}
						break;
					}
				}
			}else {
				System.out.println("Se ha detectado un problema al conectar con el servidor SQL.");
			}
		}catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		entrada.close(); // Cerrar Objeto Scanner
	}
	
	//INSERTAR USUARIO - Inserta un nuevo Usuario mediante su nombre, nombre completo y contraseña
	public static void insertarUsuario(Connection conn, String nombre, String completo, String contrasena){
		CallableStatement cstmt = null;
		try {
			String sql = "{CALL insertarUsuario(?,?,?)}";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, nombre);
			cstmt.setString(2, contrasena);
			cstmt.setString(3, completo);
			
			cstmt.execute();
			System.out.println("Usuario insertado con éxito.");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if (cstmt != null) cstmt.close();
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	// GET ENTRADAS - Devuelve el número de entradas del usuario introducido por parámetro
	public static int getEntradas(Connection conn, String nombre) {
		int entradas = -1;
		CallableStatement cstmt = null;
		try {
			String sql = "{CALL getEntradas(?,?)}";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, nombre);
			cstmt.setInt(2, -1);
			cstmt.execute();
			
			entradas = cstmt.getInt("numEntradas");
		}catch(SQLException e) {
			System.out.println("El usuario introducido no existe.");
		}finally {
			try {
				if (cstmt != null) cstmt.close();
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return entradas;
	}
	
	// USER LOGIN - Devuelve el mensaje resultado tras hacer login con un usuario y contraseña
	public static String userLogin(Connection conn, String nombre, String contrasena) {
		String resultado = "";
		CallableStatement cstmt = null;
		try {
			String sql = "{CALL userLogin(?,?,?)}";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, nombre);
			cstmt.setString(2, contrasena);
			cstmt.setString(3, "");
			cstmt.execute();
			
			resultado = cstmt.getString("resultado");
		}catch(SQLException e) {
			System.out.println("El usuario introducido no existe.");
		}finally {
			try {
				if (cstmt != null) cstmt.close();
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return resultado;
	}
}
