package org.hygga.pictureservice;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.hygga.pictureservice.domain.Picture;

@Path("/pictures")
@RequestScoped
public class PictureRestService {

    @Inject
    private PictureService pictureService;

    public List<Picture> findAll() {
	return pictureService.findAll();
    }

    @GET
    @Path("/{id:[1-9][0-9]*}")
    public Picture lookupPictureById(@PathParam("id") long id) {
	final Picture picture = pictureService.findById(id);
	return picture;

    }
    

}
