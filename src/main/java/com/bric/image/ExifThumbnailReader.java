/*
 * @(#)ExifThumbnailReader.java
 *
 * $Date$
 *
 * Copyright (c) 2011 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * http://javagraphics.java.net/
 * 
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package com.bric.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.bric.io.CombinedInputStream;
import com.bric.io.MeasuredInputStream;

/**
 * This extracts the thumbnail from JPEGs written in the EXIF file format.
 */
public class ExifThumbnailReader {
    private static boolean debug = false;

    /**
     * This extracts the thumbnail from a large JPEG, if it can be found. If a
     * thumbnail cannot be found this returns null without throwing an
     * exception.
     * <p>
     * Not all JPEG files will have thumbnails, but it is becoming more common
     * for large photos to have embedded thumbnails.
     * 
     * @return the jpeg file of this thumbnail if it can be read, or null.
     */
    public static File getThumbnailFile(URL url) {
	InputStream in = null;
	try {
	    in = url.openStream();
	    byte[] scratchArray = new byte[128];
	    byte[] data = getThumbnailData(in, scratchArray);
	    if (data == null)
		return null;

	    int headerSize = writeExifHeader(scratchArray);
	    return writeJPEGFile(scratchArray, 0, headerSize, data, 2,
		    data.length - 2);
	} catch (Throwable t) {
	    return null;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		}
	    }
	}
    }

    /**
     * This extracts the thumbnail from a large JPEG file, if it can be found.
     * If a thumbnail cannot be found this returns null without throwing an
     * exception.
     * <p>
     * Not all JPEG files will have thumbnails, but it is becoming more common
     * for large photos to have embedded thumbnails.
     * 
     * @return the jpeg file of this thumbnail if it can be read, or null.
     */
    public static File getThumbnailFile(File file) {
	InputStream in = null;
	try {
	    in = new FileInputStream(file);
	    return getThumbnailFile(in);
	} catch (Throwable t) {
	    return null;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		}
	    }
	}
    }

    /**
     * This extracts the thumbnail from a large JPEG, if it can be found. If a
     * thumbnail cannot be found this returns null without throwing an
     * exception.
     * <p>
     * Not all JPEG files will have thumbnails, but it is becoming more common
     * for large photos to have embedded thumbnails.
     * <p>
     * The <code>InputStream</code> is not closed by this method.
     * 
     * @return the jpeg file of this thumbnail if it can be read, or null.
     */
    public static File getThumbnailFile(InputStream in) throws IOException {
	byte[] scratchArray = new byte[128];
	byte[] data = getThumbnailData(in, scratchArray);
	if (data == null)
	    return null;

	int headerSize = writeExifHeader(scratchArray);
	return writeJPEGFile(scratchArray, 0, headerSize, data, 2,
		data.length - 2);
    }

    /**
     * 
     * @param input
     *            a large JPEG file, possibly with an embedded thumbnail.
     * @param scratchArray
     *            an array of at least 128 bytes to use temporarily.
     * @return
     * @throws IOException
     */
    private static byte[] getThumbnailData(InputStream input,
	    byte[] scratchArray) throws IOException {
	MeasuredInputStream in = new MeasuredInputStream(input);
	byte[] b = scratchArray;
	boolean reverse = false;
	int offset = 12;
	read(in, b, 2, reverse);
	if (!(b[0] == -1 && b[1] == -40)) {
	    // this didn't have a SOI marker at the top: give up immediately.
	    if (debug)
		System.err.println("error 1");
	    return null;
	}
	// now we want to find an APP1 marker:
	read(in, b, 2, reverse);
	while (b[0] == -1 && b[1] != -31) {
	    // we found a marker but it's not an APP1 marker.
	    read(in, b, 2, reverse);
	    int dataSize = (b[0] & 0xff) * 256 + (b[1] & 0xff);
	    in.skip(dataSize);
	    read(in, b, 2, reverse);
	}
	if (!(b[0] == -1 && b[1] == -31)) {
	    // we didn't get our APP1 marker
	    if (debug)
		System.err.println("error 1.5");
	    return null;
	}

	read(in, b, 2, reverse);
	// this value +2 is where the image data for the main image starts:
	// long length = (b[0] & 0xff)*256+(b[1] & 0xff);
	read(in, b, 6, reverse);
	if (!(b[0] == 69 && b[1] == 120 && b[2] == 105 && b[3] == 102
		&& b[4] == 0 && b[5] == 0)) {
	    // this didn't have "Exif" followed by a 0x00, so this isn't an exif
	    // file.
	    if (debug)
		System.err.println("error 2");
	    return null;
	}

	read(in, b, 2, reverse);
	if (b[0] == 73 && b[1] == 73) { // little endian
	    reverse = true;
	} else if (b[0] == 77 && b[1] == 77) { // big endian
	    // do nothing
	} else {
	    if (debug)
		System.err.println("error 3: " + b[0] + " " + b[1]);
	    return null;
	}

	read(in, b, 2, reverse);
	if (!(b[0] == 0 && b[1] == 42)) { // required byte in TIFF header
	    if (debug)
		System.err.println("error 4");
	    return null;
	}
	read(in, b, 4, reverse); // position of zero-eth IFD

	// the position of the zero-eth IFD
	long pos = (b[0] & 0xff) * 256 * 256 * 256 + (b[1] & 0xff) * 256 * 256
		+ (b[2] & 0xff) * 256 + (b[3] & 0xff);

	in.seek(pos + offset);

	IFD i0 = new IFD(in, reverse);

	read(in, b, 4, reverse);

	// the position of the next IFD
	pos = (b[0] & 0xff) * 256 * 256 * 256 + (b[1] & 0xff) * 256 * 256
		+ (b[2] & 0xff) * 256 + (b[3] & 0xff);

	i0.resolveIFDs(in, reverse, offset);
	if (debug)
	    System.out.println(i0);

	IFD i1 = null;
	while (pos != 0 && (i1 == null || i1.getJPEGPointer() < 0)) {
	    in.seek(pos + offset);

	    i1 = new IFD(in, reverse);
	    read(in, b, 4, reverse);
	    // the position of the next IFD
	    pos = (b[0] & 0xff) * 256 * 256 * 256 + (b[1] & 0xff) * 256 * 256
		    + (b[2] & 0xff) * 256 + (b[3] & 0xff);
	}

	if (i1 != null && i1.getJPEGPointer() > 0) {
	    pos = i1.getJPEGPointer();
	    int len = (int) i1.getJPEGLength();
	    if (len == 0) {
		if (debug)
		    System.err.println("error 4.5");
		return null;
	    }
	    byte[] thumbnailData = new byte[len];

	    in.seek(offset + pos);
	    read(in, thumbnailData, len, false);
	    if (!(thumbnailData[0] == -1 && thumbnailData[1] == -40)) {
		// didn't start with a SOI marker: something's wrong
		if (debug)
		    System.err.println("error 5");
		return null;
	    }
	    byte[] d = new byte[4];
	    System.arraycopy(thumbnailData, len - d.length, d, 0, d.length);
	    return thumbnailData;
	}
	return null;
    }

    /**
     * This extracts the thumbnail from a large JPEG file, if it can be found.
     * If a thumbnail cannot be found this returns null without throwing an
     * exception.
     * <p>
     * Not all JPEG files will have thumbnails, but it is becoming more common
     * for large photos to have embedded thumbnails.
     * 
     * @return the jpeg of this thumbnail if it can be read, or null.
     */
    public static BufferedImage getThumbnail(File file) {
	InputStream in = null;
	try {
	    in = new FileInputStream(file);
	    return getThumbnail(in);
	} catch (IOException e) {
	    return null;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * This extracts the thumbnail from a large JPEG, if it can be found. If a
     * thumbnail cannot be found this returns null without throwing an
     * exception.
     * <p>
     * Not all JPEG files will have thumbnails, but it is becoming more common
     * for large photos to have embedded thumbnails.
     * 
     * @return the jpeg of this thumbnail if it can be read, or null.
     */
    public static BufferedImage getThumbnail(URL url) {
	InputStream in = null;
	try {
	    in = url.openStream();
	    return getThumbnail(in);
	} catch (IOException e) {
	    return null;
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * This extracts the thumbnail from a large JPEG, if it can be found. If a
     * thumbnail cannot be found this returns null without throwing an
     * exception.
     * <p>
     * Not all JPEG files will have thumbnails, but it is becoming more common
     * for large photos to have embedded thumbnails.
     * <p>
     * The <code>InputStream</code> is not closed by this method.
     * 
     * @return the jpeg of this thumbnail if it can be read, or null.
     */
    public static BufferedImage getThumbnail(InputStream in) {
	try {
	    byte[] scratch = new byte[128];
	    byte[] thumbnailData = getThumbnailData(in, scratch);
	    if (thumbnailData == null)
		return null;

	    int headerSize = writeExifHeader(scratch);
	    InputStream jpegIn = new CombinedInputStream(
		    new ByteArrayInputStream(scratch, 0, headerSize),
		    new ByteArrayInputStream(thumbnailData, 2,
			    thumbnailData.length - 2), true, true);
	    return ImageIO.read(jpegIn);
	} catch (IOException e) {
	    return null;
	}
    }

    /**
     * Writes 42 bytes of header info for an exif JPEG file.
     * 
     * @param array
     *            an array of at least 42 bytes to write to.
     * @return the number of bytes written. This will be 42.
     */
    private static int writeExifHeader(byte[] array) {
	array[0] = -1; // Exif header
	array[1] = -40;
	array[2] = -1; // APP1 marker
	array[3] = -31;
	array[4] = 0; // length of APP1 marker
	array[5] = 38;
	array[6] = 69; // Exif marker
	array[7] = 120;
	array[8] = 105;
	array[9] = 102;
	array[10] = 0;
	array[11] = 0;
	array[12] = 73; // TIFF Header
	array[13] = 73;
	array[14] = 42;
	array[15] = 0;
	array[16] = 8;
	array[17] = 0;
	array[18] = 0;
	array[19] = 0;
	// 0th IFD
	array[20] = 1; // header: only write 1 value
	array[21] = 0;
	array[22] = 0; // FileSource, UNDEFINED [0 0 0 1] [0 0 0 3]
	array[23] = -93;
	array[24] = 7;
	array[25] = 0;
	array[26] = 1;
	array[27] = 0;
	array[28] = 0;
	array[29] = 0;
	array[30] = 3;
	array[31] = 0;
	array[32] = 0;
	array[33] = 0;

	array[34] = 0;
	array[35] = 0;
	array[36] = 0;
	array[37] = 0;
	array[38] = 0;
	array[39] = 0;
	array[40] = 0;
	array[41] = 0;

	return 42;
    }

    private static File writeJPEGFile(byte[] array1, int array1offset,
	    int array1size, byte[] array2, int array2offset, int array2size)
	    throws IOException {
	FileOutputStream out = null;
	try {
	    File file = File.createTempFile("thumbnail", ".jpeg");
	    out = new FileOutputStream(file);
	    out.write(array1, array1offset, array1size);
	    out.write(array2, array2offset, array2size);
	    return file;
	} finally {
	    if (out != null) {
		try {
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    static class IFD {
	static Hashtable TYPE_LUT = new Hashtable();
	static {
	    TYPE_LUT.put(new Integer(256), "Image Width");
	    TYPE_LUT.put(new Integer(257), "Image Length");
	    TYPE_LUT.put(new Integer(258), "BitsPerSample");
	    TYPE_LUT.put(new Integer(259), "Compression");
	    TYPE_LUT.put(new Integer(262), "PhotometricInterpretation");
	    TYPE_LUT.put(new Integer(270), "ImageDescription");
	    TYPE_LUT.put(new Integer(271), "Make");
	    TYPE_LUT.put(new Integer(272), "Model");
	    TYPE_LUT.put(new Integer(273), "StripOffsets");
	    TYPE_LUT.put(new Integer(274), "Orientation");
	    TYPE_LUT.put(new Integer(277), "SamplesPerPixel");
	    TYPE_LUT.put(new Integer(278), "RowsPerStrip");
	    TYPE_LUT.put(new Integer(279), "StripByteCounts");
	    TYPE_LUT.put(new Integer(282), "XResolution");
	    TYPE_LUT.put(new Integer(283), "YResolution");
	    TYPE_LUT.put(new Integer(284), "PlanarConfiguration");
	    TYPE_LUT.put(new Integer(296), "ResolutionUnit");
	    TYPE_LUT.put(new Integer(301), "TranserFunction");
	    TYPE_LUT.put(new Integer(305), "Software");
	    TYPE_LUT.put(new Integer(306), "DateTime");
	    TYPE_LUT.put(new Integer(315), "Artist");
	    TYPE_LUT.put(new Integer(318), "WhitePoint");
	    TYPE_LUT.put(new Integer(319), "PrimaryChromaticities");
	    TYPE_LUT.put(new Integer(513), "JPEGInterchangeFormat");
	    TYPE_LUT.put(new Integer(514), "JPEGInterchangeFormatLength");
	    TYPE_LUT.put(new Integer(529), "YCbCrCoefficients");
	    TYPE_LUT.put(new Integer(530), "YCbCrSubSampling");
	    TYPE_LUT.put(new Integer(531), "YCbCrPositioning");
	    TYPE_LUT.put(new Integer(532), "ReferenceBlackWhite");
	    TYPE_LUT.put(new Integer(33432), "Copyright");
	    TYPE_LUT.put(new Integer(34665), "Exif IFD Pointer");
	    TYPE_LUT.put(new Integer(34853), "GPSInfo IFD Pointer");
	    TYPE_LUT.put(new Integer(33434), "ExposureTime");
	    TYPE_LUT.put(new Integer(33437), "FNumber");
	    TYPE_LUT.put(new Integer(34850), "ExposureProgram");
	    TYPE_LUT.put(new Integer(34852), "SpectralSensitivity");
	    TYPE_LUT.put(new Integer(34855), "ISOSpeedRatings");
	    TYPE_LUT.put(new Integer(34856), "OECF");
	    TYPE_LUT.put(new Integer(36864), "ExifVersion");
	    TYPE_LUT.put(new Integer(36867), "DateTimeOriginal");
	    TYPE_LUT.put(new Integer(36868), "DateTimeDigitized");
	    TYPE_LUT.put(new Integer(37121), "ComponentsConfiguration");
	    TYPE_LUT.put(new Integer(37122), "CompressedBitsPerPixel");
	    TYPE_LUT.put(new Integer(37377), "ShutterSpeedValue");
	    TYPE_LUT.put(new Integer(37378), "ApertureValue");
	    TYPE_LUT.put(new Integer(37379), "BrightnessValue");
	    TYPE_LUT.put(new Integer(37380), "ExposureBiasValue");
	    TYPE_LUT.put(new Integer(37381), "MaxApertureValue");
	    TYPE_LUT.put(new Integer(37382), "SubjectDistance");
	    TYPE_LUT.put(new Integer(37383), "MeteringMode");
	    TYPE_LUT.put(new Integer(37384), "LightSource");
	    TYPE_LUT.put(new Integer(37385), "Flash");
	    TYPE_LUT.put(new Integer(37386), "FocalLength");
	    TYPE_LUT.put(new Integer(37396), "SubjectArea");
	    TYPE_LUT.put(new Integer(37500), "MakerNote");
	    TYPE_LUT.put(new Integer(37510), "UserComment");
	    TYPE_LUT.put(new Integer(37520), "SubSecTime");
	    TYPE_LUT.put(new Integer(37521), "SubSecTimeOriginal");
	    TYPE_LUT.put(new Integer(37522), "SubSecTimeDigitized");

	    TYPE_LUT.put(new Integer(40960), "FlashpixVersion");
	    TYPE_LUT.put(new Integer(40961), "ColorSpace");
	    TYPE_LUT.put(new Integer(40962), "PixelXDimension");
	    TYPE_LUT.put(new Integer(40963), "PixelYDimension");
	    TYPE_LUT.put(new Integer(40964), "RelatedSoundFile");
	    TYPE_LUT.put(new Integer(40965), "Interoperability IFD Pointer");
	    TYPE_LUT.put(new Integer(41483), "FlashEnergy");
	    TYPE_LUT.put(new Integer(41484), "SpatialFrequencyResponse");
	    TYPE_LUT.put(new Integer(41486), "FocalPlaneXResolution");
	    TYPE_LUT.put(new Integer(41487), "FocalPlaneYResolution");
	    TYPE_LUT.put(new Integer(41488), "FocalPlaneResolutionUnit");
	    TYPE_LUT.put(new Integer(41492), "SubjectLocation");
	    TYPE_LUT.put(new Integer(41493), "ExposureIndex");
	    TYPE_LUT.put(new Integer(41495), "SensingMethod");
	    TYPE_LUT.put(new Integer(41728), "FileSource");
	    TYPE_LUT.put(new Integer(41729), "SceneType");
	    TYPE_LUT.put(new Integer(41730), "CFAPattern");
	    TYPE_LUT.put(new Integer(41985), "CustomRendered");
	    TYPE_LUT.put(new Integer(41986), "ExposureMode");
	    TYPE_LUT.put(new Integer(41987), "WhiteBalance");
	    TYPE_LUT.put(new Integer(41988), "DigitalZoomRatio");
	    TYPE_LUT.put(new Integer(41989), "FocalLengthIn35mmFilm");
	    TYPE_LUT.put(new Integer(41990), "SceneCaptureType");
	    TYPE_LUT.put(new Integer(41991), "GainControl");
	    TYPE_LUT.put(new Integer(41992), "Contrast");
	    TYPE_LUT.put(new Integer(41993), "Saturation");
	    TYPE_LUT.put(new Integer(41994), "Sharpness");
	    TYPE_LUT.put(new Integer(41995), "DeviceSettingDescription");
	    TYPE_LUT.put(new Integer(41996), "SubjectDistanceRange");
	    TYPE_LUT.put(new Integer(42016), "ImageUniqueID");
	}
	Vector v = new Vector();
	IFD exifIFDPtr;
	// IFD interopIFDPtr;
	int size;

	public IFD(InputStream in, boolean reverse) throws IOException {
	    byte[] sizeArray = new byte[2];
	    read(in, sizeArray, 2, reverse);
	    size = (sizeArray[0] & 0xff) * 256 + (sizeArray[1] & 0xff);

	    for (int a = 0; a < size; a++) {
		byte[] b1 = new byte[2];
		read(in, b1, 2, reverse);
		long k = evaluate(b1);

		boolean relatedToThumbnail = k == 513 || k == 514;

		if (debug || relatedToThumbnail) {
		    byte[] b2 = new byte[2];
		    byte[] b3 = new byte[4];
		    byte[] b4 = new byte[4];
		    read(in, b2, 2, reverse);
		    read(in, b3, 4, reverse);
		    read(in, b4, 4, reverse);
		    byte[][] b = new byte[][] { b1, b2, b3, b4 };
		    v.add(b);
		} else {
		    in.skip(10);
		}
	    }
	}

	public void resolveIFDs(MeasuredInputStream in, boolean reverse,
		int offset) throws IOException {
	    for (int a = 0; a < v.size(); a++) {
		byte[][] b = (byte[][]) v.get(a);
		long k = evaluate(b[0]);
		if (k == 34665) {
		    in.seek(evaluate(b[3]) + offset);
		    exifIFDPtr = new IFD(in, reverse);
		}
	    }
	    if (exifIFDPtr != null)
		exifIFDPtr.resolveIFDs(in, reverse, offset);
	}

	public long getJPEGPointer() {
	    for (int a = 0; a < v.size(); a++) {
		byte[][] b = (byte[][]) v.get(a);
		if (evaluate(b[0]) == 513) {
		    return evaluate(b[3]);
		}
	    }
	    return -1;
	}

	public long getJPEGLength() {
	    for (int a = 0; a < v.size(); a++) {
		byte[][] b = (byte[][]) v.get(a);
		if (evaluate(b[0]) == 514) {
		    return evaluate(b[3]);
		}
	    }
	    return -1;
	}

	private static long evaluate(byte[] b) {
	    long sum = 0;
	    for (int a = 0; a < b.length; a++) {
		sum = sum + (b[a] & 0xff);
		if (a != b.length - 1)
		    sum = sum * 256;
	    }
	    return sum;
	}

	public String toString() {
	    StringBuffer sb = new StringBuffer();
	    for (int a = 0; a < v.size(); a++) {
		byte[][] b = (byte[][]) v.get(a);
		int type = (b[0][0] & 0xff) * 256 + b[0][1];
		String typeName = (String) TYPE_LUT.get(new Integer(type));
		if (typeName == null) {
		    sb.append("UNKNOWN ");
		} else {
		    sb.append(typeName);
		    sb.append(' ');
		}
		sb.append("(" + type + ") ");

		type = (b[1][0] & 0xff) * 256 + b[1][1];
		if (type == 1) {
		    sb.append("BYTE ");
		} else if (type == 2) {
		    sb.append("ASCII ");
		} else if (type == 3) {
		    sb.append("SHORT ");
		} else if (type == 4) {
		    sb.append("LONG ");
		} else if (type == 5) {
		    sb.append("RATIONAL ");
		} else if (type == 7) {
		    sb.append("UNDEFINED ");
		} else if (type == 9) {
		    sb.append("SLONG ");
		} else if (type == 10) {
		    sb.append("SRATIONAL ");
		}
		for (int i = 2; i < b.length; i++) {
		    sb.append(ExifThumbnailReader.toString(b[i]) + ' ');
		}
		sb.append('\n');
	    }
	    if (exifIFDPtr != null) {
		sb.append("Exif IFD Pointer:\n" + exifIFDPtr);
	    }
	    return sb.toString();
	}
    }

    private static String toString(byte[] b) {
	StringBuffer sb = new StringBuffer();
	sb.append('[');
	sb.append((b[0] & 0xff));
	for (int a = 1; a < b.length; a++) {
	    sb.append(' ');
	    sb.append((b[a] & 0xff));
	}
	sb.append(']');
	return sb.toString();
    }

    private static void read(InputStream in, byte[] dest, int amt,
	    boolean reverse) throws IOException {
	int ctr = 0;
	int t = in.read(dest, ctr, amt - ctr);
	while (t + ctr != amt && t != -1) {
	    ctr += t;
	    t = in.read(dest, ctr, amt - ctr);
	}
	if (t != -1)
	    ctr += t;

	if (ctr != amt) {
	    throw new EOFException();
	}

	if (reverse) {
	    t = amt / 2;
	    for (int i = 0; i < t; i++) {
		byte k = dest[amt - 1 - i];
		dest[amt - 1 - i] = dest[i];
		dest[i] = k;
	    }
	}
    }
}