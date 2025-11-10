package prodcon;

public class Consumidor extends Thread
{
	private String nombre;
	private Data buffer;
	
	public Consumidor(String nombre, Data buffer) {
		this.nombre = nombre;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		for(String recievedMessage = buffer.recieve();
				!"Fin".equals(recievedMessage);
				recievedMessage = buffer.recieve()) {
			System.out.printf("\u001B[31m%s va a comer %s\n",this.nombre,recievedMessage);
			try {
				Thread.sleep((long)(Math.random()*5000 + 1000));
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
	}
}
