package net.hibernate.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "cantante")
public class Cantante 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "pais")
	private String pais;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "cantante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cancion> canciones;
	
	public Cantante() {
		
	}
	
	public Cantante(String nombre, String pais) {
		if(nombre.length() > 255) {
			throw new IllegalArgumentException("El campo Nombre no puede tener más de 255 caracteres.");
		}
		if(pais.length() > 255) {
			throw new IllegalArgumentException("El campo País no puede tener más de 255 caracteres.");
		}
		this.nombre = nombre;
		this.pais = pais;
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

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		if(pais.length() > 255) {
			throw new IllegalArgumentException("El campo País no puede tener más de 255 caracteres.");
		}
		this.pais = pais;
	}
	
	public List<Cancion> getCanciones(){
		return this.canciones;
	}
	
	@Override
	public String toString() {
		return String.format("Cantante [ID = %d, Nombre = %s, País = %s]",
				this.id,this.nombre,this.pais);
	}
	
	public String cancionesToString() {
		String toString = "";
		for(Cancion cancion : this.canciones) {
			toString += cancion.toString() + "\n";
		}
		return toString;
	}
}
