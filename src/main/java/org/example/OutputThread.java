package org.example;

import java.io.File;

import static org.example.Main.readFile;

public class OutputThread implements Runnable{
    public File file;

    public OutputThread(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        FileInformation fileContent = readFile(file);
        synchronized (Main.logWriter) {
            Main.logWriter.printf("Прочитали файл %s кол-во байт данных: %d, кол-во считанных символов: %d, контрольное число: %d, номер части: %d%n",
                    file.getName(), fileContent.k, fileContent.s.length(), fileContent.d, fileContent.p);
        }
        synchronized (Main.contents) {
            Main.contents.add(fileContent);
        }
    }
}
