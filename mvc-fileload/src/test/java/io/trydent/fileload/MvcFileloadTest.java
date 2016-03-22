package io.trydent.fileload;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MvcFileloadApplication.class)
@WebAppConfiguration
@IntegrationTest
public class MvcFileloadTest {
	private final String folder = File.separator + System.getProperty("user.home") + File.separator + "download" + File.separator;
	private final String url = "http://localhost:9595/fileload";

	private final RestTemplate rest = new TestRestTemplate();

	@Test
	public void upload() throws URISyntaxException {
		final MultiValueMap<String, Object> multipart = new LinkedMultiValueMap<>();
		multipart.add("file", new ClassPathResource("bvs.jpg"));

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		final URI uri = new URI(this.url + "/api/upload");

		final HttpEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(multipart, headers, HttpMethod.POST, uri);
		final HttpEntity<Boolean> response = this.rest.exchange((RequestEntity<?>) request, Boolean.class);

		Assert.isTrue(response.getBody());
	}
}
