package com.example.todoNew;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private PrintWriter writer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger() {
        try {

            FileWriter fileWriter = new FileWriter("log.txt", true);
            writer = new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {

        String formattedMessage = "[" + LocalDateTime.now().format(formatter) + "] " + message;
        writer.println(formattedMessage);
        writer.flush();
    }

    public void close() {
        writer.close();
    }
}
