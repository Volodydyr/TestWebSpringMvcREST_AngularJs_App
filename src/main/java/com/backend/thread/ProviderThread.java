package com.backend.thread;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ProviderThread extends Thread {
    private MultipartFile file;
    private String fileName;
    private WorkerThread worker;
    private boolean suspendFlag;

    public ProviderThread(String name, MultipartFile file, String fileName, WorkerThread worker) {
        this.worker = worker;
        this.setName(name);
        this.file = file;
        this.fileName = fileName;
        System.out.println("Create tread: " + getName());
    }

    @Override
    public void run() {

        try {
            System.out.println("Thread " + getName() + " started");
            InputStream inputStream = file.getInputStream();
            this.worker.setInputStream(inputStream);

            File newFile = new File("C:/Users/User/Documents/" + getName() + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(newFile);
            this.worker.setOutputStream(outputStream);

            this.worker.run();

            Thread.sleep(100);
            synchronized (this) {
                while (suspendFlag) {
                    this.wait();
                }
            }

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            String hash = getName() + "-HASH=" + this.worker.getHash();
            System.out.println("Provider " + getName() + " writes the " + hash + " hash to the file");
            outputStream.write(hash.getBytes());

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
