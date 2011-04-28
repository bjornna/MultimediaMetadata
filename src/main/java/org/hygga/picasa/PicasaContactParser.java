package org.hygga.picasa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.hygga.pictureservice.domain.Person;
import org.hygga.util.HyggaRuntimeException;

public class PicasaContactParser {
    private static final String PICASA_WIN_PATH = "/AppData/Local/Google/Picasa2/contacts";
    private static final String PICASA_CONTACTS_XML = "contacts.xml";

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

    public List<Person> parseContacts(File file) {
	contactsFile = file;
	List<Person> result = parseContacts();
	contactsFile = null;
	return result;
    }

    public List<Person> parseContacts() {
	File file = getPicasaContactsXML();
	try {
	    FileInputStream fis = new FileInputStream(file);
	    PicasaContactBrowser picasaContactBrowser = new PicasaContactBrowser();
	    List<Person> result = picasaContactBrowser.init(fis);
	    return result;
	} catch (FileNotFoundException e) {
	    throw new HyggaRuntimeException(
		    "Could not find picasacontact file: ", e);
	} catch (Exception e) {
	    throw new HyggaRuntimeException(e);
	}

    }
}
