package net.hibernate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.hibernate.dao.CancionDAO;
import net.hibernate.dao.CantanteDAO;
import net.hibernate.entity.Cancion;
import net.hibernate.entity.Cantante;
import net.hibernate.util.HibernateUtil;

public class Main 
{
	public static void main(String[] args) 
	{
		// ---	CREACIÓN CONTACTOS
		System.out.println("\u001B[32m\n=== CARDINALIDAD EN HIBERNATE ===\n");
		
		System.out.println("\u001B[36m--- Creando Cantantes ...\n\u001B[37m");
		List<Cantante> objetosCantante = Arrays.asList(
				new Cantante("Freddie Mercury","Inglaterra"),
				new Cantante("Michael Jackson","Estados Unidos"),
				new Cantante("Elton John","Inglaterra"),
				new Cantante("Prince","Estados Unidos"),
				new Cantante("Amy Winehouse","Inglaterra"));
		objetosCantante.forEach(cantante -> System.out.println(cantante));
		
		System.out.println("\u001B[36m\n--- Creando Canciones ...\n\u001B[37m");
		List<Cancion> objetosCancion = Arrays.asList(
				new Cancion("Bohemian Rhapsody",1975,objetosCantante.get(0)),
				new Cancion("Remember the Time",1991,objetosCantante.get(1)),
				new Cancion("I'm Still Standing",1983,objetosCantante.get(2)),
				new Cancion("Purple Rain",1984,objetosCantante.get(3)),
				new Cancion("Back to Black",2006,objetosCantante.get(4)));
		objetosCancion.forEach(cancion -> System.out.println(cancion));
		
		// ---	INSERTAR CONTACTOS EN BASE DE DATOS
		System.out.println("\u001B[36m\n--- Insertando Cantantes en BBDD ...\n\u001B[33m");
		Transaction transaction = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			for(Cantante cantante : objetosCantante) {
				session.persist(cantante);
			}
			for(Cancion cancion : objetosCancion) {
				session.persist(cancion);
			}
			transaction.commit();
		}catch(Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			System.out.println(e.getMessage());
		}
		
