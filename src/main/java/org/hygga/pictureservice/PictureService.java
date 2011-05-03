package org.hygga.pictureservice;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Shelf;
import org.jboss.logging.Logger;

@Stateless
public class PictureService {

    private Logger log = Logger.getLogger(PictureService.class);

    @PersistenceContext
    EntityManager em;

    public List<Picture> findByName(String name) {
	log.infof("Find picture by name: %s", name);
	return em
		.createQuery("from Picture p where p.name = :name",
			Picture.class).setParameter("name", name)
		.getResultList();
    }

    public Picture findById(long id) {
	final Picture picture = em.find(Picture.class, id);
	return picture;

    }

    @SuppressWarnings("unchecked")
    public List<Picture> findAll() {
	final List<Picture> pictures = em.createQuery("from Picture")
		.getResultList();
	log.infof("Found %s pictures", pictures == null ? 0 : pictures.size());
	return pictures;
    }

    public String getAbsolutePathToPicture(Long id) {
	final Picture picture = em.find(Picture.class, id);
	final Album album = picture.getAlbum();
	final Shelf shelf = album.getShelf();
	String path = shelf.getPath() + "/" + album.getPath()
		+ "/" + picture.getName();
	return path;

    }

}
