package com.backend.thread;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Data
public class ProviderThread extends Thread {
    private MultipartFile file;
    private String fileName;


    private PipedInputStream pi;
    private PipedOutputStream po;
    private boolean suspendFlag;

    public ProviderThread(String name, MultipartFile file, String fileName) {
        this.setName(name);
        this.file = file;
        this.fileName = fileName;
        try{
            this.po = new PipedOutputStream();
            this.pi =  new PipedInputStream(po);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Create tread: " + getName());
    }

    @Override
    public void run() {

        try {
            System.out.println("Thread " + getName() + " started");
           InputStream inputStream = file.getInputStream();

            File newFile = new File("C:/temp/" + getName() + fileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
                po.write(bytes, 0, read);
            }

            Thread.sleep(100);
            synchronized (this) {
                while (suspendFlag) {
                    this.wait();
                }
            }

            byte[] data = new byte[1024];
            int len = pi.read(data, 0, 1024);
            for (int i = 0; i < len; i++) {
                outputStream.write(data[i]);
            }

            System.out.println("Provider " + getName() + " writes the hash to the file");


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
