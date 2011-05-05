package org.hygga.picasa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PicasaIniData {
	private String name;
	private String description;
	private Map<String, PicasaImage> images = new HashMap<String, PicasaImage>();

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Collection<PicasaImage> getImages(){
	return images.values();
}
	public PicasaImage get(String imageName) {
		if (imageName == null)
			throw new IllegalArgumentException(
					"Parameter imageName must not be null");
		if (images.containsKey(imageName)) {
			return images.get(imageName);
		} else {
			return null;
		}
	}

	public void add(PicasaImage image) {
		if (image == null || image.getName() == null) {
			throw new IllegalArgumentException(
					"Image is null or name is null for image: " + image);
		}

		images.put(image.getName(), image);
	}
}
