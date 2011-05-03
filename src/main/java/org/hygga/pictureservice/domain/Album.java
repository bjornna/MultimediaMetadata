package org.hygga.pictureservice.domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "album")
    
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
	if (pictures == null) {
	    return new ArrayList<Picture>();
	}
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

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Album other = (Album) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Album [id=").append(id).append(", name=").append(name)
		.append(", path=").append(path).append("]");
	return builder.toString();
    }

    public void add(Picture picture) {
	if (picture == null) {
	    throw new IllegalArgumentException("Picture is null");
	}
	if (this.getPictures().contains(picture)) {
	    return;
	}
	if (picture.getAlbum() != null && !this.equals(picture.getAlbum())) {

	}
	picture.setAlbum(this);
	pictures.add(picture);

    }

    public void removePicture(Picture picture) {
	if (picture == null) {
	    throw new IllegalArgumentException("Null picture");
	}
	if (!picture.getAlbum().equals(this)) {
	    throw new IllegalArgumentException(
		    "This album does not containt this picture" + picture);
	}
	pictures.remove(picture);
    }

    public boolean isEmpty() {
	return pictures == null || pictures.isEmpty();
    }

    public String getAbsolutePath() {
	if (getShelf().getPath() == null) {
	    return null;
	}
	return getShelf().getPath() + File.separator + getName();
    }
}
