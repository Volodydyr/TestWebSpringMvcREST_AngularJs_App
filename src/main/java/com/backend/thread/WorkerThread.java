package com.backend.thread;

import lombok.Data;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
public class WorkerThread extends Thread {

    private String fileName;
    private InputStream inputStream;
    private OutputStream outputStream;

    private String hash;

    public void run() {

        try {
            System.out.println("Thread Worker started");
            this.hash = getMD5hash(this.inputStream.toString());
            System.out.println("Thread Worker generates the MD5 hash: " + this.hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMD5hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
