package org.hygga;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.hygga.picasa.PicasaFace;
import org.hygga.picasa.PicasaFacesRectUtil;

public class Picture extends JFrame{
private String hex64 = "59e22b4b8ebe7fca";
private String pictureFile = "src/test/resources/shelf/2009.09.06-Hafjell_Downhill/IMG_1972.JPG";
    /**
     * 
     */
    private static final long serialVersionUID = -6971362647551623799L;
    
    public Picture(){
	super("PictureFrame");
    }
    public void initUI() throws IOException{
	BufferedImage image = ImageIO.read(getImageStream());
	PicasaFace face = new PicasaFacesRectUtil().parseRect64(hex64);
	
	PicturePanel picturePanel = new PicturePanel();
	picturePanel.setImage(image);
	picturePanel.setFace(face);
	getContentPane().add(picturePanel);
	setVisible(true);
	pack();
	
    }
    private InputStream getImageStream() throws FileNotFoundException{
	return new FileInputStream(new File(pictureFile));
    }

    public static void main(String[] args) {
	Picture picture = new Picture();
	picture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	try {
	    picture.initUI();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
