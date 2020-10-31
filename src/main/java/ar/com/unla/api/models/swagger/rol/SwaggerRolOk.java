package ar.com.unla.api.models.swagger.rol;

import ar.com.unla.api.models.database.Rol;
import lombok.Data;

/**
 * Provides an OK structure response for Swagger (Avoiding to show the errors attribute)
 */
@Data
public final class SwaggerRolOk {

    private Rol data;

}
