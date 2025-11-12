package prodcon;

public class Consumidor extends Thread
{
	private String nombre;
	private Almacen buffer;
	
	public Consumidor(String nombre, Almacen buffer) {
		this.nombre = nombre;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		for(String recievedMessage = buffer.recieve();
				!"FIN".equals(recievedMessage);
				recievedMessage = buffer.recieve()) {
			System.out.printf("\u001B[31m%s va a comer Pizza %s\n",this.nombre,recievedMessage);
			try {
				Thread.sleep((long)(Math.random() * 5000 + 1000));
				if(this.buffer.getPacketSize() == 0) {
					System.out.printf("\u001B[31m%s sale de la Pizzería ...\n",this.nombre);
					break;
				}
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
	}
}
