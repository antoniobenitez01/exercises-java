package saludos;

public class Personal extends Thread
{
	private static boolean jefeHere = false;
	
	private String nombre;
	private Saludo saludo;
	private boolean esJefe;
	
	public Personal(String nombre, Saludo saludo,boolean esJefe) {
		this.nombre = nombre;
		this.saludo = saludo;
		this.esJefe = esJefe;
	}
	
	@Override
	public void run() {
		System.out.printf("%s llegó.\n", this.nombre);
		if(esJefe) {
			synchronized(saludo) {
				saludo.notifyAll();
				jefeHere = true;
				System.out.printf("****** %s-: Buenos días empleados. ******\n",this.nombre.toUpperCase());
			}
		}else {
			synchronized(saludo) {
				try {
					while(!jefeHere) {
						saludo.wait();
					}
					System.out.printf("%s-: Buenos días jefe.\n",this.nombre.toUpperCase());
				}catch(InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
