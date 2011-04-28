package org.hygga.pictureservice;

import java.io.File;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hygga.pictureservice.domain.Shelf;
import org.hygga.util.HyggaRuntimeException;
import org.jboss.logging.Logger;

@Stateless
public class ShelfFromXml {
    private Logger log = Logger.getLogger(ShelfFromXml.class);

    @EJB
    ShelfService shelfService;

    public static ShelfFromXml instance() {
	ShelfFromXml shelfFromXml = new ShelfFromXml();
	return shelfFromXml;
    }

    public Shelf fromXML(InputStream inputStream) {
	try {
	    Unmarshaller unmarshaller = JAXBContext.newInstance(Shelf.class)
		    .createUnmarshaller();
	    return (Shelf) unmarshaller.unmarshal(inputStream);
	} catch (JAXBException e) {
	    throw new HyggaRuntimeException(
		    "Could not load Shelf from XML input stream. ", e);
	}
    }

    public Shelf fromXML(File file) {
	Unmarshaller unmarshaller;
	try {
	    unmarshaller = JAXBContext.newInstance(Shelf.class)
		    .createUnmarshaller();
	    return (Shelf) unmarshaller.unmarshal(file);
	} catch (JAXBException e) {
	    throw new HyggaRuntimeException("Could not load Shelf from file: "
		    + file + ". Reason: " + e);
	}

    }

    public void persistShelfFromXml(File file) {
	Shelf shelf = fromXML(file);
	log.infof("will store %s", shelf);
	shelfService.storeAndFlush(shelf);
    }
}
