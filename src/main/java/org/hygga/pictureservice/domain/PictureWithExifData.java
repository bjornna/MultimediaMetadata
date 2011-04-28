package org.hygga.pictureservice.domain;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
public class PictureWithExifData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7761960141476404421L;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Picture picture;
    @Transient
    private File pictureFile;
    private Long size;
    @OneToMany
    private List<ExifTag> exifMetadatas;

    public void setPictureFile(File pictureFile) {
	this.pictureFile = pictureFile;
    }

    public File getPictureFile() {
	return pictureFile;
    }

    public void setExifMetadatas(List<ExifTag> exifMetadatas) {
	this.exifMetadatas = exifMetadatas;
    }

    public List<ExifTag> getExifMetadatas() {
	return exifMetadatas;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getId() {
	return id;
    }

    public void setPicture(Picture picture) {
	this.picture = picture;
    }

    public Picture getPicture() {
	return picture;
    }

    public void setSize(Long size) {
	this.size = size;
    }

    public Long getSize() {
	return size;
    }
}
