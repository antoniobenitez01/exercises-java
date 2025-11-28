package net.hibernate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.hibernate.dao.CantanteDAO;
import net.hibernate.entity.Contacto;
import net.hibernate.util.HibernateUtil;

public class Main 
{
	public static void main(String[] args) 
	{
		// ---	CREACIÓN CONTACTOS
		System.out.println("\n=== AGENDA DE CONTACTOS ===\n");
		System.out.println("--- Creando Contactos ...\n");
		List<Contacto> contactos = Arrays.asList(
				new Contacto("Antonio","Calle Flores","antonio@email.com",111111111,1.78),
				new Contacto("María","Calle Mirarda","maria@email.com",222222222,1.61),
				new Contacto("Carlos","Calle Turmalina","carlos@email.com",333333333,1.56),
				new Contacto("Jorge","Calle Velazquez","jorge@email.com",444444444,1.89),
				new Contacto("Raquel","Calle Europa","raquel@email.com",555555555,1.87)
				);
		contactos.forEach(contacto -> System.out.println(contacto));
		
		// ---	INSERTAR CONTACTOS EN BASE DE DATOS
		System.out.println("\n--- Insertando Contactos en BBDD ...\n");
		Transaction transaction = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			for(Contacto contacto : contactos) {
				session.persist(contacto);
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
		int respuesta = 0, respuesta2 = 0, choice = 0;
		List<Contacto> agenda;
		while(respuesta != 5) {
			System.out.println("\nActualizando datos ...");
			agenda = CantanteDAO.selectContacto();
			respuesta = Common.menu("\n=== MENÚ PRINCIPAL CRUD ===\n"
					+ "\n1. SELECT Contacto"
					+ "\n2. INSERT Contacto"
					+ "\n3. UPDATE Contacto"
					+ "\n4. DELETE Contacto"
					+ "\n5. Apagar Programa",entrada,1,5);
			switch(respuesta) {
			case 1: // --- ---	MOSTRAR CONTACTOS
				System.out.println("\n--- Mostrando Contactos en Agenda ...\n");
				agenda.forEach(contacto -> System.out.println(contacto));
				break;
			case 2: // --- ---	INSERTAR CONTACTO
				System.out.println("\n--- Insertando nuevo Contacto ...");
				System.out.println("A continuación, introduzca los datos del nuevo Contacto.");
				
				System.out.println("Introduzca el Nombre del nuevo Contacto:");
				String nombre = entrada.nextLine();
				System.out.println("Introduzca la Dirección del nuevo Contacto:");
				String direccion = entrada.nextLine();
				System.out.println("Introduzca el EMail del nuevo Contacto:");
				String email = entrada.nextLine();
				int telefono = Common.inputInt("Introduzca el Teléfono del nuevo Contacto:",entrada);
				double altura = Common.inputDouble("Introduzca la Altura del nuevo Contacto", entrada);
				try {
					Contacto nuevoContacto = new Contacto(nombre,direccion,email,telefono,altura);
					CantanteDAO.updateContacto(nuevoContacto);
				}catch(IllegalArgumentException e) {
					System.out.println("ERROR: " + e.getMessage());
				}
				break;
			case 3: // --- ---	UPDATE CONTACTO
				for(Contacto contacto : agenda) {
					System.out.printf("%d. %s\n", agenda.indexOf(contacto) + 1,contacto);
				}
				choice = Common.inputInt("Elija el Contacto a Actualizar", entrada);
				// --- --- ---	Menú UPDATE
				try {
					Contacto updateContacto = agenda.get(choice-1);
					respuesta2 = 0;
					while((respuesta2 != 7) && (respuesta2 != 6)) {
						System.out.println("Contacto = " + updateContacto);
						respuesta2 = Common.menu("\n--- UPDATE ---\n"
								+ "\n1. Nombre\n2. Dirección\n3. EMail\n4. Teléfono\n5. Altura"
								+ "\n6. Confirmar Cambios\n7. Cancelar Operación",entrada,1,7);
						switch(respuesta2) {
						case 1: // --- --- ---	UPDATE Nombre
							System.out.println("Introduzca el Nombre actualizado.");
							try {
								updateContacto.setNombre(entrada.nextLine());
							}catch(IllegalArgumentException e) { System.out.println(e.getMessage());}
							break;
						case 2: // --- --- ---	UPDATE Direccion
							System.out.println("Introduzca la Dirección actualizada.");
							try {
								updateContacto.setDireccion(entrada.nextLine());
							}catch(IllegalArgumentException e) { System.out.println(e.getMessage());}
							break;
						case 3: // --- --- ---	UPDATE Email
							System.out.println("Introduzca el EMail actualizada.");
							try {
								updateContacto.setEmail(entrada.nextLine());
							}catch(IllegalArgumentException e) { System.out.println(e.getMessage());}
							break;
						case 4: // --- --- ---	UPDATE Telefono
							try {
								updateContacto.setTelefono(Common.inputInt("Introduzca el Teléfono actualizado.", entrada));
							}catch(IllegalArgumentException e) { System.out.println(e.getMessage());}
							break;
						case 5: // --- --- ---	UPDATE Altura
							try {
								updateContacto.setAltura(Common.inputDouble("Introduzca la Altura actualizada.", entrada));
							}catch(IllegalArgumentException e) { System.out.println(e.getMessage());}
							break;
						case 6: // --- --- ---	Confirmar Cambios
							CantanteDAO.updateContacto(updateContacto);
							break;
						case 7: // --- --- ---	Cancelar Operación
							System.out.println("Operación cancelada. Volviendo al Menú Principal ...");
							break;
						}
					}
				}catch(IndexOutOfBoundsException e) { System.out.println("ERROR: El Contacto elegido no existe."); }
				break;
			case 4: // --- ---	DELETE CONTACTO
				for(Contacto contacto : agenda) {
					System.out.printf("%d. %s\n", agenda.indexOf(contacto) + 1,contacto);
				}
				choice = Common.inputInt("Elija el Contacto a Actualizar", entrada);
				try {
					Contacto deleteContacto = agenda.get(choice-1);
					System.out.println("Contacto Elegido = " + deleteContacto);
					boolean toDelete = Common.booleanCheck("Está seguro de que desea eliminar este Contacto? (SI/NO)",entrada);
					if(toDelete) {
						CantanteDAO.deleteContacto(deleteContacto);
					}else {
						System.out.println("Operación cancelada. Volviendo al Menú Principal ...");
					}
				}catch(IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
				break;
			case 5: // --- ---	APAGAR PROGRAMA
				System.out.println("Apagando programa ...");
				break;
			}
		}
		entrada.close(); // Cerrar Scanner
	}
}
