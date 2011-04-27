package org.hygga.pictureservice.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;



@Entity
@XmlRootElement
public class Person implements Serializable {
    /**
     * 
     */

    private static final long serialVersionUID = 4128826005572169599L;
    private Long id;
    private String picasaId;
    private String name;
    private String email;
    private String display;
    private Date modifiedTime;

    @Id
    @GeneratedValue
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDisplay() {
	return display;
    }

    public void setDisplay(String display) {
	this.display = display;
    }

    public Date getModifiedTime() {
	return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
	this.modifiedTime = modifiedTime;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Person [id=").append(id).append(", picasaId=")
		.append(picasaId).append(", name=").append(name)
		.append(", email=").append(email).append(", display=")
		.append(display).append(", modifiedTime=").append(modifiedTime)
		.append("]");
	return builder.toString();
    }

    public void setPicasaId(String picasaId) {
	this.picasaId = picasaId;
    }

    @Column(name = "picasa_id", unique = true)
    public String getPicasaId() {
	return picasaId;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getEmail() {
	return email;
    }

}