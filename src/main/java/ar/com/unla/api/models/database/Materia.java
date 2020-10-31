package ar.com.unla.api.models.database;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ApiModel
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    @ApiModelProperty(notes = "nombre", required = true, example = "Sistemas distribuidos",
            position = 1)
    private String nombre;

    @Column(nullable = false)
    @ApiModelProperty(notes = "cuatrimestre", required = true, example = "1", position = 2)
    private Integer cuatrimestre;

    @Column(nullable = false)
    @ApiModelProperty(notes = "anio", required = true, example = "2020", position = 3)
    private Integer anioCarrera;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "idTurno")
    @ApiModelProperty(notes = "turno", position = 4)
    private Turno turno;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "idPeriodoInscripcion")
    @ApiModelProperty(notes = "periodoInscripcion", position = 5)
    private PeriodoInscripcion periodoInscripcion;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "materia", cascade = {CascadeType.MERGE,
            CascadeType.REFRESH})
    @Setter(AccessLevel.NONE)
    @ApiModelProperty(notes = "horarios", position = 6)
    private Set<HorarioMateria> horarios = new HashSet<>();

    public Materia() {
    }

    public Materia(String nombre, Integer cuatrimestre, Integer anioCarrera,
            Turno turno, PeriodoInscripcion periodoInscripcion) {
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
        this.anioCarrera = anioCarrera;
        this.turno = turno;
        this.periodoInscripcion = periodoInscripcion;
    }
}
