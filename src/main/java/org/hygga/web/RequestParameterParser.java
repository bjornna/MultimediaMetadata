package org.hygga.web;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class RequestParameterParser implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 9138194046976490046L;

    public String getParameter(String name) {
	HttpServletRequest req = (HttpServletRequest) FacesContext
		.getCurrentInstance().getExternalContext().getRequest();
	return req.getParameter(name);
    }

    public Long getParameterAsLong(String name) {
	String param = getParameter(name);
	if (param == null) {
	    return null;
	} else {
	    return Long.valueOf(param);
	}
    }
}
