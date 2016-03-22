package io.trydent.fileload.mvc;

import io.trydent.fileload.MvcFileloadSetting;
import javaslang.control.Try;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class DownloadEndpoint {
	private final MvcFileloadSetting fileload;

	@Autowired
	public DownloadEndpoint(MvcFileloadSetting fileload) {
		this.fileload = fileload;
	}

	@RequestMapping(method = GET, path = "/download")
	public HttpEntity<byte[]> download(@RequestParam String fileName) {
		final String folderName = this.fileload.temporaryFolder();
		final File folder = new File(folderName);

		final Collection<File> files = FileUtils.listFiles(folder, new String[] {}, false);

		final Optional<byte[]> bytes = files.stream()
			.filter(file -> file.getName().equals(fileName))
			.map(file -> Try.of(() -> FileUtils.openInputStream(file))
				.get())
			.map(stream -> Try.of(() -> IOUtils.toByteArray(stream))
				.get())
			.findFirst();

		if (!bytes.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		final byte[] content = bytes.get();

		final HttpHeaders headers = new HttpHeaders();
		if (fileName.endsWith(".pdf")) headers.setContentType(MediaType.parseMediaType("application/pdf"));

		headers.setContentDispositionFormData(fileName, fileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<>(bytes.get(), headers, HttpStatus.FOUND);
	}
}
