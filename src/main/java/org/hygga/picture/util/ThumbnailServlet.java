package org.hygga.picture.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hygga.pictureservice.PictureService;
import org.jboss.logging.Logger;

import com.bric.image.ExifThumbnailReader;

/**
 * Servlet that returns the thumbnail in the EXIF part of the image If no
 * thumbnail available - a default picture is returned
 * 
 * @author bna
 * 
 */
@WebServlet("/thumb")
public class ThumbnailServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -426682784131610826L;
    @EJB
    PictureService pictureService;
    private Logger log = Logger.getLogger(ThumbnailServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	String pictureId = req.getParameter("pictureId");
	if (pictureId == null) {
	    throw new IllegalArgumentException("Picture id is null");
	}
	Long id = Long.valueOf(pictureId);
	String path = pictureService.getAbsolutePathToPicture(id);
	File imgFile = new File(path);
	BufferedImage image = ExifThumbnailReader.getThumbnail(imgFile);
	if (image == null) {
	    log.debugf("ImageFile has no thumbnail : %s", imgFile);
	    resp.setContentType("image/png");
	    InputStream in = ThumbnailServlet.class
		    .getResourceAsStream("/JPEG.png");
	    OutputStream out = resp.getOutputStream();
	    byte[] buf = new byte[1024];
	    int count = 0;
	    while ((count = in.read(buf)) >= 0) {
		out.write(buf, 0, count);
	    }
	    in.close();
	    out.flush();
	    out.close();

	} else {

	    resp.setContentType("image/jpeg");
	    ImageIO.write(image, "JPEG", resp.getOutputStream());
	}

    }

}
