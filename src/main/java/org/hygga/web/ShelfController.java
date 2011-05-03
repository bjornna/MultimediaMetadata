package org.hygga.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hygga.pictureservice.ShelfManager;
import org.hygga.pictureservice.ShelfService;
import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.ShelfDoesNotExistException;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class ShelfController implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1180746504606829862L;
    Logger log = Logger.getLogger(ShelfController.class);
    @Inject
    ShelfService shelfService;

    @Inject
    ShelfManager shelfManager;

    @Inject
    ShelfHome shelfHome;
    private List<Shelf> shelfs = null;

    public void updateShelf() {
	try {
	    shelfManager.update(shelfHome.getInstance());
	 FacesContext.getCurrentInstance().getExternalContext().redirect("shelf.jsf?shelfId=" + shelfHome.getInstance().getId());   
	} catch (ShelfDoesNotExistException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @PostConstruct
    public void initialize() {
	log.info("@PostConstruct");
	shelfs = shelfService.getShelfs();
    }

    public void setShelfs(List<Shelf> shelfs) {
	this.shelfs = shelfs;
    }

    public void onShelfChanged(
	    @Observes(notifyObserver = Reception.IF_EXISTS) final Shelf shelf) {

	shelfs = shelfService.getShelfs();
    }

    public List<Shelf> getShelfs() {
	if (shelfs == null) {
	    log.infof("Shelf is null fetching shelfs from ShelfService %s",
		    shelfService);
	    shelfs = shelfService.getShelfs();
	}
	return shelfs;
    }
}
