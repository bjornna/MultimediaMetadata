package org.hygga.integration;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.hygga.pictureservice.ExifExtractorBean;
import org.hygga.pictureservice.PictureService;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.HyggaExeption;
import org.hygga.util.HyggaRuntimeException;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PictureServiceTestCase {

    private static Picture REF_PICTURE = createRefPicture();

    @Deployment
    public static JavaArchive createTestArchive() {
	return ShrinkWrap
		.create(JavaArchive.class, "test.jar")
		.addClasses(Picture.class, Album.class, Shelf.class,
			PictureService.class, HyggaExeption.class,
			ExifExtractorBean.class, HyggaRuntimeException.class)
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

		.addAsManifestResource(EmptyAsset.INSTANCE,
			ArchivePaths.create("beans.xml"))
		.addAsManifestResource("META-INF/test-persistence.xml",
			ArchivePaths.create("persistence.xml"))
		.addAsResource("META-INF/test-import.sql",
			ArchivePaths.create("import.sql"));
    }

    private static Picture createRefPicture() {
	// 1,'2011-04-28 21:08:24','DSC00079.JPG',640787,1)
	// 17,'2011-04-28 21:10:45','IMG_1979.JPG'
	Picture pic = new Picture();
	pic.setId(new Long(17));
	pic.setName("IMG_1979.JPG");
	return pic;
    }

    @EJB
    PictureService pictureService;

    @Test
    public void testFindAll() {

	List<Picture> pictures = pictureService.findAll();
	Assert.assertNotNull("Pictures should not be null", pictures);
	Assert.assertTrue("Shuld be more than 0 pictures", pictures.size() > 0);

    }

    @Test
    public void testFindByName() {
	String name = REF_PICTURE.getName();
	List<Picture> pictures = pictureService.findByName(name);
	Assert.assertNotNull("Picture is null", pictures);
	Assert.assertEquals("Should be one picture", 1, pictures.size());
	Picture picture = pictures.get(0);
	Assert.assertEquals("Picture name not ok", name, picture.getName());
    }

    @Test
    public void testFindPathByPicture() {
	String path = pictureService.getAbsolutePathToPicture(REF_PICTURE
		.getId());
	System.out.println("PATH = " + path);
	Assert.assertNotNull(path);
	Assert.assertTrue(path.endsWith(REF_PICTURE.getName()));
	File file = new File(path);
	Assert.assertTrue("Picture file exist", file.exists());
    }

    @Test
    public void testUpdateExifData() {
	pictureService.updateExifData(REF_PICTURE.getId());
	Picture picture = pictureService.findById(REF_PICTURE.getId());
	Assert.assertNotNull(picture);
	Assert.assertNotNull("Create date should not be null",
		picture.getCreateDate());

    }
}
