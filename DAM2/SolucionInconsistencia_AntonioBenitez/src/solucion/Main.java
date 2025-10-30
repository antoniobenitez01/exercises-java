package solucion;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
    	Ventas ventas = new Ventas();
    	Vendedor v1 = new Vendedor("Antonio",ventas);
    	Vendedor v2 = new Vendedor("María",ventas);
    	Vendedor v3 = new Vendedor("Jorge",ventas);
    	
    	long start = System.currentTimeMillis();
		System.out.println("SYSTEM START - CURRENT TIME MILLIS = " + start);
		
    	ArrayList<Thread> threads = new ArrayList<Thread>();
    	v1.start();
    	threads.add(v1);
    	v2.start();
    	threads.add(v2);
    	v3.start();
    	threads.add(v3);
    	for(Thread t : threads) {
    		try {
    			t.join();
    		}catch(InterruptedException e) {
    			System.out.println(e.getMessage());
    		}
    	}
        System.out.println("Entradas vendidas en total: " + ventas.getTicket());
        // CÁLCULO TIEMPO DE EJECUCIÓN --------------------
        long stop = System.currentTimeMillis();
        System.out.println("SYSTEM STOP - CURRENT TIME MILLIS = " + stop);
        System.out.println("CÁLCULO DE TIEMPO TOTAL EN SEGUNDOS = " + ((stop - start) / 1000) + " segundos");
    }
}
