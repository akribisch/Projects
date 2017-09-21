package com.web.uestc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.web.util.Unicode;

public class UestcSpider {

	private static MongoClient mongoClient=new MongoClient("localhost",27017);
	private static MongoDatabase mongobase= mongoClient.getDatabase("University");
	
	private static MongoCollection<org.bson.Document> mongoCollection=mongobase.getCollection("education");
	/**
	 * 获得所有的值并关闭流
	 */
	public static void getElement() {
		Map<String, String> data = new HashMap<>();
		for (int i = 1; i <= 10; i++) {
			data.put("page", "" + i + "");
			readData(data);
		}
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
			org.bson.Document readDoc=new org.bson.Document();
			for (String s3 : s2) {
				String[] s4 = s3.split("\":\"");
				System.out.println(s3);
				if (s4[0].equals("rec_No")) {
					String urls = "http://www.jiuye.org/new/career/info/Recruitment.html?id=" + s4[1] + "&rectype=0 ";
					readDoc.append("url", urls);
					
				} else if (s4[0].equals("rec_work_place") || s4[0].equals("rec_title")
						|| s4[0].equals("rec_enter_name")) {
					readDoc.append(s4[0], Unicode.decode(s4[1]));
				} else if (s4[0].equals("rec_publish_time")) {
					readDoc.append(s4[0], s4[1]);
				}
			}
			mongoCollection.insertOne(readDoc);
		}
	}

	public static void main(String[] args) {
		getElement();
	}
}
