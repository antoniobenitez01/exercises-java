package net.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.hibernate.entity.Contacto;
import net.hibernate.util.HibernateUtil;

public class ContactoDAO 
{
	//SELECT CONTACTO - Devuelve todos los Contactos registrados en la BBDD
	public static List<Contacto> selectContacto() {
		List<Contacto> agenda = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			agenda = session.createQuery("from Contacto",Contacto.class).list();
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}
		return agenda;
	}
	
	//UPDATE CONTACTO - Inserta o Actualiza el Contacto introducido por parámetro en la BBDD
	public static void updateContacto(Contacto contacto) 
	{
		if(contacto != null) {
			Transaction transaction = null;
			try(Session session = HibernateUtil.getSessionFactory().openSession()){
				transaction = session.beginTransaction();
				session.merge(contacto);
				transaction.commit();
			}catch(Exception e ) {
				if(transaction != null) {
					transaction.rollback();
				}
				System.out.println(e.getMessage());
			}
		}else {
			System.out.println("ERROR: Contacto introducido = NULL");
		}
	}
	
	//DELETE CONTACTO - Borra el Contacto introducido por parámetro de la BBDD
	public static void deleteContacto(Contacto contacto) 
	{
		if(contacto != null) {
			Transaction transaction = null;
			try(Session session = HibernateUtil.getSessionFactory().openSession()){
				transaction = session.beginTransaction();
				session.remove(contacto);
				System.out.println("Contacto eliminado correctamente.");
				transaction.commit();
			}catch(Exception e ) {
				if(transaction != null) {
					transaction.rollback();
				}
				System.out.println(e.getMessage());
			}
		}else {
			System.out.println("ERROR: Contacto introducido = NULL");
		}
	}
}
