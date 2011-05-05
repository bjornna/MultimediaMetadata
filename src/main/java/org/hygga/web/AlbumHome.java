package org.hygga.web;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Shelf;

@Model
public class AlbumHome implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -6050450615267699254L;
    private Shelf shelf;
    private Album album;
    private List<Picture> pictures;
    private Long albumId = null;
    private boolean set = false;
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private org.hygga.web.Model model;

    public boolean isSet() {
	return set;
    }

    @Inject
    private RequestParameterParser requestParameterParser;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void initialize() {
	albumId = requestParameterParser.getParameterAsLong("albumId");
	checkAndLoadIfPictureIdIsSet();
	if (albumId != null && albumId > 0) {
	    set = true;
	    album = em.find(Album.class, albumId);
	    
	    pictures = em
		    .createQuery(
			    "select album.pictures from Album album where album.id = :id")
		    .setParameter("id", albumId).getResultList();
	    shelf = album.getShelf();
	    model.setAlbum(album);
	    model.setShelf(shelf);
	    model.setPictures(pictures);
	    
	}

    }

    private Picture checkAndLoadIfPictureIdIsSet() {
	Long pictureId = requestParameterParser.getParameterAsLong("pictureId");
	if (pictureId != null && pictureId > 0) {
	    return em.find(Picture.class, pictureId);
	} else {
	    return null;
	}
    }

    public Shelf getShelf() {
	return shelf;
    }

    public Album getAlbum() {
	return album;
    }

    public List<Picture> getPictures() {
	return pictures;
    }

}
