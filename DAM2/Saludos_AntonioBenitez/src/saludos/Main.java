package saludos;

public class Main 
{
	public static void main(String[] args) 
	{
		String string = "Hola";
		Saludo s = new Saludo();
		Personal Empleado1 = new Personal("Pepe",string,false);
		Personal Empleado2 = new Personal("José",string,false);
		Personal Empleado3 = new Personal("Pedro",string,false);
		Personal Jefe1 = new Personal("Jefe",string,true);
		Empleado1.start();           
		Empleado2.start();           
		Empleado3.start();           
		Jefe1.start();
	}
}
