package org.hygga;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.hygga.picasa.PicasaFace;

public class PicturePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 6625742512771065288L;
    private BufferedImage image;
    private PicasaFace face;

    public PicturePanel() {
	super();
    }

    public void setImage(BufferedImage image) {
	this.image = image;
    }

    public BufferedImage getImage() {
	return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
	final BufferedImage toShow = scale();
	Rectangle rectangle = new Rectangle();

	rectangle.setSize(10, 10);

	Graphics2D g2 = (Graphics2D) g;
	g2.drawImage(toShow, 0, 0, null);
	int left = (int) (face.getLeft() * toShow.getWidth());
	int right = (int) (face.getRight() * toShow.getWidth());
	int bottom = (int) (face.getBottom() * toShow.getHeight());
	int top = (int) (face.getTop() * toShow.getHeight());
	int width = right - left;
	int height = bottom - top;
	g2.setColor(Color.RED);
	g2.setStroke(new BasicStroke(7));

	g.drawRect(left , top, width, height);
	//g2.draw3DRect(left, top, width, height, true);

    }

    private BufferedImage scale() {

	double scale = 0.25;
	int width = (int) (image.getWidth() * scale);
	int height = (int) (image.getHeight() * scale);
	BufferedImage scaledImage = new BufferedImage(width, height,
		BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics2D = scaledImage.createGraphics();
	AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	graphics2D.drawImage(image, xform, null);
	graphics2D.dispose();
	return scaledImage;
    }

    public void setFace(PicasaFace face) {
	this.face = face;
    }

    public PicasaFace getFace() {
	return face;
    }

}
