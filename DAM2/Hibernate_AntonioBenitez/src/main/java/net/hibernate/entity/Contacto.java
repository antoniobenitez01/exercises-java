package net.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contacto")
public class Contacto 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "direccion")
	private String direccion;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "telefono")
	private int telefono;
	
	@Column(name = "altura")
	private double altura;
	
	public Contacto() {
		
	}
	
	public Contacto(String nombre, String direccion, String email, int telefono, double altura) {
		if(nombre.length() > 255) {
			throw new IllegalArgumentException("El campo Nombre no puede tener más de 255 caracteres.");
		}
		if(direccion.length() > 255) {
			throw new IllegalArgumentException("El campo Email no puede tener más de 255 caracteres.");
		}
		if(email.length() > 255) {
			throw new IllegalArgumentException("El campo Direccion no puede tener más de 255 caracteres.");
		}
		if(altura < 0) {
			throw new IllegalArgumentException("El campo Altura no puede ser menor que 0.");
		}
		this.nombre = nombre;
		this.direccion = direccion;
		this.email = email;
		this.telefono = telefono;
		this.altura = altura;
	}
	
	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if(nombre.length() > 255) {
			throw new IllegalArgumentException("El campo Nombre no puede tener más de 255 caracteres.");
		}
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		if(direccion.length() > 255) {
			throw new IllegalArgumentException("El campo Email no puede tener más de 255 caracteres.");
		}
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email.length() > 255) {
			throw new IllegalArgumentException("El campo Direccion no puede tener más de 255 caracteres.");
		}
		this.email = email;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		if(altura < 0) {
			throw new IllegalArgumentException("El campo Altura no puede ser menor que 0.");
		}
		this.altura = altura;
	}
	
	@Override
	public String toString() {
		return String.format("Contacto [ID = %d, Nombre = %s, Dirección = %s, EMail = %s, Telefono = %d, Altura = %.2fcm]",
				this.id,this.nombre,this.direccion,this.email,this.telefono,this.altura);
	}
}
