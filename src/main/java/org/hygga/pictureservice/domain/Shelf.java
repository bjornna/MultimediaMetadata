package org.hygga.pictureservice.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Model
public class Shelf implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2410652159106414480L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String path;
    @OneToMany(mappedBy = "shelf", cascade = CascadeType.REMOVE)
    @OrderBy("name asc")
    private List<Album> albums = new ArrayList<Album>();

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public List<Album> getAlbums() {
	return albums;
    }

    public void setAlbums(List<Album> albums) {
	this.albums = albums;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public String getPath() {
	return path;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Shelf [id=").append(id).append(", name=").append(name)
		.append(", path=").append(path).append("]");
	return builder.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((path == null) ? 0 : path.hashCode());
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
	Shelf other = (Shelf) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (path == null) {
	    if (other.path != null)
		return false;
	} else if (!path.equals(other.path))
	    return false;
	return true;
    }

    public void add(Album album) {
	albums.add(album);

    }

}
