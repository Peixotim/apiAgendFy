package marketing.digital.rj.apiagendfy.infra.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final ApiErrorCode code;

    public BusinessException(ApiErrorCode code, String message) {
        this(code, message, HttpStatus.UNPROCESSABLE_ENTITY); // 422 padrão
    }

    public BusinessException(ApiErrorCode code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ApiErrorCode getCode() {
        return code;
    }
}