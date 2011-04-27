package org.hygga.pictureservice;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hygga.pictureservice.domain.Person;

@Stateless
public class PersonService {

    @PersistenceContext
    EntityManager em;

    public void saveAndFlush(Person person) {
	em.persist(person);
	em.flush();
    }

    @SuppressWarnings("unchecked")
    public Person findByPicasaId(String id) {
	String hql = "from Person person where person.picasaId = :pid";
	List<Person> result = em.createQuery(hql).setParameter("pid", id)
		.getResultList();
	if (result == null || result.size() < 1) {
	    return null;
	} else {
	    return result.get(0);
	}
    }
}
