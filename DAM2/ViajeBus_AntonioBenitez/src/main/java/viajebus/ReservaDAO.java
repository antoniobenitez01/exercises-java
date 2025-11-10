package viajebus;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservaDAO 
{
	// GET RESERVAS - Devuelve un ArrayList de objetos RESERVA actualizados desde la BBDD
	public static ArrayList<Reserva> getReservas(Connection conn){
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		try(PreparedStatement sql = conn.prepareStatement("SELECT * FROM reservas");
				ResultSet rs = sql.executeQuery()){
			while(rs.next()) {
				Reserva reserva = new Reserva(rs.getInt("numero_reserva"),rs.getInt("codigo_viaje"),rs.getInt("codigo_cliente"),
						rs.getInt("plazas_reservadas"),rs.getString("estado"));
				reservas.add(reserva);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return reservas;
	}
	
	// INSERTAR RESERVA - Inserta una RESERVA en la BBDD mediante los datos introducidos por parámetro
	public static int insertReserva(Connection conn, int codViaje, int codCliente, int plazas) {
		int tablas = 0;
		try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO reservas VALUES(NULL,?,?,?,?)")){
			stmt.setInt(1, codViaje);
			stmt.setInt(2, codCliente);
			stmt.setInt(3, plazas);
			String estado = "A";
			if(plazas > ViajeDAO.searchViaje(conn, codViaje).getPlazasDisp()) {
				estado = "E";
			}
			stmt.setString(4,estado);
			tablas = stmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return tablas;
	}
	
	// CANCELAR RESERVA - Cancela la RESERVA con el número de reserva y código de cliente pasado por parámetro
	public static boolean cancelarReserva(Connection conn, int numReserva, int codigoCliente) {
		boolean done = false;
		CallableStatement cstmt = null;
		try {
			String sql = "{CALL cancelar_reserva(?,?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, numReserva);
			cstmt.setInt(2, codigoCliente);
			done = cstmt.execute();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if (cstmt != null) cstmt.close();
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return done;
	}
}
