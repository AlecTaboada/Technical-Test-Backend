package es.softtek.calificaciones.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notas")
public class Nota {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "asignatura")
	private String asignatura;

	@Column(name = "description")
	private String description;

	@Column(name = "username")
	private String username;

	@Column(name = "calificacion")
	private Integer calificacion;
	public Nota(String asignatura, String description, String username, Integer calificacion) {
		this.asignatura = asignatura;
		this.description = description;
		this.username = username;
		this.calificacion = calificacion;
	}

}
