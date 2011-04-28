package org.hygga.pictureservice;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hygga.pictureservice.domain.Shelf;

@Stateless
public class ShelfService {

    @PersistenceContext
    EntityManager em;
    public void storeAndFlush(Shelf shelf) {
	em.persist(shelf);
	em.flush();
    }

    @SuppressWarnings("unchecked")
    public List<Shelf> findByName(String name) {
	return em.createQuery("from Shelf shelf where shelf.name = :name")
		.setParameter("name", name).getResultList();
    }
    @SuppressWarnings("unchecked")
    public List<Shelf> getShelfs(){
	return em.createQuery("from Shelf").getResultList();
    }
}