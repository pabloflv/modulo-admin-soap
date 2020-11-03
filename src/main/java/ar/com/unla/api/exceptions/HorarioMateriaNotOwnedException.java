package ar.com.unla.api.exceptions;

import ar.com.unla.api.constants.CommonsErrorConstants;
import org.springframework.http.HttpStatus;

public class HorarioMateriaNotOwnedException extends GeneralApiException {

    public HorarioMateriaNotOwnedException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return CommonsErrorConstants.NOT_OWNED_ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
