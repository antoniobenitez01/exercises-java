package prodcon;

import java.util.ArrayList;

public class Almacen 
{
	public static final int LIMITE = 10;
	
	private ArrayList<String> packets = new ArrayList<String>();
	private boolean recieved = true;
	
	public int getPacketSize() {
		return this.packets.size();
	}
	
	public synchronized String recieve() {
		while(this.getPacketSize() == 0) {
			try {
				wait();
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
		recieved = true;
		String returnPacket = packets.get(0);
		notifyAll();
		packets.remove(0);
		System.out.printf("\u001B[36mSENDING - Pizza %s || PACKETS SIZE: %d\n",returnPacket,this.packets.size());
		return returnPacket;
	}
	
	public synchronized void send(String packet) {
		recieved = false;
		packets.add(packet);
		notifyAll();
		System.out.printf("\u001B[36mRECIEVED - Pizza %s || PACKETS SIZE: %d\n",packet,this.packets.size());
	}
}
