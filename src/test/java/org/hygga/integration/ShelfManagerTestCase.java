package org.hygga.integration;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.hygga.pictureservice.PictureService;
import org.hygga.pictureservice.ShelfManager;
import org.hygga.pictureservice.ShelfService;
import org.hygga.pictureservice.UpdateAlbumReport;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.DirectoryFileNameFilter;
import org.hygga.util.HyggaExeption;
import org.hygga.util.HyggaRuntimeException;
import org.hygga.util.PictureFileFilter;
import org.hygga.util.ShelfDoesNotExistException;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ShelfManagerTestCase {
    @EJB
    ShelfManager shelfManager;

    @EJB
    ShelfService shelfService;

    @EJB
    PictureService pictureService;

    @PersistenceContext
    EntityManager em;
    public static String PATH = PATH_TO_TESTSHELF();
    private Logger log = Logger.getLogger(ShelfManagerTestCase.class);

   private static String PATH_TO_TESTSHELF() {
	return new File(
		"/Users/bna/git_workspace/PictureService/src/test/resources/shelf")
		.getAbsolutePath();
    }

    @Deployment
    public static JavaArchive createTestArchive() {
	return ShrinkWrap
		.create(JavaArchive.class, "test.jar")
		.addClasses(ShelfManager.class, ShelfService.class,
			ShelfDoesNotExistException.class,
			DirectoryFileNameFilter.class, PictureFileFilter.class,
			UpdateAlbumReport.class, PictureService.class,
			HyggaRuntimeException.class, HyggaExeption.class)
		.addPackage(Shelf.class.getPackage())
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
			ArchivePaths.create("persistence.xml"));
    }

    @Test
    public void testUpdateShelf() {
	Assert.assertNotNull("PATH is null", PATH);
	File file = new File(PATH);
	Assert.assertTrue("SHELF dir exist:  " + PATH, file.isDirectory());
	Shelf shelf = new Shelf();
	shelf.setName("TestShelf");
	shelf.setPath(PATH);
	Shelf indb = shelfService.storeAndFlush(shelf);
	try {
	    shelfManager.update(indb);
	    assertAlbumUpdate(indb);
	} catch (ShelfDoesNotExistException e) {
	    Assert.fail("Shelf does not exist - why ?");
	}

    }

    private void assertAlbumUpdate(Shelf indb) {
	List<Album> albums = shelfService.getAlbums(indb);
	Assert.assertNotNull("Albums is null", albums);
	Assert.assertTrue("Albums size should be 2", albums.size() == 2);
	assertFirstRoundOK(albums);
	assertSecondRoundOK(albums);

    }

    private void assertFirstRoundOK(List<Album> albums) {
	log.info("Initial update of albums");
	for (Album album : albums) {
	    UpdateAlbumReport report = shelfManager.update(album);
	    assertReportOnFirstRound(album, report);
	}
	assertPictureWithNameExist("DSC00079.JPG");
    }

    private void assertSecondRoundOK(List<Album> albums) {
	log.info("Second round of update on album");
	for (Album album : albums) {
	    UpdateAlbumReport report = shelfManager.update(album);
	    assertReportOnSecondRound(album, report);
	}
	assertPictureWithNameExist("DSC00079.JPG");
    }

    private void assertReportOnSecondRound(Album album, UpdateAlbumReport report) {
	int newPics = 0;
	int updatedPics = 0;
	if (album.getName().startsWith("2008.05.03")) {
	    newPics = 0;
	    updatedPics = 9;
	} else if (album.getName().startsWith("2009.09.06")) {
	    newPics = 0;
	    updatedPics = 8;
	}
	Assert.assertEquals("Number of new Pictures = " + newPics, newPics,
		report.newPictures());
	Assert.assertEquals("Number of updated Pictures = " + updatedPics,
		updatedPics, report.updatedPictures());
    }

    private void assertReportOnFirstRound(Album album, UpdateAlbumReport report) {
	int newPics = 0;
	int updatedPics = 0;
	if (album.getName().startsWith("2008.05.03")) {
	    newPics = 9;
	    updatedPics = 0;
	} else if (album.getName().startsWith("2009.09.06")) {
	    newPics = 8;
	    updatedPics = 0;
	}

	Assert.assertEquals("Number of new Pictures = " + newPics, newPics,
		report.newPictures());
	Assert.assertEquals("Number of updated Pictures = " + updatedPics,
		updatedPics, report.updatedPictures());

    }

    private void assertPictureWithNameExist(String name) {
	List<Picture> pictures = pictureService.findByName(name);
	Assert.assertNotNull("Result should contain one picture  - not null",
		pictures);
	Assert.assertTrue("Result should contain one picture",
		pictures.size() == 1);
	Assert.assertTrue("Picture has id", pictures.get(0).getId() > 0);

    }
}
