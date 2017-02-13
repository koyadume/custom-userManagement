package in.koyad.piston.app.userMgmt.service.utils;

import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import in.koyad.piston.app.userMgmt.service.resources.ResourceMarker;
import in.koyad.piston.common.utils.JsonProcessor;

public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		super();

		packages(ResourceMarker.class.getPackage().getName());

		register(getJsonProvider());
	}

	private JacksonJsonProvider getJsonProvider() {
		JacksonJsonProvider provider = new JacksonJsonProvider();
		provider.setMapper(JsonProcessor.getMapper());

		return provider;
	}

}
