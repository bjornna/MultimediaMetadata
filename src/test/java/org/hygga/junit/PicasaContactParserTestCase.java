package org.hygga.junit;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.hygga.picasa.PicasaContactParser;
import org.hygga.picasa.PicasaContacts;
import org.hygga.pictureservice.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class PicasaContactParserTestCase {
    PicasaContactParser parser;
    File contactsXML = null;

    @Before
    public void setUp() {
	contactsXML = new File("src/test/resources/picasa/contacts.xml");
	parser = new PicasaContactParser();
    }

    @Test
    public void testParseTestContactsXml() {
	PicasaContacts result = parser.parseContacts(contactsXML);
	Assert.assertNotNull(result);
	Assert.assertTrue(result.getPersons().size() == 2);
	String picasaId = "ce40d3d32b8e98c1";
	Person person = result.get(picasaId);
	Assert.assertNotNull("Person with picasaid: " + picasaId, person);
	Assert.assertEquals("bjornna@gmail.com", person.getEmail());
	Assert.assertEquals("ce40d3d32b8e98c1", person.getPicasaId());
	Assert.assertEquals("Bjørn Næss", person.getName());
    }

    @Test
    public void testDefaultFileExists() {
	File file = parser.getPicasaContactsXML();
	Assert.assertNotNull(file);
	Assert.assertTrue(file.exists());
    }

    @Test
    public void testFindPersonsFromUsersPicasaDir() throws SAXException, IOException,
	    ParserConfigurationException {
	PicasaContacts persons = parser.parseContacts();
	Assert.assertNotNull(persons);
	Assert.assertTrue(persons.getPersons().size() > 1);

    }

}
