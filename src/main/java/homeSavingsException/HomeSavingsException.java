package homeSavingsException;

public class HomeSavingsException extends Exception {
    private String message;
    private String codeException;
    private Exception exception;

    public HomeSavingsException(String message){
        this.message = message;
    }
    public HomeSavingsException(String message, String codeException){
        this.message = message;
        this.codeException = codeException;
    }
    public HomeSavingsException(String message, Exception exception){
        super(exception);
        this.message = message;
        this.exception = exception;

    }
    public String getMessage(){
        return this.message;
    }

    public Exception getException() {
        return exception;
    }
}

