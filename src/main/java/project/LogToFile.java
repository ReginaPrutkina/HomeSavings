package project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogToFile implements Logging {
    private String userName="USER";
    private String computerName;
    private String logFolder = ".";
    private String logFileName = "log.txt";
    private Path logFile;

    LogToFile(String userName){
        this.userName=userName;
        computerName=System.getenv("COMPUTERNAME");
        try { logFile = getLogFile();
        }catch (MyException ex) {
            ex.printStackTrace();
        }

    }
    LogToFile(String userName, String logFolder){
        this.userName=userName;
        computerName=System.getenv("COMPUTERNAME");
        this.logFolder = logFolder;
        try { logFile = getLogFile();
        }catch (MyException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void log(String description) {
        List<String> logList = new ArrayList<>();
        logList.add( "" + LocalDateTime.now());
        // берем из стека элемент массива [2] для вывода имени метода,
        // т.к. в [1] - текущий метод - logMethod
        StackTraceElement sTE = Thread.currentThread().getStackTrace()[2];
        logList.add("    "+sTE.getClassName() + " / " + sTE.getMethodName());
        logList.add("    "+description);
        logList.add("    "+"computerName: " + computerName + " user: " + userName);
        try {
            Files.write(logFile, logList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    };

    public void setLogFileName (String fileName){
        logFileName = fileName;
    }
    private Path getLogFile() throws MyException {
        Path logFile = Paths.get(logFolder+"\\"+ logFileName);
        if (!Files.exists(logFile) || !Files.isRegularFile(logFile))
            try {
                Files.createFile(logFile);
            }catch (IOException ex){
                throw new MyException(" Не удалось создать файл лога" + logFolder+"\\"+ logFileName,ex);

            }
        return logFile;
    }

  }
