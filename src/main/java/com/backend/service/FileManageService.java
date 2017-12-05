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
        try {

            ProviderThread[] threads = new ProviderThread[100];
            for (i = 0; i < 100; i++) {
                threads[i] = new ProviderThread("provider" + i, file, fileName);
            }
            WorkerThread worker = new WorkerThread(threads);

            for (i = 0; i < 100; i++) {
                threads[i].goSuspend();
                threads[i].start();
            }
            Thread.sleep(200);
            worker.start();

            for (i = 0; i < 100; i++) {
                Thread.sleep(100);
                threads[i].goResume();
                threads[i].join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
