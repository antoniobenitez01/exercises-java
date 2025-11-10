package viajebus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO 
{
	// GET CLIENTE - Devuelve un ArrayList de objetos CLIENTE actualizados desde la BBDD
	public static ArrayList<Cliente> getClientes(Connection conn){
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM clientes");
				ResultSet rs = sql.executeQuery()){
			while(rs.next()) {
				Cliente cliente = new Cliente(rs.getInt("codigo"),rs.getString("nombre"));
				clientes.add(cliente);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return clientes;
	}
	
	// SEARCH CLIENTE - Devuelve TRUE si encuentra un CLIENTE en la BBDD con el CODIGO introducido por parámetro
	public static boolean searchCliente(Connection conn, int codigo) {
		boolean encontrado = false;
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM clientes WHERE codigo = ?")){
			sql.setInt(1, codigo);
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				Cliente cliente = null;
				cliente = new Cliente(rs.getInt("codigo"),rs.getString("nombre"));
				if(cliente != null) {
					encontrado = true;
				}
			}
			rs.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return encontrado;
	}
}
