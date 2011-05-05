package org.hygga.picasa;

import org.jboss.logging.Logger;

public class PicasaFaceRectangleUtil {
	private static final float HEX_DIVIDE = 65536;
	private  Logger log = Logger.getLogger(PicasaFaceRectangleUtil.class);
	
	public static PicasaFaceRectangleUtil instance(){
		return new PicasaFaceRectangleUtil();
	}

	/**
	 * rect64(795575e08caa873f)
	 * 
	 * @param string
	 */
	public PicasaFaceRectangle parse(String string) {
		return parseRect64(find64String(string));

	}

	protected String find64String(String string) {
		String result = null;
		if (string.startsWith("rect64")) {
			int indexLeft = string.indexOf("(");
			int indexRight = string.indexOf(")");
			result = string.substring(indexLeft + 1, indexRight);

		} else {
			result = string;
		}
		if(16 != result.length()){
			throw new IllegalArgumentException(string + " should be 16 characters long, but was this " + result);
		}
		log.tracef("find64String from '%s' gave '%s' as result", string, result);
		return result;
	}

	protected PicasaFaceRectangle parseRect64(String string) {
		final float arr[] = rect64(find64String(string));
		final PicasaFaceRectangle face = new PicasaFaceRectangle(arr);
		return face;
	}

	protected float[] rect64(String string) {
		String[] arr = toArray(find64String(string));
		float[] num = new float[4];
		for (int i = 0; i < arr.length; i++) {
			Integer integer = Integer.valueOf(arr[i], 16);
			Float result = new Float(integer / HEX_DIVIDE);
			num[i] = result.floatValue();

		}
		return num;

	}

	protected String[] toArray(String string) {

		String a1 = string.substring(0, 4);
		String a2 = string.substring(4, 8);
		String a3 = string.substring(8, 12);
		String a4 = string.substring(12, 16);
		return new String[] { a1, a2, a3, a4 };
	}
}
