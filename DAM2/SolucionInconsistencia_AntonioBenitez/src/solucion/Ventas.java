package solucion;

class Ventas {
    private int ventas = 30;
    private int ticket = 0;
    
    public int vender() {
        synchronized(this) {
        	if(ventas > 0) {
        		ventas--;
        		ticket++;
        		return ticket;
        	}else {
            	return -1;
        	}
        }
    }

    public synchronized int getVentas() {
        return ventas;
    }
    
    public synchronized int getTicket() {
    	return this.ticket;
    }
}
