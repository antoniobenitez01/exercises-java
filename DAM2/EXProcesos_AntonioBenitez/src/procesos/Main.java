package procesos;

import java.util.concurrent.Semaphore;

public class Main 
{
	public static void main(String[] args) 
	{
		
		System.out.println("\n=== EL COMEDOR DE LOS PITUFOS ===\n");
		
		String[] colores = {"\u001B[31m","\u001B[32m","\u001B[36m","\u001B[35m","\u001B[33m","\u001B[29m"};
		String[] nombres = {"Papa Pitufo","Pitufina","Filósofo","Pintor","Gruñon","Bromista","Dormilón","Tímido","Bonachón","Romántico"};
		Pitufo[] pitufos = new Pitufo[10];
		Semaphore comedor = new Semaphore(3);
		for(int i=0; i<pitufos.length;i++) {
			pitufos[i] = new Pitufo(nombres[i],comedor,colores[(int)(Math.random() * colores.length)]);
		}
		for(Pitufo p : pitufos) {
			p.start();
		}
	}
}
