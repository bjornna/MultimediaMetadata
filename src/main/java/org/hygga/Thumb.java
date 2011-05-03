package org.hygga;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.bric.image.ExifThumbnailReader;

public class Thumb extends JFrame {
    private String path = "src/test/resources/img/IMG_9826.jpg";

    public Thumb() {
	super("Thumbnail viewer");
	initUI();
    }

    private void initUI() {
	javax.swing.JLabel imglabel = new JLabel(createIcon());
	getContentPane().add(imglabel);
	pack();
	setVisible(true);
    }

    private ImageIcon createIcon() {
	File file  = new File(path);
	ImageIcon icon = new ImageIcon(ExifThumbnailReader.getThumbnail(file));
	return icon;
    }

    public static void main(String[] args) {
	Thumb thumb = new Thumb();
	thumb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