		// ---	MENÚ PRINCIPAL
		Scanner entrada = new Scanner(System.in);
		int respuesta = 0;
		List<Cantante> cantantes;
		List<Cancion> canciones;
		Cantante elegido = null;
		while(respuesta != 5) {
			
			// ---|	ACTUALIZACIÓN DATOS
			System.out.println("\u001B[36m\nActualizando datos ...\n\u001B[33m");
			cantantes = CantanteDAO.selectCantante();
			canciones = CancionDAO.selectCancion();
			
			// ---|	MENÚ PRINCIPAL
			respuesta = Common.menu("\u001B[32m\n=== MENÚ PRINCIPAL - Cardinalidad ===\n\u001B[37m"
					+ "\n1. Listado de Cantantes"
					+ "\n2. Consulta por Cantante"
					+ "\n3. INSERT Cantante"
					+ "\n4. INSERT Canción"
					+ "\n5. Apagar Programa",entrada,1,5);
			switch(respuesta) {
			case 1: // --- ---	LISTADO DE CANTANTES
				if(cantantes.size() > 0) {
					System.out.println("\u001B[32m\n--- Listado de Cantantes registrados ---\n\u001B[37m");
					cantantes.forEach(cantante -> System.out.println(cantante));
				}else {
					System.out.println("\u001B[31mERROR: No hay ningún Cantante registrado.\u001B[37m");
				}
				break;
			case 2: // --- ---	CONSULTA POR CANTANTE
				if(cantantes.size() > 0) {
					try {
						elegido = cantantes.get(elegirCantante(cantantes,entrada));
						if(elegido != null) {
							if(elegido.getCanciones().size() > 0) {
								System.out.printf("\u001B[32m\n- Canciones de %s -\n\n\u001B[37m",elegido.getNombre());
								for(Cancion cancion : elegido.getCanciones()) {
									System.out.println(cancion.toString());
								}
							}else {
								System.out.println("\u001B[31mERROR:El Cantante introducido no tiene Canciones registradas.\u001B[37m");
							}
						}else {
							System.out.println("\u001B[31mERROR: Cantante = NULL\u001B[37m");
						}
					}catch(IndexOutOfBoundsException e) {
						System.out.println("\u001B[31mERROR: Logic Error, too bad !\u001B[37m");
					}
				}else {
					System.out.println("\u001B[31mERROR: No hay ningún Cantante registrado.\u001B[37m");
				}
				break;
			case 3: // --- ---	INSERT CANTANTE
				System.out.println("\u001B[32m\n--- INSERT Cantante ---\u001B[37m");
				System.out.println("A continuación, introduzca los datos del nuevo Cantante.");
				
				System.out.println("Introduzca el Nombre del nuevo Cantante:");
				String nombre = entrada.nextLine();
				System.out.println("Introduzca el País del nuevo Cantante:");
				String pais = entrada.nextLine();
				try {
					System.out.println("\u001B[36m\nCreando nuevo Cantante ...\u001B[37m");
					Cantante nuevoCantante = new Cantante(nombre,pais);
					System.out.println(nuevoCantante.toString());
					System.out.println("\u001B[36m\nInsertando nuevo Cantante ...\u001B[33m");
					CantanteDAO.updateCantante(nuevoCantante);
				}catch(IllegalArgumentException e) {
					System.out.println("\u001B[31mERROR: " + e.getMessage() + "\u001B[37m");
				}
				break;
			case 4: // --- ---	INSERT CANCION
				if(cantantes.size() > 0) {
					System.out.println("\u001B[32m\n--- INSERT Canción ---\u001B[37m");
					System.out.println("A continuación, introduzca los datos de la nueva Canción.");
					
					System.out.println("Introduzca el Título de la nueva Canción:");
					String titulo = entrada.nextLine();
					int anyo = Common.inputInt("Introduzca el Año de la nueva Canción", entrada);
					try {
						Cantante cantanteCancion = cantantes.get(elegirCantante(cantantes,entrada));
						System.out.println("\u001B[36m\nCreando nueva Canción ...\u001B[37m");
						Cancion nuevaCancion = new Cancion(titulo,anyo,cantanteCancion);
						System.out.println(nuevaCancion.toString());
						System.out.println("\u001B[36m\nInsertando nueva Canción ...\u001B[33m");
						CancionDAO.updateCancion(nuevaCancion);
					}catch(IndexOutOfBoundsException e) {
						System.out.println("\u001B[31mERROR: Logic Error, too bad !\u001B[37m");
					}catch(IllegalArgumentException e) {
						System.out.println("\u001B[31mERROR: " + e.getMessage() + "\u001B[37m");
					}
				}else {
					System.out.println("\u001B[31mERROR: No hay ningún Cantante registrado.\u001B[37m");
				}
			case 5: // --- ---	APAGAR PROGRAMA
				System.out.println("\u001B[33mApagando programa ...");
				break;
			}
		}
		entrada.close(); // Cerrar Scanner
	}
	
	// ELEGIR CANTANTE - Devuelve el ID del Cantante elegido por teclado
	private static int elegirCantante(List<Cantante> cantantes,Scanner entrada) 
	{
		int elegido = 0;
		System.out.println("\u001B[32m\n--- Listado de Cantantes registrados ---\n\u001B[37m");
		cantantes.forEach(cantante -> System.out.println(cantante));
		while(elegido < 1 || elegido > cantantes.size()) {
			elegido = Common.inputInt("\u001B[37m\nElija la ID del Cantante a continuación.", entrada);
			if(elegido < 1 || elegido > cantantes.size()) {
				System.out.println("ERROR: ID Introducida no válida, inténtelo de nuevo.");
			}
		}
		return elegido - 1;
	}
}
