// infra/exception/GlobalExceptionHandler.java
package marketing.digital.rj.apiagendfy.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // --- Helpers ---
    private String newIncidentId() { return UUID.randomUUID().toString(); }

    private ResponseEntity<ApiError> build(HttpStatus status, ApiErrorCode code, String message,
                                           String incidentId, HttpServletRequest req,
                                           List<ValidationError> fieldErrors) {

        ApiError body = ApiError.of(status, code.name(), message, incidentId, req.getRequestURI());
        body.errors = (fieldErrors == null || fieldErrors.isEmpty()) ? null : fieldErrors;
        return ResponseEntity.status(status).body(body);
    }

    // --- 422 | Business rules ---
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
        String id = newIncidentId();
        // Business: loga em nível WARN
        log.warn("Business error [{}]: {} - {}", id, ex.getCode(), ex.getMessage());
        return build(ex.getStatus(), ex.getCode(), ex.getMessage(), id, req, null);
    }

    // --- 404 | Not found ---
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        String id = newIncidentId();
        log.warn("NotFound [{}]: {}", id, ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ApiErrorCode.NOT_FOUND, ex.getMessage(), id, req, null);
    }

    // --- 400 | Validação @Valid (body) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest req) {
        String id = newIncidentId();
        List<ValidationError> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new ValidationError(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());
        log.warn("Validation error [{}]: {} fields invalid", id, fields.size());
        return build(HttpStatus.BAD_REQUEST, ApiErrorCode.VALIDATION_ERROR,
                "Dados inválidos. Verifique os campos.", id, req, fields);
    }

    // --- 400 | Validação em params/path ---
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex,
                                                              HttpServletRequest req) {
        String id = newIncidentId();
        List<ValidationError> fields = ex.getConstraintViolations().stream()
                .map(v -> new ValidationError(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());
        log.warn("Constraint violation [{}]: {}", id, fields.size());
        return build(HttpStatus.BAD_REQUEST, ApiErrorCode.VALIDATION_ERROR,
                "Parâmetros inválidos.", id, req, fields);
    }

    // --- 400 comuns ---
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        String id = newIncidentId();
        log.warn("Bad request [{}]: {}", id, ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ApiErrorCode.BAD_REQUEST,
                "Requisição inválida.", id, req, null);
    }

    // --- 401/403 (segurança) ---
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        String id = newIncidentId();
        log.warn("Access denied [{}]: {}", id, ex.getMessage());
        return build(HttpStatus.FORBIDDEN, ApiErrorCode.ACCESS_DENIED,
                "Você não tem permissão para acessar este recurso.", id, req, null);
    }

    // --- 405/415 ---
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class
    })
    public ResponseEntity<ApiError> handleMethodOrMedia(Exception ex, HttpServletRequest req) {
        String id = newIncidentId();
        log.warn("Method/Media error [{}]: {}", id, ex.getMessage());
        HttpStatus st = (ex instanceof HttpRequestMethodNotSupportedException)
                ? HttpStatus.METHOD_NOT_ALLOWED : HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        ApiErrorCode code = (ex instanceof HttpRequestMethodNotSupportedException)
                ? ApiErrorCode.BAD_REQUEST : ApiErrorCode.BAD_REQUEST;
        return build(st, code, ex.getMessage(), id, req, null);
    }

    // --- 500 | fallback ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUncaught(Exception ex, HttpServletRequest req) {
        String id = newIncidentId();
        // 500: loga em nível ERROR com stacktrace
        log.error("Internal error [{}]: {}", id, ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ApiErrorCode.INTERNAL_ERROR,
                "Erro interno no servidor.", id, req, null);
    }
}