package org.hygga.junit;

import java.awt.image.BufferedImage;
import java.io.File;
import org.junit.Test;

import junit.framework.Assert;

import com.bric.image.ExifThumbnailReader;

public class ExifThumbnailReaderTestCase {

	private static String IMG_2 = "src/test/resources/img/IMG_9826.jpg";
    private static String IMG_3 = "src/test/resources/img/IMG_9814_small.jpg";
    public void before(){
    	
    }
    @Test
    public void testExtractImage(){
    BufferedImage thumb = 	ExifThumbnailReader.getThumbnail(new File(IMG_2));
    Assert.assertNotNull(thumb);
    }
    @Test
    public void testExtractImage_3(){
    	BufferedImage thumb = ExifThumbnailReader.getThumbnail(new File(IMG_3));
    	Assert.assertNull(thumb);
    }
	
}
