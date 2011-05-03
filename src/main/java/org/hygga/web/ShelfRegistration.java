package org.hygga.web;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hygga.pictureservice.ShelfService;
import org.hygga.pictureservice.domain.Shelf;
import org.jboss.logging.Logger;

@Model
public class ShelfRegistration {

    private Logger log = Logger.getLogger(ShelfRegistration.class);
    private Shelf newShelf;
    @Inject
    private ShelfService shelfService;

    @Inject
    private Event<Shelf> shelfEventSrc;

    @Produces
    @Named
    public Shelf getNewShelf() {
	return newShelf;
    }

    public void register() {
	log.infof("Regiser new shelf: %s", newShelf);
	if (pathExist()) {
	    if (nameEmptyOrNull()) {
		newShelf.setName(new File(newShelf.getPath()).getName());
	    }
	 final Shelf registered =    shelfService.storeAndFlush(newShelf);
	    shelfEventSrc.fire(newShelf);
	    initNewShelf();
	    try {
		FacesContext.getCurrentInstance().getExternalContext().redirect("shelf.jsf?shelfId="+ registered.getId() );
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} else {
	    log.warnf("Path does not exist %s", newShelf.getPath());
	    FacesContext.getCurrentInstance().addMessage("shelfRegisterPath",
		    new FacesMessage("The path does not exist"));
	}

    }

    private boolean nameEmptyOrNull() {
	String name = newShelf.getName();
	if (name == null) {
	    return true;
	} else if (name.isEmpty()) {
	    return true;
	} else if ("".equals(name)) {
	    return true;
	} else {
	    return false;
	}
    }

    private boolean pathExist() {
	String path = newShelf.getPath();
	File file = new File(path);
	if (file.exists() && file.isDirectory()) {
	    return true;
	} else {
	    return false;
	}
    }

    @PostConstruct
    public void initNewShelf() {
	newShelf = new Shelf();
    }
}
