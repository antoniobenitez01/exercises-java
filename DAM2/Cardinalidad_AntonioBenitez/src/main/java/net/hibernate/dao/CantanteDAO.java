package net.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.hibernate.entity.Cantante;
import net.hibernate.util.HibernateUtil;

public class CantanteDAO 
{
	//SELECT CANTANTE - Devuelve todos los Cantantes registrados en la BBDD
	public static List<Cantante> selectCantante() {
		List<Cantante> cantantes = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			cantantes = session.createQuery("from Cantante",Cantante.class).list();
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}
		return cantantes;
	}
	
	//UPDATE CANTANTE - Inserta o Actualiza el Cantante introducido por parámetro en la BBDD
	public static void updateCantante(Cantante cantante) 
	{
		if(cantante != null) {
			Transaction transaction = null;
			try(Session session = HibernateUtil.getSessionFactory().openSession()){
				transaction = session.beginTransaction();
				session.merge(cantante);
				transaction.commit();
			}catch(Exception e ) {
				if(transaction != null) {
					transaction.rollback();
				}
				System.out.println(e.getMessage());
			}
		}else {
			System.out.println("ERROR: Cantante introducido = NULL");
		}
	}
}
