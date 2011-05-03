package org.hygga.picture.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hygga.pictureservice.PictureService;
import org.jboss.logging.Logger;

@WebServlet("/picture")
public class PictureServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 4167449248874708614L;
    @EJB
    PictureService pictureService;
    Logger log = Logger.getLogger(PictureServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	String pictureId = req.getParameter("pictureId");
	if (pictureId == null) {
	    throw new IllegalArgumentException(
		    "PictureID is null - set parameter pictureId to a number");
	}
	Long id = Long.valueOf(pictureId);
	String path = pictureService.getAbsolutePathToPicture(id);
	File imgFile = new File(path);
	FileInputStream in = new FileInputStream(imgFile);
	OutputStream out = resp.getOutputStream();
	resp.setContentLength((int) imgFile.length());
	resp.setContentType("image/jpeg");
	byte[] buf = new byte[1024];
	int count = 0;
	while ((count = in.read(buf)) >= 0) {
	    out.write(buf, 0, count);
	}
	in.close();
	out.flush();
	out.close();

    }


}
