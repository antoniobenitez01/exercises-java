package prodcon;

public class Productor extends Thread
{
	private String nombre;
	private Almacen buffer;
	private String[] pizzas;
	
	public Productor(String nombre, Almacen buffer, String[] pizzas) {
		this.nombre = nombre;
		this.buffer = buffer;
		this.pizzas = pizzas;
	}
	
	@Override
	public void run() {
		for(String pizza : pizzas) {
			while(buffer.getPacketSize() >= Almacen.LIMITE) {
				try {
					Thread.sleep((long)(Math.random()*5));
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println(e.getMessage());
				}
			}
			System.out.printf("\u001B[32m%s quiere mandar una Pizza %s al estante\n",this.nombre, pizza);
			buffer.send(pizza);
			try {
				Thread.sleep((long)(Math.random()*1000+100));
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
	}
}
