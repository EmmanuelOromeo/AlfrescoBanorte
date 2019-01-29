package com.banorte.aforeXXI.alfresco;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

public class AlfrescoCargaArchivo {
	public static void main(String[] args) {

		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();

		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "admin");

		parameter.put(SessionParameter.ATOMPUB_URL,
				"http://127.0.0.1:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom");
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

		Session session = factory.getRepositories(parameter).get(0).createSession();
		Folder root = session.getRootFolder();

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		properties.put(PropertyIds.NAME, "AlfrescoFolderAfore");

		//Folder parent = root.createFolder(properties);
		Folder parent = (Folder) session.getObjectByPath("/AlfrescoFolder"); 
		System.out.println("PArent: " + parent);

		String nameFile = "archivoTest";
		Map<String, Object> properties2 = new HashMap<String, Object>();
		properties2.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties2.put(PropertyIds.NAME, "AlfrescoDocumentoAfore");

		byte[] content = "Hello Alfresco CMIS V2".getBytes();
		InputStream stream = new ByteArrayInputStream(content);
		ContentStream contentStream = new ContentStreamImpl(nameFile, BigInteger.valueOf(content.length), "text/plain",
				stream);

		Document newDoc = parent.createDocument(properties2, contentStream, VersioningState.MAJOR);
		System.out.println("Finalizado");

	}

}
