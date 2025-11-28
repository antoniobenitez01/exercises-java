package net.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cancion")
public class Cancion 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "anyo")
	private int anyo;
	
	@ManyToOne()
    @JoinColumn(name = "id_cantante")
    private Cantante cantante;
	
	public Cancion() {
		
	}
	
	public Cancion(String titulo, int anyo, Cantante cantante) {
		if(titulo.length() > 255) {
			throw new IllegalArgumentException("El campo Título no puede tener más de 255 caracteres.");
		}
		if(cantante == null) {
			throw new IllegalArgumentException("El campo Cantante no puede ser nulo.");
		}
		this.titulo = titulo;
		this.anyo = anyo;
		this.cantante = cantante;
	}
	
	public int getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String nombre) {
		if(nombre.length() > 255) {
			throw new IllegalArgumentException("El campo Título no puede tener más de 255 caracteres.");
		}
		this.titulo = nombre;
	}
	
	public int getAnyo() {
		return this.anyo;
	}
	
	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}
	
	public Cantante getCantante() {
		return this.cantante;
	}
	
	public void setCantante(Cantante cantante) {
		if(cantante == null) {
			throw new IllegalArgumentException("El campo Cantante no puede ser nulo.");
		}
		this.cantante = cantante;
	}
	
	@Override
	public String toString() {
		return String.format("Canción [ID = %d, Título = %s, Año = %d, Cantante = %s]",
				this.id,this.titulo,this.anyo,this.cantante.getNombre());
	}
}
