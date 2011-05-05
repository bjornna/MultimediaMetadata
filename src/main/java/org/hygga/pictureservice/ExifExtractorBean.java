package org.hygga.pictureservice;

import java.io.File;
import java.util.Date;

import org.hygga.util.HyggaExeption;
import org.hygga.util.HyggaRuntimeException;
import org.jboss.logging.Logger;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

public class ExifExtractorBean {
    private boolean parsed = false;
    private Metadata metadata;
    private Logger log = Logger.getLogger(ExifExtractorBean.class);

    public ExifExtractorBean() {

    }

    public void parse(File file) throws HyggaExeption {
	try {
	    metadata = JpegMetadataReader.readMetadata(file);
	    parsed = true;
	} catch (JpegProcessingException e) {
	    throw new HyggaExeption(e);
	}
    }

    public Date getCreateDate() {
	checkParsed();
	Directory exifDir = metadata.getDirectory(ExifDirectory.class);
	try {
	    final Date date = exifDir
		    .getDate(ExifDirectory.TAG_DATETIME_DIGITIZED);
	    log.infof("createDate : %s", date);
	    return date;
	} catch (MetadataException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}

    }

    private void checkParsed() {
	if (!parsed) {
	    throw new HyggaRuntimeException("Must invoke parse first");
	}

    }
}
