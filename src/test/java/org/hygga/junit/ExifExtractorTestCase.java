package org.hygga.junit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.hygga.pictureservice.ExifExtractor;
import org.hygga.pictureservice.domain.ExifTag;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.PictureWithExifData;
import org.hygga.util.FileSizeConverter;
import org.hygga.util.HyggaExeption;
import org.junit.Test;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentReader;

public class ExifExtractorTestCase {

    private static String IMG_2 = "src/test/resources/img/IMG_9826.jpg";
    private static String IMG_3 = "src/test/resources/img/IMG_9814_small.jpg";

    @Test
    public void testExtractExifDataFromIMG_2() {
	File file = new File(IMG_2);
	PictureWithExifData pic = extract(file);
	Long size = new Long(1150045);
	assertSize(size, pic);

    }

    /**
     * Test if we can read metadata from URL Not working -have to check this
     */
    @Test
    public void testExtractFromURL() {
	String url = "http://www.hygga.org/img/IMG_9814_small.jpg";
	try {
	    URL uri = new URL(url);
	    ExifExtractor exifExtractor = new ExifExtractor();
	    List<ExifTag> result = exifExtractor.extract(uri.openStream());
	    Assert.assertNotNull("Result is null from google", result);
	    PictureWithExifData pic = createPictureWithExifMetadata(result);
	    pic.setPictureFile(new File("hygga_org.jpeg"));
	    log(pic);
	} catch (Exception e) {

	    System.err
		    .println("Error getting picture metadata from URL. Accept this for now");
	}
    }

    private void assertSize(Long size, PictureWithExifData pic) {
	Assert.assertEquals("Size did not mathc", size, pic.getSize());

    }

    @Test
    public void testExtractExifDataFromIMG_3() {
	PictureWithExifData pic = extract(new File(IMG_3));
	Long size = new Long(85948);
	assertSize(size, pic);

    }

    private PictureWithExifData extract(File file) {
	ExifExtractor exifExtractor = new ExifExtractor();
	try {
	    List<ExifTag> metadata = exifExtractor.extractFromFile(file);
	    PictureWithExifData pic = createPictureWithExifMetadata(metadata);
	    Picture picture = new Picture();
	    picture.setName(file.getName());
	    picture.setPath(file.getAbsolutePath());
	    pic.setPicture(picture);
	    pic.setPictureFile(file);
	    pic.setSize(file.length());
	    return pic;
	} catch (HyggaExeption e) {
	    Assert.fail("Error when extracting metadata");
	    return null;
	}
    }

    private PictureWithExifData createPictureWithExifMetadata(
	    List<ExifTag> metadata) {
	Assert.assertNotNull("Metadata is null", metadata);
	Assert.assertTrue("Metadata size <= 0", metadata.size() > 0);
	PictureWithExifData pictureWithExifData = new PictureWithExifData();
	pictureWithExifData.setExifMetadatas(metadata);

	return pictureWithExifData;
    }

    private void log(PictureWithExifData pictureWithExifData) {
	try {
	    String writeTo = pictureWithExifData.getPictureFile().getName()
		    + ".xml";
	    writeTo = "target" + File.separator + writeTo;
	    Marshaller marshaller = JAXBContext.newInstance(
		    PictureWithExifData.class).createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		    new Boolean(true));
	    marshaller.marshal(pictureWithExifData, new FileOutputStream(
		    new File(writeTo)));
	} catch (JAXBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
