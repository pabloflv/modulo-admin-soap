package ar.com.unla.api.models.database;

import ar.com.unla.api.constants.CommonsErrorConstants;
import ar.com.unla.api.exceptions.HorarioMateriaAlreadyOwnedException;
import ar.com.unla.api.exceptions.HorarioMateriaNotOwnedException;
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

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "idProfesor")
    @ApiModelProperty(notes = "profesor", position = 2)
    private Usuario profesor;

    @Column(nullable = false)
    @ApiModelProperty(notes = "cuatrimestre", required = true, example = "1", position = 3)
    private Integer cuatrimestre;

    @Column(nullable = false)
    @ApiModelProperty(notes = "anio", required = true, example = "2020", position = 4)
    private Integer anioCarrera;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "idTurno")
    @ApiModelProperty(notes = "turno", position = 5)
    private Turno turno;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "idPeriodoInscripcion")
    @ApiModelProperty(notes = "periodoInscripcion", position = 6)
    private PeriodoInscripcion periodoInscripcion;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @Setter(AccessLevel.NONE)
    @ApiModelProperty(notes = "horarios", position = 7)
    private Set<HorarioMateria> horarios = new HashSet<>();

    public Materia() {
    }

    public Materia(String nombre, Usuario profesor, Integer cuatrimestre, Integer anioCarrera,
            Turno turno, PeriodoInscripcion periodoInscripcion) {
        this.nombre = nombre;
        this.profesor = profesor;
        this.cuatrimestre = cuatrimestre;
        this.anioCarrera = anioCarrera;
        this.turno = turno;
        this.periodoInscripcion = periodoInscripcion;
    }

    public void addHourHand(HorarioMateria horarioMateria)
            throws HorarioMateriaAlreadyOwnedException {
        if (horarios.contains(horarioMateria)) {
            throw new HorarioMateriaAlreadyOwnedException(
                    CommonsErrorConstants.ALREADY_OWNED_ERROR_MESSAGE);
        }
        horarios.add(horarioMateria);
    }

    public void removeHourHand(HorarioMateria horarioMateria)
            throws HorarioMateriaNotOwnedException {
        if (!horarios.contains(horarioMateria)) {
            throw new HorarioMateriaNotOwnedException(
                    CommonsErrorConstants.NOT_OWNED_ERROR_MESSAGE);
        }
        horarios.remove(horarioMateria);
    }
}
