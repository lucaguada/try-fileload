package io.trydent.fileload.mvc;

import io.trydent.fileload.MvcFileloadSetting;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class UploadEndpoint {
	private final MvcFileloadSetting fileload;

	@Autowired
	public UploadEndpoint(MvcFileloadSetting fileload) {
		this.fileload = fileload;
	}

	@RequestMapping(method = POST, path = "/upload")
	public HttpEntity<Boolean> upload(@RequestParam final MultipartFile file) throws IOException {
		if (file.isEmpty()) return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_ACCEPTABLE);

		final String filepath = fileload.temporaryFolder() + File.separator + file.getName();

		final File uploaded = new File(filepath);
		FileUtils.copyInputStreamToFile(file.getInputStream(), uploaded);

		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
	}

	@RequestMapping(method = POST, path = "/uploadAsync")
	public Callable<HttpEntity<Boolean>> uploadAsync(@RequestParam final MultipartFile file) {
		return () -> upload(file);
	}
}
