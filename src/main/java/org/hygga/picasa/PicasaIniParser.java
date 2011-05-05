package org.hygga.picasa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Set;

import org.hygga.util.HyggaRuntimeException;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.ini4j.Profile.Section;
import org.jboss.logging.Logger;

public class PicasaIniParser {
	private Logger log = Logger.getLogger(PicasaIniParser.class);
	private PicasaIniData picasaIniData;

	public PicasaIniData parse(File file) {
		try {
			log.debugf("Parse file %s", file);
			parse(new FileInputStream(file));
			return picasaIniData;
		} catch (FileNotFoundException e) {
			throw new HyggaRuntimeException("The file does not exist: " + file);
		}
	}

	public void parse(InputStream inputStream) {
		try {
			picasaIniData = new PicasaIniData();
			Wini ini = new Wini(inputStream);
			
			Set<Entry<String, Section>> entries = ini.entrySet();
			for (Entry<String, Section> entry : entries) {
				parse(entry);
			}

		} catch (InvalidFileFormatException e) {
			throw new HyggaRuntimeException(e);
		} catch (IOException e) {
			throw new HyggaRuntimeException(e);
		}

	}

	private void parse(Entry<String, Section> entry) {
		if (isImage(entry)) {
			parseImage(entry);
		} else if ("Picasa".equals(entry.getKey())) {
			parsePicasaSection(entry);
		} else if ("Contacts".equals(entry.getKey())) {
			parseContacts(entry);
		}

	}

	private void parseContacts(Entry<String, Section> entry) {
		log.debugf("Parse contacts value %s", entry.getValue());

	}

	private void parseImage(Entry<String, Section> entry) {
		log.debugf("Parse image %s - value: %s", entry.getKey(),
				entry.getValue());
		PicasaImage image = new PicasaImage();
		image.setName(entry.getKey());
		image.setFaces(entry.getValue().get("faces"));
		image.setStarred(entry.getValue().get("star"));
		picasaIniData.add(image);

	}

	private boolean isImage(Entry<String, Section> entry) {
		String key = entry.getKey().toLowerCase();
		if (key.endsWith("jpg")) {
			return true;
		} else {
			return false;
		}

	}

	private void parsePicasaSection(Entry<String, Section> entry) {
		log.debugf("Parse Picasa section: %s", entry.getValue());
		Section s = entry.getValue();
		picasaIniData.setName(s.get("name"));
		picasaIniData.setDescription(s.get("description"));

	}
}
