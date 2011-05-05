package org.hygga.picasa;

import junit.framework.Assert;

import org.hygga.picasa.PicasaFace;
import org.hygga.picasa.PicasaFacesRectUtil;
import org.junit.Before;
import org.junit.Test;

public class PicasaRect64TestCase {

	private final String rect64_with_parent = "rect64(795575e08caa873f)";
	private final String rect64 = "795575e08caa873f";
	private PicasaFacesRectUtil picasaFacesRectUtil = null;

	@Before
	public void setup() {
		picasaFacesRectUtil = new PicasaFacesRectUtil();
	}

	@Test
	public void testParseStringArray() {
		String[] arr = picasaFacesRectUtil.toArray(rect64);
		assertArrayOK(arr);
	}

	@Test
	public void testParseRect64() {
		float[] num = picasaFacesRectUtil.rect64(rect64);
		for (float i : num) {
			System.out.println("Num: " + i);
			Assert.assertTrue(i >= 0);
		}
	}

	@Test
	public void testParseRect64WithParent() {
		String result = picasaFacesRectUtil.find64String(rect64_with_parent);
		Assert.assertEquals(rect64, result);
		String result2 = picasaFacesRectUtil.find64String(rect64);
		Assert.assertEquals(rect64, result2);

	}

	@Test
	public void testParseFace() {
		PicasaFace face = picasaFacesRectUtil.parseRect64(rect64);
		System.out.println("Face is " + face);
		Assert.assertNotNull(face);
		Assert.assertTrue(face.getBottom() > 0);
	}

	private void assertArrayOK(String[] arr) {
		String a1 = "7955";
		String a2 = "75e0";
		String a3 = "8caa";
		String a4 = "873f";
		Assert.assertEquals(a1, arr[0]);
		Assert.assertEquals(a2, arr[1]);
		Assert.assertEquals(a3, arr[2]);
		Assert.assertEquals(a4, arr[3]);
	}
}
