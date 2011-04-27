package org.hygga.pictureservice.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class Album implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8732929508391985584L;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    @XmlTransient
    private Shelf shelf;
    @OneToMany
    private List<Picture> pictures = new ArrayList<Picture>();
    @Column(unique = true)
    private String name;
    private String path;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public List<Picture> getPictures() {
	return pictures;
    }

    public void setPictures(List<Picture> pictures) {
	this.pictures = pictures;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setShelf(Shelf shelf) {
	this.shelf = shelf;
    }

    @XmlTransient
    public Shelf getShelf() {
	return shelf;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

}
