package org.hygga.junit;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.hygga.picasa.PicasaContactParser;
import org.hygga.picasa.PicasaContacts;
import org.hygga.picasa.PicasaImage;
import org.hygga.picasa.PicasaImageFace;
import org.hygga.pictureservice.domain.Person;
import org.junit.Before;
import org.junit.Test;

public class PicasaContactFromIniTestCase {

	private PicasaImage picasaImage = null;
	private File picasaContactFile = new File("src/test/resources/picasa/contacts.xml");

	@Before
	public void before() {
		picasaImage = new PicasaImage();
		picasaImage
				.setFaces("rect64(bf36433ee8c885c0),eca0a7d736d273a7;rect64(81695ebe9918847f),4a0893db8454f145");
		picasaImage.setName("IMG_1972.JPG");
	}

	@Test
	public void testFindPerson() {
		PicasaContactParser parser = new PicasaContactParser();
		PicasaContacts contacts = parser.parseContacts(picasaContactFile);
		List<PicasaImageFace> faces = picasaImage.getPicasaImageFaces();
		for(PicasaImageFace imageface: faces){
		Person p = 	contacts.get(imageface.getPicasaId());
		Assert.assertNotNull(p);
			
		}

	}
}
