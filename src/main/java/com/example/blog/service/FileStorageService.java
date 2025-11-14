package com.example.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

	@Value("${app.upload.dir:uploads}")
	private String uploadDir;

	private static final Set<String> allowedContentTypes = Set.of(
			"image/png", "image/jpeg", "image/jpg", "image/gif", "image/webp"
	);

	public String storeImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return null;
		}
		String contentType = file.getContentType();
		if (contentType == null || !allowedContentTypes.contains(contentType)) {
			throw new IllegalArgumentException("Unsupported image type");
		}
		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
		String ext = "";
		int dot = originalFilename.lastIndexOf('.')
				;
		if (dot >= 0) {
			ext = originalFilename.substring(dot);
		}
		String filename = UUID.randomUUID().toString() + ext;
		try {
			Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
			Files.createDirectories(dir);
			Path target = dir.resolve(filename);
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			return "/uploads/" + filename;
		} catch (IOException e) {
			throw new IllegalStateException("Failed to store file", e);
		}
	}
}


