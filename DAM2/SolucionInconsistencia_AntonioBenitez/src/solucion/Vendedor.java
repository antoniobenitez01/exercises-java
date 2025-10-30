package solucion;

class Vendedor extends Thread {
	private String nombre;
    Ventas ventas;

    Vendedor(String nombre, Ventas ventas) {
    	this.nombre = nombre;
        this.ventas = ventas;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
        	int ticket = ventas.vender();
        	if (ticket != -1) {
        		System.out.printf("%s - Vendiendo Ticket Nº%d\n",this.nombre,ticket);
        	}
        }
    }
}
