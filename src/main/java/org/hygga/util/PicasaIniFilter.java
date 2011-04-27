package org.hygga.util;

import java.io.File;
import java.io.FilenameFilter;

public class PicasaIniFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
	return name.equals(".picasa.ini");
    }

}
