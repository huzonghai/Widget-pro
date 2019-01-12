package com.infec.undip;

public class Utils {
	
	public static final String EXECUTABLE 	= StrUtils.deCrypt("libservi.so");        // libccn.so
	public static final String LIB_NAME 	= StrUtils.deCrypt("servi");             // ccn
	
	public static String entryString(String str){
    	byte[] bytes = str.getBytes();
    	int length = bytes.length;
    	for (int i=0; i< length; i++){
    		bytes[i] ^= 16;
    	}
    	return new String(bytes);
    }
	

}
