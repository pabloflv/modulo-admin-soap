package ar.com.unla.api.models.database;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ApiModel
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HorarioMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "idMateria")
    @ApiModelProperty(notes = "materia", required = true, position = 1)
    private Materia materia;

    @Column(nullable = false)
    @ApiModelProperty(notes = "horaDesde", required = true, example = "16:00", position = 2)
    private String horaDesde;

    @Column(nullable = false)
    @ApiModelProperty(notes = "horaHasta", required = true, example = "21:00", position = 3)
    private String horaHasta;

    @Column(nullable = false)
    @ApiModelProperty(notes = "horaDesde", required = true, example = "miercoles", position = 4)
    private String dia;

    public HorarioMateria() {
    }

    public HorarioMateria(Materia materia, String horaDesde, String horaHasta, String dia) {
        this.materia = materia;
        this.horaDesde = horaDesde;
        this.horaHasta = horaHasta;
        this.dia = dia;
    }
}
