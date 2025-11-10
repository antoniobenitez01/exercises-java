package bancaria;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
	public static void main(String[] args) 
	{
		// VARIABLES ------------------------------||
		Properties props = new Properties();
		PreparedStatement stmt;
		ResultSet rs;
		double cantidad = 2000;
		// PROGRAMA -------------------------------||
		try {
			props.load(new FileInputStream("mysql.cnf"));
			String url = String.format("jdbc:mysql://%s:%s/%s",
							props.getProperty("ip"),props.getProperty("port"),props.getProperty("dbname"));
			String user = "bancaria";
			String password = "123";
			Connection conn = Common.connectionSQL(url,user,password);
			if(conn != null) 
			{
				System.out.println("\n	--- TRANSFERENCIAS BANCARIAS ---");
				try {
					conn.setAutoCommit(false);
					
					System.out.println("   Transferencia Nº1 Restar cantidad a Cuenta A\n");
					stmt = conn.prepareStatement("UPDATE cuentas SET saldo = saldo - ? WHERE id_cuenta = 'A1001'");
					stmt.setDouble(1, cantidad);
					stmt.executeUpdate();
					
					stmt = conn.prepareStatement("SELECT * FROM cuentas WHERE id_cuenta = 'A1001'");
					rs = stmt.executeQuery();
					while(rs.next()) {
						System.out.println(String.format("ID_CUENTA = %s\nNOMBRE = %s\nSALDO = %.2f€\n",
								rs.getString("id_cuenta"),rs.getString("nombre_cliente"),rs.getDouble("saldo")));
					}
					
					System.out.println("   Transferencia Nº2 Sumar cantidad a Cuenta B\n");
					stmt = conn.prepareStatement("UPDATE cuentas SET saldo = saldo + ? WHERE id_cuenta = 'B2002'");
					stmt.setDouble(1, cantidad);
					stmt.executeUpdate();
					
					stmt = conn.prepareStatement("SELECT * FROM cuentas WHERE id_cuenta = 'B2002'");
					rs = stmt.executeQuery();
					while(rs.next()) {
						System.out.println(String.format("ID_CUENTA = %s\nNOMBRE = %s\nSALDO = %.2f€\n",
								rs.getString("id_cuenta"),rs.getString("nombre_cliente"),rs.getDouble("saldo")));
					}
					
					conn.commit();
				}catch (SQLException sqle) { // CATCH (SQL EXCEPTION) --------------
					try {
						conn.rollback();
						System.out.printf("Transacción revertida debido a un error:\t%s\n",sqle.getMessage());
					} catch (SQLException rollbackEx) {
						System.out.println(rollbackEx.getMessage());
					}
				} finally { // FINALLY ----------------------------------------------
					try {
						conn.setAutoCommit(true);
						conn.close();
					} catch (SQLException sqle2) {
						System.out.println(sqle2.getMessage());
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
	}
}
