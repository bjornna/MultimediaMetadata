package org.hygga.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hygga.pictureservice.domain.*;
import org.jboss.logging.Logger;

public class FastShelfImporter {
    private Logger log = Logger.getLogger(FastShelfImporter.class);
    private final File dir;
    private Shelf shelf;
    private Album album;
    private Picture picture;
    private List<File> albumFiles = new ArrayList<File>();
    private File currentAlbumFile;

    public FastShelfImporter(File dir) {
	this.dir = dir;

    }

    private void validate() {
	if (dir == null || !dir.exists()) {
	    throw new HyggaRuntimeException(
		    "The directory to import from does not exist: " + dir);
	}
    }

    public Shelf doImport() {
	log.info("Do import on dir: " + dir);
	validate();
	shelf = new Shelf();
	shelf.setPath(toPath(dir));
	shelf.setName(dir.getName());
	collectAlbumFiles();
	addAlbumsFromAlbumFiles();
	return shelf;
    }

    private void collectAlbumFiles() {
	File[] firstLevelAlbumFiles = dir
		.listFiles(new DirectoryFileNameFilter());
	if (firstLevelAlbumFiles == null) {
	    log.warnf("There is no directories in dir '%s'", dir);
	} else {
	    for (File f : firstLevelAlbumFiles) {
		albumFiles.add(f);
		collectChildren(f);
	    }
	}

    }

    private void collectChildren(File f) {
	File[] child = f.listFiles(new DirectoryFileNameFilter());
	for (File c : child) {
	    albumFiles.add(c);
	    collectChildren(c);
	}

    }

    private void addAlbumsFromAlbumFiles() {

	for (File albumFile : albumFiles) {
	    log.infof("Adding album '%s' ", albumFile.getName());
	    currentAlbumFile = albumFile;
	    album = new Album();
	    album.setPath(toPath(albumFile));
	    album.setName(albumFile.getName());
	    album.setShelf(shelf);
	    shelf.getAlbums().add(album);
	    addPicturesToCurrentAlbumFile();
	}
    }

    private String toPath(File file) {
	return file.toURI().getPath();
    }

    private void addPicturesToCurrentAlbumFile() {

	File[] pictureFiles = currentAlbumFile
		.listFiles(new PictureFileFilter());
	for (File pictureFile : pictureFiles) {
	    log.debugf("Adding picture: '%s'", pictureFile.getName());
	    picture = new Picture();
	    picture.setAlbum(album);
	    picture.setName(pictureFile.getName());
	    picture.setPath(toPath(currentAlbumFile));
	    album.getPictures().add(picture);

	}

    }

    private void marshals(String file) {
	try {
	    Marshaller marshaller = JAXBContext.newInstance(Shelf.class)
		    .createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		    new Boolean(true));
	    marshaller.marshal(shelf, new FileOutputStream(new File(file)));
	} catch (JAXBException e) {
	    log.errorf("Could not marshal shelf. Reason: %s", e.getMessage(), e);
	} catch (FileNotFoundException e) {
	    log.errorf("Could not marshal shelf. Reason: %s", e.getMessage(), e);
	}
    }

    public static void main(String[] args) {
	String dir = "/Users/bna/Pictures/";
	FastShelfImporter importer = new FastShelfImporter(new File(dir));
	importer.doImport();
	importer.marshals("shelf_pictures.xml");

    }

}
