package com.backend.thread;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WorkerThread extends Thread {

    private ProviderThread[] providers;

    public WorkerThread (ProviderThread[] providers) {
        this.providers = providers;
    }

    public void run() {

        try {
            System.out.println("Thread Worker started");
           /* sleep(1000);*/
          for(ProviderThread provider: providers) {

              StringBuilder builder = new StringBuilder();
              byte[] data = new byte[1024];
              int len = provider.getPi().read(data, 0, 1024);
              for (int i = 0; i < len; i++) {
                  builder.append(data[i]);
              }

            //System.out.println(builder.toString());
              String hash = getMD5hash(builder.toString() + provider.getName());
              hash += "_Md5Hash_" + provider.getName();
                      provider.getPo().write(hash.getBytes());
              System.out.println("Thread Worker generates the MD5 hash: " + hash + " for "+ provider.getName());
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
