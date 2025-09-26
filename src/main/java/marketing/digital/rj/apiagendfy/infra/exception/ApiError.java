package marketing.digital.rj.apiagendfy.infra.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    public int status;
    public String code;          // código de erro estável (ex.: USER_NOT_FOUND)
    public String message;       // mensagem amigável
    public String incidentId;    // UUID para rastrear no log
    public String path;          // request path
    public OffsetDateTime timestamp = OffsetDateTime.now();
    public List<ValidationError> errors; // lista de erros de validação (opcional)

    public static ApiError of(HttpStatus status, String code, String message, String incidentId, String path) {
        ApiError e = new ApiError();
        e.status = status.value();
        e.code = code;
        e.message = message;
        e.incidentId = incidentId;
        e.path = path;
        return e;
    }
}