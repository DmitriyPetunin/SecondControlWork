package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static String dir = "cw2";
    private static int v = 17;
    private static String resultLogFile = "logv" + v + ".log";
    private static String resultFile = "outputv" + v + ".txt";
    public static PrintWriter logWriter;
    public static List<FileInformation> contents = new ArrayList<>();

    public static void main(String[] args) {
        try {
            logWriter = new PrintWriter(new FileWriter(resultLogFile));

            File folder = new File(dir + "/v" + v);
            File[] listOfFiles = folder.listFiles();
            List<Thread> listOfThread = new ArrayList<>();
            for (File file : listOfFiles){
                Thread thread = new Thread(new OutputThread(file));
                thread.start();
                listOfThread.add(thread);
            }
            for (Thread thread : listOfThread){
                thread.join();
            }
            Collections.sort(contents, Comparator.comparingInt(f -> f.p));
            logWriter.flush();
            writeToFile();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeToFile() throws IOException {
        try(PrintWriter outputWriter = new PrintWriter(new FileWriter(resultFile))){
            for (FileInformation content : contents) {
                outputWriter.print(content.s);
            }
        }
        logWriter.close();
    }

    public static FileInformation readFile(File file) {
        FileInformation content = new FileInformation();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            content.k = dis.readInt();
            byte[] bytes = new byte[content.k];
            dis.readFully(bytes);
            content.s = new String(bytes, "UTF-8");
            content.d = dis.readInt();
            content.p = dis.readInt();
            if (content.s.length() != content.d) { // проверка на кол-во символов
                throw new IllegalStateException("Строка не совпадают с контрольным числом символов.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}