package org.hygga.picasa;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hygga.pictureservice.domain.Person;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PicasaContactBrowser {

    private List<Person> contacts = new ArrayList<Person>();
    private Logger log = Logger.getLogger(PicasaContactBrowser.class);

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
    public List<Person> init(InputStream inputStream) throws SAXException,
	    IOException, ParserConfigurationException {
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(inputStream);
	doc.getDocumentElement().normalize();
	log.debugf("Root element: %s", doc.getDocumentElement().getNodeName());
	NodeList contactsNodes = doc.getElementsByTagName("contact");
	contacts = new ArrayList<Person>();
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
	    log.errorf(e,"Could not parse date from string %s. Reason: %s",attribute,e.getMessage());
	    return null;
	}
    }

}
