package org.hygga.pictureservice;

import java.io.Serializable;

import org.hygga.pictureservice.domain.Picture;

public class UpdateAlbumReport implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -3878996315219975268L;
    
    private int newPictures = 0;
    private int updatedPicture = 0;

    public void newPicture(Picture picture) {
	newPictures++;
    }

    public void updatedPicture(Picture picture) {
	updatedPicture++;
    }

    public int newPictures() {
	return newPictures;
    }

    public int updatedPictures() {
	return updatedPicture;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("UpdateAlbumReport [newPictures=").append(newPictures)
		.append(", updatedPicture=").append(updatedPicture).append("]");
	return builder.toString();
    }
    
}
