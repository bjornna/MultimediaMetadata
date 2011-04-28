package org.hygga.junit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.hygga.picasa.PicasaContactParser;
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
	List<Person> result = parser.parseContacts(contactsXML);
	Assert.assertNotNull(result);
	Assert.assertTrue(result.size() == 1);
	Assert.assertEquals("bjornna@gmail.com", result.get(0).getEmail());
	Assert.assertEquals("ce40d3d32b8e98c1", result.get(0).getPicasaId());
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
	List<Person> persons = parser.parseContacts();
	Assert.assertNotNull(persons);
	Assert.assertTrue(persons.size() > 1);

    }

}
