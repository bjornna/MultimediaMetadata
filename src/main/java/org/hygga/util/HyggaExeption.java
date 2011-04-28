package org.hygga.util;

import java.io.FileNotFoundException;

import com.drew.imaging.jpeg.JpegProcessingException;

public class HyggaExeption extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 17075217534879886L;

    public HyggaExeption(JpegProcessingException e) {
	super(e);
    }

    public HyggaExeption(FileNotFoundException e) {
super(e);
    }

}
