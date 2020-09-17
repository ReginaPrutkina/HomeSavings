package project;

public class MyException extends Exception {
    private String message;
    private String codeException;
    private Exception exception;
    MyException(String message){
        super();
        this.message = message;
    }
    MyException(String message, String codeException){
        super();
        this.message = message;
        this.codeException = codeException;
    }
    MyException(String message,  Exception exception){
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

