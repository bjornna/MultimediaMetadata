package org.hygga.picasa;

import org.jboss.logging.Logger;

public class PicasaFacesRectUtil {
    private static final float HEX_DIVIDE = 65536;
    private Logger log = Logger.getLogger(PicasaFacesRectUtil.class);

    /**
     * rect64(795575e08caa873f)
     * 
     * @param string
     */
    public void parse(String string) {

    }

    public PicasaFace parseRect64(String string){
	final float arr[] = rect64(string);
	final PicasaFace face = new PicasaFace(arr);
	return face;
    }
    public float[] rect64(String string) {
	String[] arr = toArray(string);
	float[] num = new float[4];
	for (int i = 0; i < arr.length; i++) {
	    Integer integer = Integer.valueOf(arr[i], 16);
	    Float result = new Float(integer / HEX_DIVIDE);
	    num[i] = result.floatValue();

	}
	return num;

    }

    public String[] toArray(String string) {
	String a1 = string.substring(0, 4);
	String a2 = string.substring(4, 8);
	String a3 = string.substring(8, 12);
	String a4 = string.substring(12, 16);
	return new String[] { a1, a2, a3, a4 };
    }
}
