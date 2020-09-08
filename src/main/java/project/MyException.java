package project;

public class MyException extends Exception {
    private String message;
    private String codeException;
    MyException(String message){
        super();
        this.message = message;
    };
    MyException(String message, String codeException){
        super();
        this.message = message;
        this.codeException = codeException;
    };
    MyException(String message,  Exception exception){
        super(exception);
        this.message = message;

    };
    public String getMessage(){
        return this.message;
    };
}

