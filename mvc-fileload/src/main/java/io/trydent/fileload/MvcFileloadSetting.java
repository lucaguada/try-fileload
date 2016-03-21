package io.trydent.fileload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class MvcFileloadSetting {
	@Value("temporary.folder")
	private String temporaryFolder;

	public final String temporaryFolder() { return this.temporaryFolder; }
}
