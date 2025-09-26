package marketing.digital.rj.apiagendfy.infra.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(ApiErrorCode.NOT_FOUND, message, HttpStatus.NOT_FOUND);
    }
}