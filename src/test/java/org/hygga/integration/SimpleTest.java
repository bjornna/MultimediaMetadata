package org.hygga.integration;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.examples.FastShelfImporter;
import org.hygga.pictureservice.ShelfFromXml;
import org.hygga.pictureservice.ShelfService;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.DirectoryFileNameFilter;
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
public class SimpleTest {
    @EJB
    ShelfService shelfService;
    private static String NAME = "test_shelf";
    private static String PATH = "/Users/bna/test";

    @Deployment
    public static JavaArchive createTestArchive() {

	return ShrinkWrap
		.create(JavaArchive.class, "test.jar")
		.addClasses(ShelfService.class, Shelf.class, Album.class,
			ShelfFromXml.class, Picture.class,
			FastShelfImporter.class, HyggaRuntimeException.class,
			PictureFileFilter.class, DirectoryFileNameFilter.class)
		.addAsManifestResource(EmptyAsset.INSTANCE,
			ArchivePaths.create("beans.xml"))
		.addAsManifestResource("META-INF/test-persistence.xml",
			ArchivePaths.create("persistence.xml"));

    }

    @Test
    public void testCanPersistShelf() {
	Shelf shelf = new Shelf();
	shelf.setName(NAME);
	shelf.setPath(PATH);
	shelfService.storeAndFlush(shelf);
	List<Shelf> result = shelfService.findByName(NAME);
	Assert.assertNotNull(result);
	Assert.assertTrue(result.size() == 1);
	Assert.assertEquals(NAME, result.get(0).getName());
	Assert.assertEquals(PATH, result.get(0).getPath());
    }

    @Test
    public void testCanPersistFromXml() {
	String shelfXMLFile = "/C:/Users/bna/git_workspace/PictureService/src/test/resources/shelf_xml/shelf.xml";
	String s_name = "pictures";
	String s_path = "/C:/Users/bna/tmp/pictures/";
	ShelfFromXml shelfFromXml = ShelfFromXml.instance();

	Shelf shelf = shelfFromXml.fromXML(new File(shelfXMLFile));
	Assert.assertEquals(s_name, shelf.getName());
	Assert.assertEquals(s_path, shelf.getPath());

	shelfService.storeAndFlush(shelf);
	List<Shelf> result = shelfService.findByName(s_name);
	Assert.assertNotNull(result);
	Assert.assertTrue(result.size() == 1);
	Assert.assertEquals(s_name, result.get(0).getName());

    }
}
