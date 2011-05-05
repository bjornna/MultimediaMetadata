package org.hygga.picasa;

public class PicasaImageFace {
	private String picasaId;
	private String rect64;

	public String getPicasaId() {
		return picasaId;
	}

	public void setPicasaId(String picasaId) {
		this.picasaId = picasaId;
	}

	public String getRect64() {
		return rect64;
	}

	public void setRect64(String rect64) {
		this.rect64 = rect64;
	}

	public PicasaFaceRectangle getPicasaFace() {
		return PicasaFaceRectangleUtil.instance().parse(rect64);
	}
}
