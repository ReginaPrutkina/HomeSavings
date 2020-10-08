package module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogToFile implements Logging {

    private String userName;

    private String computerName;

    private String logFolder;

    private String logFileName;

    private Path logFile;

    LogToFile() throws IOException{
        computerName=System.getenv("COMPUTERNAME");
        logFile = getLogFile();
    }

    LogToFile(String logFolder) throws IOException{
        computerName=System.getenv("COMPUTERNAME");
        this.logFolder = logFolder;
        logFile = getLogFile();
    }

    LogToFile(String userName, String logFolder) throws IOException{
        this.userName=userName;
        computerName=System.getenv("COMPUTERNAME");
        this.logFolder = logFolder;
        logFile = getLogFile();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getLogFolder() {
        return logFolder;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFile(Path logFile) {
        this.logFile = logFile;
    }

    @Override
    public void log(String description) throws IOException{
        List<String> logList = new ArrayList<>();
        logList.add( "" + LocalDateTime.now());
        // берем из стека элемент массива [2] для вывода имени метода,
        // т.к. в [1] - текущий метод - logMethod
        StackTraceElement sTE = Thread.currentThread().getStackTrace()[2];
        logList.add("    "+sTE.getClassName() + " / " + sTE.getMethodName());
        logList.add("    "+description);
        logList.add("    "+"computerName: " + computerName + " user: " + userName);
 //       try {
            Files.write(logFile, logList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
 //       } catch (IOException ex) {
 //           ex.printStackTrace();
 //       }
    }

    public void setLogFileName (String fileName){
        logFileName = fileName;
    }

    private Path getLogFile() throws IOException {
        Path logFile = Paths.get(logFolder+"\\"+ logFileName);
        if (!Files.exists(logFile) || !Files.isRegularFile(logFile))
 //           try {
                Files.createFile(logFile);
//            }catch (IOException ex){
 //               throw new MyException(" Не удалось создать файл лога" + logFolder+"\\"+ logFileName,ex);

 //          }
        return logFile;
    }

  }