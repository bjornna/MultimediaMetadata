package org.hygga.pictureservice;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.DirectoryFileNameFilter;
import org.hygga.util.HyggaRuntimeException;
import org.hygga.util.PictureFileFilter;
import org.hygga.util.ShelfDoesNotExistException;
import org.jboss.logging.Logger;

@Stateless
public class ShelfManager {
    private Logger log = Logger.getLogger(ShelfManager.class);
    @PersistenceContext
    EntityManager em;
    private Shelf currentShelf = null;

    private Album currentAlbum = null;

    private void parseShelfDir(Shelf shelf, File dir) {
	File[] albumDirs = dir.listFiles(new DirectoryFileNameFilter());
	for (File albumdir : albumDirs) {
	    String name = albumdir.getName();
	    Album album = findAlbum(shelf, name);
	    if (album == null) {
		album = new Album();
		album.setName(name);
		album.setShelf(currentShelf);
		album.setPath(albumdir.getName());
		log.debugf("album does not exist. Create new: %s", album);
		em.persist(album);
		em.flush();

	    } else {
		log.debugf("Album exist: %s", album);
	    }
	}

    }

    public void update(Shelf shelf) throws ShelfDoesNotExistException {
	log.infof("update shelf %s", shelf);
	currentShelf = em.find(Shelf.class, shelf.getId());
	if (currentShelf == null) {
	    throw new ShelfDoesNotExistException();
	}
	File dir = new File(shelf.getPath());
	if (dir.exists()) {
	    parseShelfDir(currentShelf, dir);
	} else {
	    throw new HyggaRuntimeException(
		    "Shelf dir does not exist. Check if this path is correcet. Path: "
			    + dir);
	}
    }

    @SuppressWarnings("unchecked")
    private Album findAlbum(Shelf shelf, String name) {
	String hql = "from Album album where album.name = :name";
	List<Album> result = em.createQuery(hql).setParameter("name", name)
		.getResultList();
	if (result == null || result.size() == 0) {
	    return null;
	} else if (result.size() > 1) {
	    throw new HyggaRuntimeException("More than one album with name: "
		    + name);
	} else {
	    return result.get(0);
	}
    }

    public UpdateAlbumReport update(Album album) {
	UpdateAlbumReport report = new UpdateAlbumReport();
	currentAlbum = findAlbum(album);
	String path = currentAlbum.getPath();
	Shelf shelf = currentAlbum.getShelf();
	path = shelf.getPath() + File.separator + path;
	log.debugf("Found album %s will update pictures from path: %s", album, path);
	File[] pictures = new File(path).listFiles(new PictureFileFilter());
	for (File pictureFile : pictures) {
	    Picture picture = createPictureFromFile(pictureFile);
	    Picture indb = findPictureFromFileAndAlbum(album, pictureFile);
	    if (indb != null) {
		
		log.debugf("Picture exist : %s", indb);
		indb.setModifiedDate(picture.getModifiedDate());
		indb.setSize(picture.getSize());
		report.updatedPicture(indb);
		
	    } else {
		log.debugf("Adding a new picture %s to currentAlbum %s", picture,currentAlbum);
		picture.setAlbum(currentAlbum);
		em.persist(picture);
		currentAlbum.add(picture);
		report.newPicture(picture);
		log.debugf("Persisted picture:  %s", picture);
	    }
	}
	em.flush();
	log.infof("Updated album %s. Report = %s", album, report);
	return report;
	
    }

    @SuppressWarnings("unchecked")
    private Picture findPictureFromFileAndAlbum(Album album, File pictureFile) {
	String hql = "from Picture p where p.name = :name and p.album = :album";
	List<Picture> result = em.createQuery(hql)
		.setParameter("name", pictureFile.getName())
		.setParameter("album", album).getResultList();
	if (result == null || result.size() <= 0) {
	    log.debugf("No picture from hql: %s", hql);
	    return null;
	} else {
	    log.debugf("Found picture %s", result.get(0));
	    return result.get(0);
	}

    }

    private Picture createPictureFromFile(File pictureFile) {
	Picture picture = new Picture();
	picture.setName(pictureFile.getName());
	picture.setPath(pictureFile.getName());
	picture.setModifiedDate(new Date(pictureFile.lastModified()));
	picture.setSize(pictureFile.length());
	return picture;
    }

    private Album findAlbum(Album album) {
	if (album == null) {
	    throw new HyggaRuntimeException(
		    "Parameter album is null - can not find any album then. ");
	} else if (em.contains(album)) {
	    log.debugf("EntityManager contains album: %s",album);
	    return album;
	} else if (album.getId() != null) {
	    log.debugf("em.findAlbumd %s", album);
	    return em.find(Album.class, album.getId());
	} else {
	    throw new HyggaRuntimeException(
		    "Could not find any Album (id = null)");
	}
    }
}
