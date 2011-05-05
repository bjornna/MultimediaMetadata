package org.hygga.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.hygga.pictureservice.PictureService;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Shelf;
import org.jboss.logging.Logger;

@Named
@SessionScoped
public class Model implements Serializable {
    private Album album;
    private Shelf shelf;
    private Picture picture;

    private List<Picture> pictures = new ArrayList<Picture>();
    private Logger log = Logger.getLogger(Model.class);
    @EJB
    private PictureService pictureService;

    @Remove
    public void destroy() {
    }

    public String updateExifDataOnCurrentPicture() {
	if (picture != null) {
	    pictureService.updateExifData(picture.getId());
	    
	    return "shelf.jsf";
	} else {
	    return "index.jsf";
	}
    }

    /**
     * 
     */
    private static final long serialVersionUID = 8514723647704610938L;

    public Album getAlbum() {
	return album;
    }

    public void setAlbum(Album album) {
	this.album = album;
    }

    public Shelf getShelf() {
	return shelf;
    }

    public void setShelf(Shelf shelf) {
	this.shelf = shelf;
    }

    public Picture getPicture() {
	return picture;
    }

    public void setPicture(Picture picture) {
	this.picture = picture;
    }

    public List<Picture> getPictures() {
	return pictures;
    }

    public void setPictures(List<Picture> pictures) {
	this.pictures = pictures;
    }

    public void setPrevPicture(Picture prevPicture) {
    }

    public Picture getPrevPicture() {
	int prev = currentPicture();
	if (prev == -1) {
	    return null;
	} else {
	    prev = prev - 1;
	    if (prev < 0) {
		prev = pictures.size() - 1;
	    }
	    log.infof("Getting previous picture with index %s", prev);
	    return pictures.get(prev);
	}
    }

    public void setNextPicture(Picture nextPicture) {
    }

    public Picture getNextPicture() {
	int next = currentPicture();
	if (next == -1) {
	    return null;
	} else {
	    next = next + 1;
	    if (next >= pictures.size() - 1) {
		next = 0;
	    }
	    log.infof("Getting next picture with index %s", next);
	    return pictures.get(next);
	}
    }

    private int currentPicture() {
	if (picture == null || pictures == null)
	    return -1;
	else {
	    final int current = pictures.indexOf(picture);
	    log.infof("Current picture has index %s", current);
	    return current;
	}

    }

}
