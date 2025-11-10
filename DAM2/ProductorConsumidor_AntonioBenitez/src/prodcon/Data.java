package prodcon;

public class Data 
{
	private String packet;
	private boolean recieved = true;
	
	public synchronized String recieve() {
		while(recieved) {
			try {
				wait();
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
		recieved = true;
		String returnPacket = packet;
		notifyAll();
		System.out.printf("\u001B[36mRECIEVED - %s\n",packet);
		return returnPacket;
	}
	
	public synchronized void send(String packet) {
		while(!recieved) {
			try {
				wait();
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
		recieved = false;
		this.packet = packet;
		notifyAll();
		System.out.printf("\u001B[36mSENT - %s\n",packet);
	}
}
