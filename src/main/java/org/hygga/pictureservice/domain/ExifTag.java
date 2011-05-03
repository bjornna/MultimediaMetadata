package org.hygga.pictureservice.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;

@XmlType(namespace="http://www.hygga.org/exif")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class ExifTag implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8823223478396566416L;
    @Id @GeneratedValue @XmlTransient
    private Long id;
    @ManyToOne @XmlTransient
    private PictureWithExifData pictureWithExifData;
    @XmlAttribute
    private String name;
    private String description;
    @XmlAttribute
    private String directoryName;
    @XmlAttribute
    private String tagTypeHex;
    @XmlAttribute
    private int tagType;

    public String getTagTypeHex() {
        return tagTypeHex;
    }

    public void setTagTypeHex(String tagTypeHex) {
        this.tagTypeHex = tagTypeHex;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDirectoryName() {
	return directoryName;
    }

    public void setDirectoryName(String directoryName) {
	this.directoryName = directoryName;
    }

    public ExifTag() {

    }

    public ExifTag(Tag tag) {

	name = tag.getTagName();
	directoryName = tag.getDirectoryName();
	tagTypeHex = tag.getTagTypeHex();
	tagType = tag.getTagType();
	
	try {
	    description = tag.getDescription();
	} catch (MetadataException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("ExifMetadata [name=").append(name)
		.append(", description=").append(description)
		.append(", directoryName=").append(directoryName)
		.append(", tagTypeHex=").append(tagTypeHex)
		.append(", tagType=").append(tagType).append("]");
	return builder.toString();
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getId() {
	return id;
    }

    public void setPictureWithExifData(PictureWithExifData pictureWithExifData) {
	this.pictureWithExifData = pictureWithExifData;
    }

    public PictureWithExifData getPictureWithExifData() {
	return pictureWithExifData;
    }

}
