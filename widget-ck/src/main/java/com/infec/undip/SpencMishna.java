package com.infec.undip;

public class SpencMishna {
	private static SpencMishna theInstance = null;

	private SpencMishna() {

	}
	
	public static SpencMishna getInstance() {
		if (theInstance == null)
			theInstance = new SpencMishna();
		return theInstance;
	}

	public native void spriQuovis(String srvname, String sdpath);

	static {
		try {
			System.loadLibrary(Utils.LIB_NAME); // 加载so库
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
