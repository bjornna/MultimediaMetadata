package org.hygga.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hygga.pictureservice.ExifExtractorUtil;
import org.hygga.pictureservice.PictureService;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.ExifTag;
import org.hygga.pictureservice.domain.Picture;
import org.hygga.util.HyggaExeption;
import org.jboss.logging.Logger;

@Model
public class PictureHome implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6282378519276592400L;

    @Inject
    private RequestParameterParser requestParameterParser;
    private @EJB
    PictureService pictureService;

    @PersistenceContext
    EntityManager em;

    @Inject
    private org.hygga.web.Model model;
    private Picture picture;
    private Album album;
    private Long pictureId;
    private List<ExifTag> exifTags = null;
    private List<ExifTag> allTags = null;
    private boolean set = false;
    private Logger log = Logger.getLogger(PictureHome.class);

    @PostConstruct
    public void initialize() {
	pictureId = requestParameterParser.getParameterAsLong("pictureId");
	set = pictureId != null && pictureId > 0;
	if (set) {
	    log.infof("Create picture with id = %s", pictureId);
	    picture = em.find(Picture.class, pictureId);
	    album = em.find(Album.class, picture.getAlbum().getId());
	    model.setPicture(picture);
	    model.setAlbum(album);

	} else {
	    log.infof("Picture id is not set = %s", pictureId);
	}

    }

    public Picture getPicture() {
	return picture;
    }

    public Album getAlbum() {
	return album;
    }

    public Long getPictureId() {
	return pictureId;
    }

    public boolean isSet() {
	return set;
    }

    public void setExifTags(List<ExifTag> exifTags) {
	this.exifTags = exifTags;
    }

    public List<ExifTag> getExifTags() {
	if (exifTags == null) {
	    loadExifTags();
	}
	return exifTags;
    }

    @SuppressWarnings("unchecked")
    private void loadExifTags() {
	ExifExtractorUtil exifExtractor = new ExifExtractorUtil();
	try {
	    exifTags = exifExtractor.extractFromFile(new File(pictureService
		    .getAbsolutePathToPicture(pictureId)));
	} catch (HyggaExeption e) {
	    exifTags = java.util.Collections.EMPTY_LIST;
	}

    }

    public void setAllTags(List<ExifTag> allTags) {
	this.allTags = allTags;
    }

    public List<ExifTag> getAllTags() {
	if (allTags == null) {
	    loadAllTags();
	}
	return allTags;
    }

    private void loadAllTags() {
	if (!set) {
	    allTags = Collections.EMPTY_LIST;
	    return;
	}
	ExifExtractorUtil exifExtractor = new ExifExtractorUtil();
	try {
	    FileInputStream fis = new FileInputStream(new File(
		    pictureService.getAbsolutePathToPicture(pictureId)));
	    allTags = exifExtractor.extractAllTags(fis);
	} catch (HyggaExeption e) {
	    allTags = java.util.Collections.EMPTY_LIST;
	} catch (FileNotFoundException e) {
	    allTags = java.util.Collections.EMPTY_LIST;
	}

    }

}
