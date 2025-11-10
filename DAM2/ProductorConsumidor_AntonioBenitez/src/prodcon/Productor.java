package prodcon;

public class Productor extends Thread
{
	private static String[] comidas = {"Hamburguesa","Pizza","Kebab","Pasta","Patatas Fritas","Fin"};
	
	private Data buffer;
	
	public Productor(Data buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		for(String comida : comidas) {
			System.out.printf("\u001B[32mProductor quiere mandar %s\n", comida);
			buffer.send(comida);
			try {
				Thread.sleep((long)(Math.random()*5000 + 1000));
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
	}
}
