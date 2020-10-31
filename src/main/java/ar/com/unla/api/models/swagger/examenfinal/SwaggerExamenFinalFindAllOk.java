package ar.com.unla.api.models.swagger.examenfinal;

import ar.com.unla.api.models.database.ExamenFinal;
import java.util.List;
import lombok.Data;

/**
 * Provides an OK structure response for Swagger (Avoiding to show the errors attribute)
 */
@Data
public final class SwaggerExamenFinalFindAllOk {

    private List<ExamenFinal> data;

}
