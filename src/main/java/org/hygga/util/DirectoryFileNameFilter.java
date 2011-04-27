package org.hygga.util;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFileNameFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
	if (!pathname.isDirectory()) {
	    return false;
	}
	if (pathname.getName().startsWith(".")) {
	    return false;
	} else if (pathname.getName().startsWith("Pica")) {
	    return false;
	} else {
	    return true;
	}
    }

}
