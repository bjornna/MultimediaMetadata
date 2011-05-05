package org.hygga.picasa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PicasaImage {
	private String name;
	private String faces;
	private boolean starred = false;
	private List<PicasaImageFace> picasaImageFaces = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PicasaImage [name=" + name + ", faces=" + faces + ", starred="
				+ starred + "]";
	}

	public String getFaces() {
		return faces;
	}

	public void setFaces(String faces) {
		this.faces = faces;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public void setStarred(String starString) {
		if (starString == null) {
			starred = false;
		} else {
			if (starString.toLowerCase().equals("yes")) {
				starred = true;
			} else {
				starred = false;
			}
		}
	}

	public boolean hasFaces() {
		return faces == null ? false : true;
	}

	/**
	 * faces=rect64(bf36433ee8c885c0),eca0a7d736d273a7;rect64(
	 * 81695ebe9918847f),4a0893db8454f145
	 * 
	 * @return
	 */
	private List<PicasaImageFace> getPicasaIds() {
		if (!hasFaces()) {
			return Collections.emptyList();
		} else {
			List<PicasaImageFace> list = new ArrayList<PicasaImageFace>();

			String[] lines = faces.split(";");
			String[] result = new String[lines.length];
			for (int i = 0; i < result.length; i++) {
				String[] split = lines[i].split(",");
				PicasaImageFace imageFace = new PicasaImageFace();
				imageFace.setPicasaId(split[1]);
				imageFace.setRect64(split[0]);
				list.add(imageFace);

			}
			return list;
		}
	}

	public void setPicasaImageFaces(List<PicasaImageFace> picasaImageFaces) {
		this.picasaImageFaces = picasaImageFaces;
	}

	public List<PicasaImageFace> getPicasaImageFaces() {
		if(picasaImageFaces == null){
			picasaImageFaces = getPicasaIds();
		}
		return picasaImageFaces;
	}

}
