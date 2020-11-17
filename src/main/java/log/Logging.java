package log;

public interface Logging {
    void log(String description) ;
    void log(String description, Exception exception) ;
    void setUserName(String userName);
}
