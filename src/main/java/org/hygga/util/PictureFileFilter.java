package org.hygga.util;

import java.io.File;
import java.io.FilenameFilter;

public class PictureFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
	String upper = name.toUpperCase();
	return upper.endsWith("JPG") || upper.endsWith("JPEG")
		|| upper.endsWith("PNG") || upper.endsWith("GIF");
    }

}
