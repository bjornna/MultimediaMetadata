package org.hygga.junit;

import java.io.File;

import junit.framework.Assert;

import org.hygga.picasa.PicasaImage;
import org.hygga.picasa.PicasaIniData;
import org.hygga.picasa.PicasaIniParser;
import org.junit.Before;
import org.junit.Test;

public class PicasaIniParserTestCase {
	private PicasaIniParser parser = null;
public  static final File PICASA_INI = new File(
			"src/test/resources/shelf/2008.05.03-Tromso_Topptur/.picasa.ini");

	@Before
	public void before() {
		parser = new PicasaIniParser();

	}

	@Test
	public void testParseFile() {
		PicasaIniData result = parser.parse(PICASA_INI);
		Assert.assertNotNull("PicasaIniData is null", result);
		Assert.assertNotNull("Name null", result.getName());
		Assert.assertEquals("2008.05.03-Tromsø_Topptur", result.getName());
		Assert.assertTrue("INI file should have two images", result.getImages()
				.size() == 2);
		assertImageisStarredAndHaveFaces(result, "DSC00080.JPG");
		

	}

	private void assertImageisStarredAndHaveFaces(PicasaIniData result,
			String name) {
		PicasaImage image = result.get(name);
		Assert.assertNotNull(image);
		Assert.assertTrue("Image should be starred", image.isStarred());
		Assert.assertTrue("Image should have faces", image.hasFaces());

	}
}
