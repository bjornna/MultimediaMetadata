package org.hygga.web;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Shelf;

@Model
public class ShelfHome {

    private Long shelfId;

    @Inject
    RequestParameterParser requestParameterParser;

    @PersistenceContext
    EntityManager em;

    private Shelf instance;
    private List<Album> albums = null;

    public Shelf getInstance() {
	if (instance != null)
	    return instance;
	shelfId = getShelfId();
	if (shelfId > 0) {
	    return em.find(Shelf.class, shelfId);
	} else {
	    return null;
	}
    }
    
   

    private Long getShelfId() {
	Long id = requestParameterParser.getParameterAsLong("shelfId");
	if (id == null)
	    return new Long(-1);
	else
	    return id;

    }

    public boolean isSet() {
	return getShelfId() > 0;
    }



 


    @SuppressWarnings("unchecked")
    public List<Album> getAlbums() {
	if(albums == null){
	    albums = em.createQuery("select shelf.albums from Shelf shelf where shelf.id = :id").setParameter("id", getShelfId()).getResultList();
	}
	return albums;
    }
}
