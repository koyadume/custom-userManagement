package in.koyad.piston.app.identityMgmt.service.identityStore.utils;

import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import in.koyad.piston.app.identityMgmt.service.identityStore.resources.ResourceMarker;
import in.koyad.piston.common.util.rest.JsonProcessor;

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
