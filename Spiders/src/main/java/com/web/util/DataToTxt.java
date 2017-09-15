package com.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataToTxt {

	/**
	 * 创建文档流
	 * @param file 文件
	 * @param stream 文档流
	 * @return
	 */
	public static FileOutputStream createStream(File file, FileOutputStream stream) {
		try {
			stream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stream;
	}

	/**
	 * 创建文件
	 * @param path 文件路径
	 * @param file 文件
	 * @return
	 */
	public static File createFile(String path, File file) {
		file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 关闭并刷新文档
	 * 
	 * @param stream
	 *            文档流
	 */
	public static void closeStream(FileOutputStream stream) {
		try {
			stream.flush(); // 写完之后刷新
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 字符串输入到文档流中
	 * 
	 * @param str
	 *            目标字符串
	 * @param stream
	 *            文档流
	 */
	public static void wirte(String str, FileOutputStream stream) {
		byte[] s = str.getBytes();
		try {
			stream.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 换行
	 * 
	 * @param stream
	 *            文档流
	 */
	public static void enter(FileOutputStream stream) {
		try {
			stream.write("\r\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
