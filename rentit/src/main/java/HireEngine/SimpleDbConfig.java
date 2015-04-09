package HireEngine;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleDbConfig {

	@Bean
	public DataSource dataSource() {

		// Database configuration
		URI dbUri;
		try {
			String username = "postgres";
			String password = "1234";
			String url = "jdbc:postgresql://localhost:5432/plants";
			String dbProperty = System.getenv("DATABASE_URL");
			if (dbProperty != null) {
				dbUri = new URI(dbProperty);
				
				String[] data = dbUri.getUserInfo().split(":");

				username = data[0];
				password = data.length > 1 ? data[1] : "";
				url = "jdbc:postgresql://" + dbUri.getHost() + ':'
						+ dbUri.getPort() + dbUri.getPath();
			}

			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setUrl(url);
			basicDataSource.setUsername(username);
			basicDataSource.setPassword(password);

			return basicDataSource;

		} catch (URISyntaxException e) {
			return null;
		}
	}
}
