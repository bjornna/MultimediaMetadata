package org.hygga.pictureservice.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class Picture implements Serializable {

  
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(nullable =false)
    @XmlTransient
       private Album album;
    @Transient
    private String path;

    @Column(nullable = false)
    private String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    private long size;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @XmlTransient
    public Album getAlbum() {
	return album;
    }

    public void setAlbum(Album album) {
	this.album = album;
    }

    @XmlAttribute
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
    }

    public Date getModifiedDate() {
	return modifiedDate;
    }

    public void setSize(long size) {
	this.size = size;
    }

    public long getSize() {
	return size;
    }
    public Double getSizeInKb(){
	return org.hygga.util.FileSizeConverter.getKB(size);
    }
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Picture [id=").append(id).append(", path=")
		.append(path).append(", name=").append(name)
		.append(", modifiedDate=").append(modifiedDate)
		.append(", size=").append(size).append("]");
	return builder.toString();
    }

    /**
     * 
     */
    private static final long serialVersionUID = -4257238358502981534L;
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((album == null) ? 0 : album.hashCode());
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
	Picture other = (Picture) obj;
	if (album == null) {
	    if (other.album != null)
		return false;
	} else if (!album.equals(other.album))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getCreateDate() {
	return createDate;
    }

}
