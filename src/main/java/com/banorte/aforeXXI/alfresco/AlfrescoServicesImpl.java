package com.banorte.aforeXXI.alfresco;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banorte.aforeXXI.alfresco.beans.AlfrescoSession;

@RestController
@CrossOrigin
public class AlfrescoServicesImpl {

	@Autowired
	private AlfrescoSession alfrescoSession;

	private static final Logger log = LoggerFactory.getLogger(AlfrescoServicesImpl.class);

	@RequestMapping(value = "/api/properties", method = RequestMethod.GET)
	public Map<String, String> parametros() {

		Map<String, String> appProperties = new HashMap<String, String>();
		appProperties.put("url", alfrescoSession.getUrl());
		appProperties.put("usr", alfrescoSession.getUsr());
		appProperties.put("ticket", alfrescoSession.getTicket());
		return appProperties;

	}

	@RequestMapping(value = "/api/crearfolder", method = RequestMethod.GET)
	public Folder crearFolder(@RequestParam("nombre") String nombreFolder) {

		Session session = alfrescoSession.getSession();
		Folder root = session.getRootFolder();
		Folder folder = null;
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		properties.put(PropertyIds.NAME, nombreFolder);

		try {
			folder = root.createFolder(properties);

			log.info("Objeto creado... " + nombreFolder);
		} catch (CmisContentAlreadyExistsException e) {
			folder = (Folder) session.getObjectByPath(root.getPath() + nombreFolder);
		}

		return folder;
	}

	@RequestMapping(value = "/api/eliminarfolder", method = RequestMethod.GET)
	public String eliminarFolder(@RequestParam("ruta") String rutaFolder, @RequestParam("nombre") String nombreFolder) {

		Session session = alfrescoSession.getSession();
		String mensaje = null;
		try {
			CmisObject object = session.getObjectByPath(rutaFolder + nombreFolder);
			Folder eliminarFolder = (Folder) object;
			eliminarFolder.deleteTree(true, UnfileObject.DELETE, true);

			mensaje = "Objeto eliminado... " + nombreFolder;
			log.info(mensaje);
		} catch (CmisObjectNotFoundException e) {
			mensaje = "Objeto no encontrado -  " + nombreFolder;
		}

		return mensaje;
	}

	@RequestMapping(value = "/api/creardocumento", method = RequestMethod.GET)
	public Document crearDocumento(@RequestParam("ruta") String rutaFolder,
			@RequestParam("nombre") String nombreDocumento) {

		Session session = alfrescoSession.getSession();

		Folder folder = (Folder) session.getObjectByPath(rutaFolder);

		Document documento = null;
		Map<String, Object> properties2 = new HashMap<String, Object>();
		properties2.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties2.put(PropertyIds.NAME, nombreDocumento);

		byte[] content = "Hello Alfresco CMIS V2".getBytes();
		InputStream stream = new ByteArrayInputStream(content);
		ContentStream contentStream = new ContentStreamImpl(nombreDocumento, BigInteger.valueOf(content.length),
				"text/plain", stream);

		try {
			documento = folder.createDocument(properties2, contentStream, VersioningState.MAJOR);

			log.info("Objeto creado... " + nombreDocumento);
		} catch (CmisContentAlreadyExistsException e) {
			documento = (Document) session.getObjectByPath(folder.getPath() + "/" + nombreDocumento);
		}

		return documento;

	}

	@RequestMapping(value = "/api/eliminardocumento", method = RequestMethod.GET)
	private String eliminarDocumento(@RequestParam("ruta") String rutaFolder,
			@RequestParam("nombre") String nombreDocumento) {

		Session session = alfrescoSession.getSession();
		String rutDocumento = rutaFolder + "/" + nombreDocumento;
		String mensaje = null;
		try {
			CmisObject object = session.getObjectByPath(rutDocumento);
			Document documento = (Document) object;
			documento.delete(true);
			mensaje = "Objeto eliminado... " + nombreDocumento;
			log.info(mensaje);

		} catch (CmisObjectNotFoundException e) {
			mensaje = "Objeto no encontrado...  " + nombreDocumento;
		}

		return mensaje;
	}

}
