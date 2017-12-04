package com.backend.service;

import com.backend.model.UploadedFile;
import com.backend.thread.ProviderThread;
import com.backend.thread.WorkerThread;
import com.backend.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileManageService {
    private int i;

    @Autowired
    FileValidator fileValidator;

    public ModelAndView manageUploadedFile(UploadedFile uploadedFile, BindingResult result) {
        MultipartFile file = uploadedFile.getFile();
        fileValidator.validate(uploadedFile, result);

        String fileName = file.getOriginalFilename();
        if (result.hasErrors()) {
            return new ModelAndView("uploadForm");
        }
        threadsMain(file, fileName);
        return new ModelAndView("showFile", "message", "File '" + fileName + "' have been uploaded " + i + " times so you have " + i + " files with identical content and different names and generated hash at the end of each file. Please check the output console and the target \"C:/temp/\" (if you use Windows)");
    }

    private void threadsMain(MultipartFile file, String fileName) {
        PipedOutputStream po = new PipedOutputStream();
        PipedInputStream pi = new PipedInputStream(); // or new PipedInputStream(po);

        try {
            pi.connect(po);

            ProviderThread[] threads = new ProviderThread[3];
            WorkerThread worker = new WorkerThread();

            for (i = 0; i < 3; i++) {
                threads[i] = new ProviderThread("provider" + i, file, fileName, worker);
                threads[i].goSuspend();
                threads[i].start();
                Thread.sleep(100);
                threads[i].goResume();
                threads[i].join();
            }

           /*ExecutorService service = Executors.newSingleThreadExecutor();
            for(int i = 0; i < 3; i++){
                service.submit(new Runnable()  {
                    public void run() {
                        try {
                            ProviderThread provider = new ProviderThread("provider", file, fileName, worker);
                            provider.goSuspend();
                            provider.start();
                            Thread.sleep(100);
                            provider.goResume();
                            provider.join();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
            }*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
