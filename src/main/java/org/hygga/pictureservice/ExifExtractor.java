package org.hygga.pictureservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hygga.pictureservice.domain.ExifTag;
import org.hygga.util.HyggaExeption;
import org.jboss.logging.Logger;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectory;

public class ExifExtractor {
private Logger log = Logger.getLogger(ExifExtractor.class);
    public ExifExtractor() {

    }

    
    public List<ExifTag> extractFromFile(File file) throws HyggaExeption {
	try {
	    return extract(new FileInputStream(file));
	} catch (FileNotFoundException e) {

	    throw new HyggaExeption(e);
	}

    }
    @SuppressWarnings("unchecked")
    public List<ExifTag> extract(InputStream file) throws HyggaExeption{
	log.debug("extract from inputstream");
	try {
	    Metadata metadata = JpegMetadataReader.readMetadata(file);
	    Directory exit = metadata.getDirectory(ExifDirectory.class);
	    Iterator<Tag> tags = exit.getTagIterator();
	    List<ExifTag> exifMetadatas = new ArrayList<ExifTag>();
	    while (tags.hasNext()) {
		Tag tag = tags.next();
		tag.getTagName();
		ExifTag exifMetadata = new ExifTag(tag);
		exifMetadatas.add(exifMetadata);

	    }
	    
	    return exifMetadatas;
	} catch (JpegProcessingException e) {
	    throw new HyggaExeption(e);
	}
    }
}
