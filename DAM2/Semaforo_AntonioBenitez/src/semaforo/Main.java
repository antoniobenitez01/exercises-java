package semaforo;

import java.util.concurrent.Semaphore;

public class Main 
{
	public static void main(String[] args) 
	{
		String[] nombres = {"Antonio","Pepe","Marcos","Jorge","María","Laura","Marta","Lucas","Mario","Pedro"};
		Persona[] personas = new Persona[10];
		Semaphore bano = new Semaphore(3);
		for(int i=0; i<personas.length;i++) {
			personas[i] = new Persona(nombres[i],bano);
		}
		for(int i=0; i<personas.length;i++) {
			personas[i].start();
		}
	}
}
