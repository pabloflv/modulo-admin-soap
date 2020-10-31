package ar.com.unla.api.models.swagger.horariomateria;

import ar.com.unla.api.models.database.HorarioMateria;
import java.util.List;
import lombok.Data;

/**
 * Provides an OK structure response for Swagger (Avoiding to show the errors attribute)
 */
@Data
public final class SwaggerHorarioMateriaFindAllOk {

    private List<HorarioMateria> data;

}
