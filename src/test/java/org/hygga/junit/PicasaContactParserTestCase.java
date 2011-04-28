package org.hygga.junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.hygga.picasa.PicasaContactBrowser;
import org.hygga.picasa.PicasaContactParser;
import org.hygga.pictureservice.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class PicasaContactParserTestCase {
    PicasaContactParser parser;

    @Before
    public void setUp() {
	parser = new PicasaContactParser();
    }

    @Test
    public void testDefaultFileExists() {
	File file = parser.getPicasaContactsXML();
	Assert.assertNotNull(file);
	Assert.assertTrue(file.exists());
    }

    @Test
    public void testFindPersons() throws SAXException, IOException,
	    ParserConfigurationException {
	File file = parser.getPicasaContactsXML();
	FileInputStream fis = new FileInputStream(file);
	PicasaContactBrowser browser = new PicasaContactBrowser();
	List<Person> persons = browser.init(fis);
	Assert.assertNotNull(persons);
	Assert.assertTrue(persons.size() > 1);

    }

}
