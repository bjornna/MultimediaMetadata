package org.hygga.util;

public class FileSizeConverter {

    
    public static Double getKB(Long bytes){
	return (double) (bytes/1024);
    }
    public static Double getMB(Long bytes){
	return getKB(bytes)/1024;
    }
}
