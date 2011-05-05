package org.hygga.integration;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.hygga.pictureservice.ExifExtractorUtil;
import org.hygga.pictureservice.ShelfFromXml;
import org.hygga.pictureservice.ShelfService;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.ExifTag;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.PictureWithExifData;
import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.DirectoryFileNameFilter;
import org.hygga.util.HyggaExeption;
import org.hygga.util.HyggaRuntimeException;
import org.hygga.util.PictureFileFilter;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ExifExtractorIntegrationTestCase {
    @Inject
    ExifExtractorUtil exifExtractor;

    @Deployment
    public static JavaArchive createTestArchive() {

	return ShrinkWrap
		.create(JavaArchive.class, "test.jar")
		.addClasses(ExifExtractorUtil.class, ExifTag.class,
			ShelfService.class, Shelf.class, Album.class,
			ShelfFromXml.class, Picture.class, HyggaExeption.class,
			HyggaRuntimeException.class, PictureFileFilter.class,
			PictureWithExifData.class,
			DirectoryFileNameFilter.class)
		.addPackage(com.drew.metadata.Tag.class.getPackage())
		.addPackage(com.drew.lang.Rational.class.getPackage())
		.addPackage(
			com.drew.imaging.ImageMetadataReader.class.getPackage())
		.addPackage(
			com.drew.imaging.jpeg.JpegMetadataReader.class
				.getPackage())
		.addPackage(
			com.drew.metadata.iptc.IptcReader.class.getPackage())
		.addPackage(
			com.drew.metadata.exif.ExifDirectory.class.getPackage())
		.addPackage(
			com.drew.metadata.jpeg.JpegReader.class.getPackage())

		.addAsResource(
			new File("src/test/resources/img/IMG_9814_small.jpg"),
			"img.jpg")
		.addAsManifestResource(EmptyAsset.INSTANCE,
			ArchivePaths.create("beans.xml"))
		.addAsManifestResource("META-INF/test-persistence.xml",
			ArchivePaths.create("persistence.xml"));

    }

    @Test
    public void testParseExif() {

	InputStream is = ExifExtractorUtil.class.getResourceAsStream("/img.jpg");
	try {
	    List<ExifTag> result = exifExtractor.extract(is);
	    Assert.assertNotNull("No metadata", result);
	    Assert.assertTrue("Metadata <= 0", result.size() > 0);
	} catch (HyggaExeption e) {
	    Assert.fail(e.getMessage());
	}
    }
}
