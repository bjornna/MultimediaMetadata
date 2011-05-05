package org.hygga.junit;

import java.util.List;

import org.hygga.picasa.PicasaImage;
import org.hygga.picasa.PicasaImageFace;
import org.hygga.picasa.PicasaIniData;
import org.hygga.picasa.PicasaIniParser;
import org.junit.Assert;
import org.junit.Test;
/**
 * TestCase to check correctparse from faces=...
 * @author bna
 *
 */
public class PicasaImageGetPicasaIdsTestCase {

	private String faces = "rect64(bf36433ee8c885c0),eca0a7d736d273a7;rect64(81695ebe9918847f),4a0893db8454f145";
	private String face = "rect64(59e22b4b8ebe7fca),74b9670455f58bf5";

	@Test
	public void testGetMany() {
		PicasaImage image = new PicasaImage();
		image.setFaces(faces);
		List<PicasaImageFace> arr = image.getPicasaImageFaces();
		Assert.assertNotNull("Returns null", arr);
		Assert.assertEquals(2, arr.size());
		Assert.assertEquals("4a0893db8454f145", arr.get(1).getPicasaId());
		Assert.assertEquals("eca0a7d736d273a7", arr.get(0).getPicasaId());
	}

	@Test
	public void testGetFace() {
		PicasaImage image = new PicasaImage();
		image.setFaces(face);
		List<PicasaImageFace> list = image.getPicasaImageFaces();
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals("rect64(59e22b4b8ebe7fca)", list.get(0).getRect64());
		Assert.assertEquals("74b9670455f58bf5", list.get(0).getPicasaId());
	}

	@Test
	public void testGetFromPicasaIni() {
		PicasaIniParser parser = new PicasaIniParser();
		PicasaIniData result = parser.parse(PicasaIniParserTestCase.PICASA_INI);
		String imgname = "DSC00080.JPG";
		String faces = "rect64(73a0a07f80ffb5d5),ce40d3d32b8e98c1";
		PicasaImage img = result.get(imgname);
		Assert.assertNotNull("Images was null", img);
		Assert.assertEquals(1, img.getPicasaImageFaces().size());
		Assert.assertEquals("rect64(73a0a07f80ffb5d5)", img
				.getPicasaImageFaces().get(0).getRect64());
		Assert.assertEquals("ce40d3d32b8e98c1", img.getPicasaImageFaces()
				.get(0).getPicasaId());
		Assert.assertEquals(faces, img.getFaces());

	}
}
