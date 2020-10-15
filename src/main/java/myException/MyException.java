package myException;

public class MyException extends Exception {
    private String message;
    private String codeException;
    private Exception exception;

    public MyException(String message){
        super();
        this.message = message;
    }
    public MyException(String message, String codeException){
        super();
        this.message = message;
        this.codeException = codeException;
    }
    public MyException(String message, Exception exception){
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

