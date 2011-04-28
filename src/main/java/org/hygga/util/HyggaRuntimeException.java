package org.hygga.util;

public class HyggaRuntimeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6352545926948358985L;

    public HyggaRuntimeException(String string) {
	super(string);
    }

    public HyggaRuntimeException(String string, Exception e) {
	super(string, e);
    }

    public HyggaRuntimeException(Exception e) {
super(e);
    }

}
