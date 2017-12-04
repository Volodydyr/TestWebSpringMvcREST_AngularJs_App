package com.backend.thread;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Data
public class ProviderThread extends Thread {
    private MultipartFile file;
    private String fileName;
    private WorkerThread worker;

    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean suspendFlag;
    private String md5hash;

    public ProviderThread(String name, MultipartFile file, String fileName) {
        this.setName(name);
        this.file = file;
        this.fileName = fileName;
        System.out.println("Create tread: " + getName());
    }

    @Override
    public void run() {

        try {
            System.out.println("Thread " + getName() + " started");
            this.inputStream = file.getInputStream();

            File newFile = new File("C:/temp/" + getName() + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            this.outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            Thread.sleep(100);
            synchronized (this) {
                while (suspendFlag) {
                    this.wait();
                }
            }

            System.out.println("Provider " + getName() + " writes the " + md5hash + " hash to the file");
            outputStream.write((md5hash + getName()).getBytes());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void goSuspend() {
        suspendFlag = true;
    }

    public synchronized void goResume() {
        suspendFlag = false;
        notify();
    }
}
