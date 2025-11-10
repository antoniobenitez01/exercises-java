package semaforo;

import java.util.concurrent.Semaphore;

public class Persona extends Thread
{
	private static String[] colores = {"\u001B[31m","\u001B[32m","\u001B[37m"};
	private String nombre;
	private Semaphore bano;
	
	public Persona(String nombre, Semaphore bano) {
		this.nombre = nombre;
		this.bano = bano;	
	}
	
	@Override
	public void run() {
		try {
			System.out.printf("%s%s quiere usar el baño.\n",colores[2],this.nombre);
			bano.acquire();
			System.out.printf("%s%s está usando el baño ...\n",colores[0],this.nombre);
			Thread.sleep((long) (Math.random()*5000 + 1000));
			System.out.printf("%s%s ha dejado de usar el baño.\n",colores[1],this.nombre);
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println(e.getMessage());
		}finally {
			bano.release();
		}
	}
}
