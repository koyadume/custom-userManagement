/**
 * 
 */
package in.koyad.piston.app.userMgmt.service.application;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;

import in.koyad.piston.app.userMgmt.service.utils.JerseyConfig;

/**
 *
 *
 * @author Shailendra Singh
 * @since 1.0
 */
public class RESTApplication {
	public static void main(String[] args) {
		JerseyConfig config = new JerseyConfig();

		System.out.println("Starting application ..");
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9090).build();
		JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Application started.");
	}
}
