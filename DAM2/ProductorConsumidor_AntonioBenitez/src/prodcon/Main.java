package prodcon;

import java.util.ArrayList;

public class Main 
{
	public static void main(String[] args) 
	{
		String[] pizzas1 = {"Margarita","Kebab","Barbacoa","Carbonara","Napolitana","Roquefort","Mixta"};
		String[] pizzas2 = {"Salami","Hawaii","Banger","Pepperoni","4 Quesos","Italiana","Tuto Carne"};

		Almacen data = new Almacen();
		Productor prod1 = new Productor("Productor 1",data,pizzas1);
		Productor prod2 = new Productor("Productor 2",data,pizzas2);
		Consumidor con1 = new Consumidor("Antonio",data);
		Consumidor con2 = new Consumidor("María",data);
		Consumidor con3 = new Consumidor("Carlos",data);
		
		long start = System.currentTimeMillis();
		System.out.println("SYSTEM START - CURRENT TIME MILLIS = " + start);
		System.out.println("Abriendo Pizzería ...\n");
		
    	ArrayList<Thread> threads = new ArrayList<Thread>();
    	prod1.start();
    	threads.add(prod1);
    	prod2.start();
    	threads.add(prod2);
    	con1.start();
    	threads.add(con1);
    	con2.start();
    	threads.add(con2);
    	con3.start();
    	threads.add(con3);
    	for(Thread t : threads) {
    		try {
    			t.join();
    		}catch(InterruptedException e) {
    			System.out.println(e.getMessage());
    		}
    	}
    	System.out.println("\u001B[37m\nCerrando Pizzería ...");
        // CÁLCULO TIEMPO DE EJECUCIÓN --------------------
        long stop = System.currentTimeMillis();
        System.out.println("SYSTEM STOP - CURRENT TIME MILLIS = " + stop);
        System.out.println("CÁLCULO DE TIEMPO TOTAL EN SEGUNDOS = " + ((stop - start) / 1000) + " segundos");
	}
}
