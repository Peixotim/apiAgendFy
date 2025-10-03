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


    public BusinessException(String message) {
        super(message);
        this.code = ApiErrorCode.BUSINESS_RULE;   // defina um default sensato
        this.status = HttpStatus.BAD_REQUEST;     // ou outro default
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ApiErrorCode getCode() {
        return code;
    }
}