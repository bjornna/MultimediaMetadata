package org.hygga.web;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@Stateful
@SessionScoped
public class Model {

   
    @Remove
    public void destroy() {}
}
