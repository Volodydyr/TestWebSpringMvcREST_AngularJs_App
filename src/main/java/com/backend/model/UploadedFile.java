package com.backend.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadedFile {

	private MultipartFile file;

}
