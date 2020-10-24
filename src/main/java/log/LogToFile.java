package log;

import org.springframework.stereotype.Component;

import java.io.*;
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

    public LogToFile() {
        computerName=System.getenv("COMPUTERNAME");
       // logFile = getLogFile();
    }

    public LogToFile(String logFolder) {
        computerName=System.getenv("COMPUTERNAME");
        this.logFolder = logFolder;
      //  logFile = getLogFile();
    }

    public LogToFile(String userName, String logFolder) {
        this.userName=userName;
        computerName=System.getenv("COMPUTERNAME");
        this.logFolder = logFolder;
      //  logFile = getLogFile();
    }

    public String getUserName() {
        return userName;
    }

    @Override
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
    public void log(String description) {
        Path logFile = getLogFile();
        List<String> logList = new ArrayList<>();
        logList.add("" + LocalDateTime.now());
        // берем из стека элемент массива [2] для вывода имени метода,
        // т.к. в [1] - текущий метод - logMethod
        StackTraceElement sTE = Thread.currentThread().getStackTrace()[2];
        logList.add("    " + sTE.getClassName() + " / " + sTE.getMethodName());
        logList.add("    " + description);
        logList.add("    " + "computerName: " + computerName + " user: " + userName);
        try {
            Files.write(logFile, logList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        @Override
        public void log(String description, Exception exception) {
            Path logFile = getLogFile();
            List<String> logList = new ArrayList<>();
            logList.add( "" + LocalDateTime.now());
            // берем из стека элемент массива [2] для вывода имени метода,
            // т.к. в [1] - текущий метод - logMethod
            StackTraceElement sTE = Thread.currentThread().getStackTrace()[2];
            logList.add("    "+sTE.getClassName() + " / " + sTE.getMethodName());
            logList.add("    "+ description);
            logList.add("    "+"computerName: " + computerName + " user: " + userName);
            try {
                Files.write(logFile, logList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(logFile.toFile(), true)));
                exception.printStackTrace(pw);
                pw.close();
               } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    public void setLogFileName (String fileName){
        logFileName = fileName;
    }

    private Path getLogFile()  {
        Path logFile = Paths.get(logFolder+"\\"+ logFileName);
        if (!Files.exists(logFile) || !Files.isRegularFile(logFile))
            try {
                Files.createFile(logFile);
            }catch (IOException ex){
                ex.printStackTrace();
           }
        return logFile;
    }

  }
