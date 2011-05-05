package org.hygga.picasa;

import java.io.Serializable;

public class PicasaFaceRectangle implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -5362482220929143675L;
	private float left, top, right, bottom;

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

	public PicasaFaceRectangle() {
		left = 0;
		top = 0;
		right = 0;
		bottom = 0;
	}

	public PicasaFaceRectangle(float left, float top, float right, float bottom) {
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	/**
	 * 
	 * @param arr
	 *            (left, top, right, bottom).
	 */
	public PicasaFaceRectangle(float[] arr) {
		super();
		left = arr[0];
		top = arr[1];
		right = arr[2];
		bottom = arr[3];
	}

	public float getBottom() {
		return bottom;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}

	public float getRight() {
		return right;
	}

	public void setRight(float right) {
		this.right = right;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PicasaFace [left=").append(left).append(", top=")
				.append(top).append(", right=").append(right)
				.append(", bottom=").append(bottom).append("]");
		return builder.toString();
	}
}
