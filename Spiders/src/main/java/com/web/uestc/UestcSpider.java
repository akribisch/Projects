package com.web.uestc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.web.util.DataToTxt;
import com.web.util.Unicode;

public class UestcSpider {

	private static File file;
	private static FileOutputStream stream = null;
	
	/**
	 * 加载文件和流
	 */
	static{
		file=DataToTxt.createFile("C:/Users/Administrator/Desktop/uestc.txt", file);
		stream=DataToTxt.createStream(file, stream);
	}
	
	/**
	 * 获得所有的值并关闭流
	 */
	public static void getElement() {
		Map<String, String> data = new HashMap<>();
		for (int i = 1; i <= 10; i++) {
			data.put("page", "" + i + "");
			readData(data);
		}
		DataToTxt.closeStream(stream);
	}

	/**
	 * 连接url，设置参数，获取需要的值并存入txt中
	 * @param data ajax参数
	 */
	private static void readData(Map<String, String> data) {
		data.put("rec_way", "1");
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.jiuye.org/new/sys/fore.php?op=listRecruit").data(data)
					.referrer("http://www.jiuye.org/new/career/info/CampusRec.html")
					.cookie("PHPSESSID",
							"8vjtuod47uc282n7u8nupq75m4; jiathis_rdc=%7B%22http%3A//www.jiuye.org/new/career/info/Recruitment.html%3Fid%3D17028%26rectype%3D0%22%3A2120990941%2C%22http%3A//www.jiuye.org/new/career/info/Recruitment.html%3Fid%3D17886%26rectype%3D0%22%3A0%7C1505359575072%2C%22http%3A//www.jiuye.org/new/career/info/Recruitment.html%3Fid%3D17029%26rectype%3D0%22%3A%220%7C1505359584400%22%7D")
					.post();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = doc.toString();
		result = result.substring(51, result.length() - 42);
		String[] data1 = result.split("\"\\},\\{\"");
		for (String s1 : data1) {
			String[] s2 = s1.split("\",\"");
			for (String s3 : s2) {
				String[] s4 = s3.split("\":\"");
				if (s4[0].equals("rec_No")) {
					String urls = "http://www.jiuye.org/new/career/info/Recruitment.html?id=" + s4[1] + "&rectype=0 ";
					DataToTxt.wirte(urls, stream);

				} else if (s4[0].equals("rec_work_place") || s4[0].equals("rec_title")
						|| s4[0].equals("rec_enter_name")) {
					DataToTxt.wirte(Unicode.decode(s4[1]) + " ", stream);
				} else if (s4[0].equals("rec_publish_time")) {
					DataToTxt.wirte(s4[1] + " ", stream);
				}
			}
			DataToTxt.enter(stream);
		}
	}

	public static void main(String[] args) {
		getElement();
	}
}
