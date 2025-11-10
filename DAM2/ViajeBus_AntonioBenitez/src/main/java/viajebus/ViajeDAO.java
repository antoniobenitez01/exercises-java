package viajebus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViajeDAO 
{
	// GET VIAJES - Devuelve un ArrayList de objetos VIAJE actualizados desde la BBDD
	public static ArrayList<Viaje> getViajes(Connection conn){
		ArrayList<Viaje> viajes = new ArrayList<Viaje>();
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM viajes");
				ResultSet rs = sql.executeQuery()){
			while(rs.next()) {
				Viaje viaje = new Viaje(rs.getInt("codigo"),rs.getString("destino"),rs.getInt("plazas_disponibles"));
				viajes.add(viaje);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return viajes;
	}
	
	// SEARCH VIAJE - Devuelve TRUE si encuentra un VIAJE en la BBDD con el CODIGO introducido por parámetro
	public static Viaje searchViaje(Connection conn, int codigo) {
		Viaje viaje = null;
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM viajes WHERE codigo = ?")){
			sql.setInt(1, codigo);
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				viaje = new Viaje(rs.getInt("codigo"),rs.getString("destino"),rs.getInt("plazas_disponibles"));
			}
			rs.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return viaje;
	}
	
	// PLAZAS LIBRES - Devuelve las plazas libres del VIAJE con destino introducido por parámetro
	public static int plazasLibres(Connection conn, String destino) {
		int plazas = -1;
		try(PreparedStatement stmt = conn.prepareStatement("SELECT plazas_disponibles FROM viajes WHERE destino = ?");){
			stmt.setString(1, destino);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				plazas = rs.getInt("plazas_disponibles");
			}
			rs.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return plazas;
	}
}
