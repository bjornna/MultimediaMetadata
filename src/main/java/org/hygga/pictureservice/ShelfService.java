package org.hygga.pictureservice;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hygga.pictureservice.domain.Album;
import org.hygga.pictureservice.domain.Shelf;

@Stateless
public class ShelfService {

    @PersistenceContext
    EntityManager em;

    public Shelf storeAndFlush(Shelf shelf) {
	em.persist(shelf);
	em.flush();
	return shelf;
    }

    public Shelf findById(Long id) {
	return em.find(Shelf.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Shelf> findByName(String name) {
	return em.createQuery("from Shelf shelf where shelf.name = :name")
		.setParameter("name", name).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Shelf> getShelfs() {
	return em.createQuery("from Shelf").getResultList();
    }

    public void albumExist(Shelf shelf, String name) {
	em.find(Shelf.class, shelf.getId());
    }

    @SuppressWarnings("unchecked")
    public List<Album> getAlbums(Shelf indb) {
	Shelf shelf = findById(indb.getId());
	return em.createQuery("select shelf.albums from Shelf shelf where shelf.id = :id").setParameter("id", shelf.getId()).getResultList();
	

    }

}