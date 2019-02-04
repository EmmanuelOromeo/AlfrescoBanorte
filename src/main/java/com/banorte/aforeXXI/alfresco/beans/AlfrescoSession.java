package com.banorte.aforeXXI.alfresco.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alfresco")
public class AlfrescoSession {

	private static final Logger log = LoggerFactory.getLogger(AlfrescoSession.class);

	private String url;
	private String usr;
	private String ticket;
	private Session session;

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Session getSession() {
		log.info("Obteniendo Sesión... " + usr);
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();

		parameter.put(SessionParameter.USER, usr);
		parameter.put(SessionParameter.PASSWORD, ticket);

		parameter.put(SessionParameter.ATOMPUB_URL, url);
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

		session = factory.getRepositories(parameter).get(0).createSession();
		log.info("Sesión Obtenida...");
		return session;
	}
}
