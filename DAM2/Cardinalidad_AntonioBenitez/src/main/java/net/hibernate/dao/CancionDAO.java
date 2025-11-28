package net.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.hibernate.entity.Cancion;
import net.hibernate.util.HibernateUtil;

public class CancionDAO 
{
	//SELECT CANCION - Devuelve todos las Canciones registradas en la BBDD
	public static List<Cancion> selectCancion() {
		List<Cancion> cantantes = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			cantantes = session.createQuery("from Cancion",Cancion.class).list();
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}
		return cantantes;
	}
	
	//UPDATE CANCION - Inserta o Actualiza la Canción introducida por parámetro en la BBDD
	public static void updateCancion(Cancion cancion) 
	{
		if(cancion != null) {
			Transaction transaction = null;
			try(Session session = HibernateUtil.getSessionFactory().openSession()){
				transaction = session.beginTransaction();
				session.merge(cancion);
				transaction.commit();
			}catch(Exception e ) {
				if(transaction != null) {
					transaction.rollback();
				}
				System.out.println(e.getMessage());
			}
		}else {
			System.out.println("ERROR: Canción introducido = NULL");
		}
	}
}
