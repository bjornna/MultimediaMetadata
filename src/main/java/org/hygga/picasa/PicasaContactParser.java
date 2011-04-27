package org.hygga.picasa;

import java.io.File;

public class PicasaContactParser {
    private static final String PICASA_WIN_PATH = "/AppData/Local/Google/Picasa2/contacts";
    private static final String PICASA_CONTACTS_XML = "contacts.xml";

    public PicasaContactParser() {

    }

    public File getPicasaContactsXML() {
	String path = System.getProperty("user.home");
	String filename = path + File.separator + PICASA_WIN_PATH
		+ File.separator + PICASA_CONTACTS_XML;
	File contactsFile = new File(filename);
	return contactsFile;
    }
}
