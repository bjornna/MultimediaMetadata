package org.hygga.web;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hygga.picasa.PicasaContactParser;
import org.hygga.pictureservice.domain.Person;


@SessionScoped
@Named
@Stateful
public class PersonHome implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -3801602205908983450L;
    private List<Person> persons;
    private Person currentPerson;
    private @PersistenceContext EntityManager em;
    
    @Inject
    private PicasaContactParser picasaContactParser;
    
    @PostConstruct
    public void initalize(){
	persons = picasaContactParser.parseContacts();
    }
    @PreDestroy
    public void preDestroy(){
	
    }
    @Remove
    public void remove(){
	
    }
    public void setCurrentPerson(Person currentPerson) {
	this.currentPerson = currentPerson;
    }
    public Person getCurrentPerson() {
	return currentPerson;
    }
    public void setPersons(List<Person> persons) {
	this.persons = persons;
    }
    public List<Person> getPersons() {
	return persons;
    }
    
    public String saveAll(){
	for(Person p: persons){
	    
	    em.persist(p);
	}
	return "home";
    }
    
    
    
}
