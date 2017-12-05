package com.backend.thread;

import lombok.Data;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
public class WorkerThread extends Thread {

    private String fileName;
    private PipedInputStream pi;
    private PipedOutputStream po;
    private ProviderThread[] providers;

    public WorkerThread (ProviderThread[] providers, PipedInputStream pi, PipedOutputStream po) {
        this.providers = providers;
        this.pi = pi;
        this.po = po;
    }

    public void run() {

        try {
            System.out.println("Thread Worker started");
           /* sleep(1000);*/
          for(ProviderThread provider: providers) {
              StringBuilder builder = new StringBuilder();
              byte[] data = new byte[1024];
              int len = pi.read(data, 0, 1024);
              for (int i = 0; i < len; i++) {
                  builder.append(data[i]);
              }
//            System.out.println(builder.toString());
              String hash = getMD5hash(builder.toString());
              po.write(hash.getBytes());
              System.out.println("Thread Worker generates the MD5 hash: " + provider.getMd5hash());

          }

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
