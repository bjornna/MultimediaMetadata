package org.hygga.picasa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hygga.pictureservice.domain.Person;
import org.hygga.util.HyggaRuntimeException;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PicasaContactParser implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -7524767785963334586L;
    private static final String PICASA_WIN_PATH = "/AppData/Local/Google/Picasa2/contacts";
    private static final String PICASA_CONTACTS_XML = "contacts.xml";
private Logger log = Logger.getLogger(PicasaContactParser.class);
    private File contactsFile = null;

    public PicasaContactParser() {

    }

    
    
    public File getPicasaContactsXML() {
	if (contactsFile != null) {
	    return contactsFile;
	} else {
	    String path = System.getProperty("user.home");
	    String filename = path + File.separator + PICASA_WIN_PATH
		    + File.separator + PICASA_CONTACTS_XML;
	    File contactsFile = new File(filename);
	    return contactsFile;
	}
    }

    public PicasaContacts parseContacts(File file) {
	contactsFile = file;
	PicasaContacts result = parseContacts();
	contactsFile = null;
	return result;
    }
    

    public PicasaContacts parseContacts() {
	File file = getPicasaContactsXML();
	try {
	    FileInputStream fis = new FileInputStream(file);
	  return   init(fis);
	    
	} catch (FileNotFoundException e) {
	    throw new HyggaRuntimeException(
		    "Could not find picasacontact file: ", e);
	} catch (Exception e) {
	    throw new HyggaRuntimeException(e);
	}

    }
    /**
	 * <pre>
	 *  <contact id="59a8df726fd2d6a1" name="Turid Moen" 
	 *  display="Turid" modified_time="2010-08-20T08:07:25+01:00" sync_enabled="1">
	 *   <subject user="bjornna_lh" id="f976a9b8d207084"/>
	 *  </contact>
	 * </pre>
	 * 
	 * @return
	 */
	public PicasaContacts init(InputStream inputStream) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();
		log.debugf("Root element: %s", doc.getDocumentElement().getNodeName());
		NodeList contactsNodes = doc.getElementsByTagName("contact");
		PicasaContacts contacts = new PicasaContacts();
		for (int i = 0; i < contactsNodes.getLength(); i++) {
			Node node = contactsNodes.item(i);
			Element element = (Element) node;
			Person contact = new Person();
			contact.setPicasaId(element.getAttribute("id"));
			contact.setName(element.getAttribute("name"));
			contact.setEmail(element.getAttribute("email0"));
			contact.setDisplay(element.getAttribute("display"));
			contact.setModifiedTime(createModifiedTime(element
					.getAttribute("modified_time")));
			contacts.add(contact);
			log.debugf("Added contact %s", contact);
		}
		return contacts;

	}

	private Date createModifiedTime(String attribute) {
		// 2010-08-20T08:07:25+01:00
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			return sdf.parse(attribute);
		} catch (ParseException e) {
			log.errorf(e, "Could not parse date from string %s. Reason: %s",
					attribute, e.getMessage());
			return null;
		}
	}
}
