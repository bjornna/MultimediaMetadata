package org.hygga.web;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.hygga.pictureservice.PictureService;
import org.hygga.pictureservice.ShelfManager;
import org.hygga.pictureservice.domain.Picture;

@Model
public class AlbumController {

    @Inject
    AlbumHome albumHome;
    
    @Inject
    ShelfManager shelfManager;
    
    @EJB
    PictureService pictureService;
    
    public void updateAlbum(){
	shelfManager.update(albumHome.getAlbum());
	try {
	    FacesContext.getCurrentInstance().getExternalContext().redirect("album.jsf?albumId=" + albumHome.getAlbum().getId());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    public void updateExifOnAllPicturesInAlbum(){
	List<Picture> pictures = albumHome.getPictures();
	for(Picture picture:pictures){
	    pictureService.updateExifData(picture.getId());
	}
	try {
	    FacesContext.getCurrentInstance().getExternalContext().redirect("album.jsf?albumId=" + albumHome.getAlbum().getId());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
