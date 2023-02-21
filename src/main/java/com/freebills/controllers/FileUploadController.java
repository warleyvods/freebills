package com.freebills.controllers;

import com.freebills.controllers.dtos.FileUpload;
import com.freebills.gateways.FileUploadGateway;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/s3")
public class FileUploadController {

	private final FileUploadGateway uploadGateway;

	@PostMapping(value = "/bucket/create/{bucketName}")
	public String createBucket(@PathVariable String bucketName) {
		return uploadGateway.createBucket(bucketName);
	}

	@GetMapping(value = "/bucket/list")
	public List<String> getBucketList() {
		return uploadGateway.getBucketList();
	}

	@GetMapping(value = "/bucket/files/{bucketName}")
	public List<FileUpload> getBucketfiles(@PathVariable String bucketName) {
		return uploadGateway.getBucketfiles(bucketName);
	}

	@DeleteMapping(value = "/bucket/delete/hard/{bucketName}")
	public String hardDeleteBucket(@PathVariable String bucketName) {
		return uploadGateway.hardDeleteBucket(bucketName);
	}

	@DeleteMapping(value = "/bucket/delete/{bucketName}")
	public String softDeleteBucket(@PathVariable String bucketName) {
		return uploadGateway.softDeleteBucket(bucketName);
	}

	@PostMapping(value = "/file/upload/{bucketName}")
	public String fileUplaod(@PathVariable String bucketName, MultipartFile file) {
		return uploadGateway.fileUplaod(bucketName, file);
	}

	@DeleteMapping(value = "/file/delete/{bucketName}/{fileName}")
	public String deleteFile(@PathVariable String bucketName, @PathVariable String fileName) {
		return uploadGateway.deleteFile(bucketName, fileName);
	}

	@GetMapping(value = "/file/download/{bucketName}/{fileName}")
	public StreamingResponseBody downloadFile(@PathVariable String bucketName, @PathVariable String fileName, HttpServletResponse httpResponse) {
		FileUpload downloadFile = uploadGateway.downloadFile(bucketName, fileName);
		httpResponse.setContentType("application/octet-stream");
		httpResponse.setHeader("Content-Disposition",
				String.format("inline; filename=\"%s\"", downloadFile.getFileName()));
		return outputStream -> {
			outputStream.write(downloadFile.getFile());
			outputStream.flush();
		};
	}

	@GetMapping(value = "/file/download/{bucketName}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<InputStreamResource> findPhoto(@PathVariable String bucketName, @PathVariable String fileName, HttpServletResponse httpResponse) {
		FileUpload downloadFile = uploadGateway.downloadFile(bucketName, fileName);
		httpResponse.setContentType("application/octet-stream");
		httpResponse.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", downloadFile.getFileName()));
		InputStream myInputStream = new ByteArrayInputStream(downloadFile.getFile());
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(myInputStream));
	}
}