package procesos;

import java.util.concurrent.Semaphore;

public class Pitufo extends Thread {
	
	public static final String[] PLATOS = {"Berenjenas rellenas","Fabada","Ajoblanco casero","Ensalada tropical"};
	public static final String[] POSTRES = {"Sandía","Helado","Café","Mousse de Chocolate","Tiramisú"};
	
	private static int pitufosComiendo;
	
	private String nombre;
	private Semaphore comedor;
	private String color;
	
	static {
		pitufosComiendo = 0;
	}
	
	public Pitufo(String nombre, Semaphore comedor, String color) {
		this.nombre = nombre;
		this.comedor = comedor;
		this.color = color;
	}
	
	@Override
	public void run() {
		try {
			
			// Intentar entrar en el Comedor
			System.out.printf("%s%s quiere entrar al Comedor ...\n",this.color,this.nombre);
			comedor.acquire();
			
			// Entrado en el Comedor
			pitufosComiendo++;
			System.out.printf("\u001B[37mCOMEDOR - Hay %d %s comiendo.\n\n", pitufosComiendo,pitufosComiendo == 1 ? "Pitufo" : "Pitufos");
			
			// COMER - Almuerzo
			System.out.printf("%s%s está comiendo %s para almozar ...\n",this.color,this.nombre,PLATOS[(int)(Math.random()*PLATOS.length)]);
			Thread.sleep((long) (Math.random()*5000 + 1000));
			
			// COMER - Postre
			System.out.printf("%s%s está comiendo %s como postre ...\n",this.color,this.nombre,POSTRES[(int)(Math.random()*POSTRES.length)]);
			Thread.sleep((long) (Math.random()*5000 + 1000));
			
			// Salir del Comedor
			System.out.printf("%s%s está saliendo del Comedor ...\n",this.color,this.nombre);
			pitufosComiendo--;
			System.out.printf("\u001B[37mCOMEDOR - Hay %d %s comiendo.\n\n", pitufosComiendo,pitufosComiendo == 1 ? "Pitufo" : "Pitufos");
			
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println(e.getMessage());
		}finally {
			comedor.release();
		}
	}
}
