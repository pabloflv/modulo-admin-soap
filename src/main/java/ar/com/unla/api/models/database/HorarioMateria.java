package ar.com.unla.api.models.database;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ApiModel
@EqualsAndHashCode
public class HorarioMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    @ApiModelProperty(notes = "horaDesde", required = true, example = "16:00", position = 2)
    private String horaDesde;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    @ApiModelProperty(notes = "horaHasta", required = true, example = "21:00", position = 3)
    private String horaHasta;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    @ApiModelProperty(notes = "dia", required = true, example = "miercoles", position = 4)
    private String dia;

    public HorarioMateria() {
    }

    public HorarioMateria(/*Materia materia,*/ String horaDesde, String horaHasta, String dia) {
        this.horaDesde = horaDesde;
        this.horaHasta = horaHasta;
        this.dia = dia;
    }
}
