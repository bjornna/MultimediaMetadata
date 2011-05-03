package org.hygga.web;

import java.io.IOException;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.hygga.pictureservice.ShelfManager;

@Model
public class AlbumController {

    @Inject
    AlbumHome albumHome;
    
    @Inject
    ShelfManager shelfManager;
    
    public void updateAlbum(){
	shelfManager.update(albumHome.getAlbum());
	try {
	    FacesContext.getCurrentInstance().getExternalContext().redirect("album.jsf?albumId=" + albumHome.getAlbum().getId());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
