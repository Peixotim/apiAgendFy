// infra/exception/ValidationError.java
package marketing.digital.rj.apiagendfy.infra.exception;

public class ValidationError {
    public String field;
    public String message;

    public ValidationError() {}
    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}