package prodcon;

public class Main 
{
	public static void main(String[] args) 
	{
		Data data = new Data();
		Productor prod = new Productor(data);
		Consumidor con = new Consumidor("Antonio",data);
		
		prod.start();
		con.start();
	}
}
