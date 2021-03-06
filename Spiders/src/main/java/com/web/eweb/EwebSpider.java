package com.web.eweb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.web.util.DataToTxt;

public class EwebSpider {

	private static File file;
	private static FileOutputStream stream = null;

	/**
	 * 加载文件和流
	 */
	static {
		file = DataToTxt.createFile("C:/Users/Administrator/Desktop/eweb.txt", file);
		stream = DataToTxt.createStream(file, stream);
	}

	/**
	 * 获得所有的值并关闭流
	 */
	public static void getElement() {
		readData(
				"http://106.3.35.197/eweb/jygl/zpfw.so?modcode=jygl_xjhxxck&subsyscode=zpfw&type=searchXjhxx&xjhType=all");
		for (int i = 1; i <= 10; i++) {
			readData(
					"http://106.3.35.197/eweb/wfc/app/pager.so?type=goPager&requestPager=pager&pageMethod=next&currentPage="
							+ i);
		}
		DataToTxt.closeStream(stream);
	}

	/**
	 * 获取需要的值并存入txt中
	 * @param url 发送请求的URL
	 */
	private static void readData(String url) {
		String result = sendGet(url);
		Document doc = Jsoup.parse(result);
		doc.setBaseUri(url);
		Elements elements = doc.select(".z_newsl li div");
		for (int i = 5; i < elements.size(); i++) {
			String view = elements.get(i).select("a").attr("onclick");
			if (view != "" && view != null) {
				String[] s = view.split("'");
				String urls = "http://106.3.35.197/eweb/jygl/zpfw.so?modcode=jygl_xjhxxck&subsyscode=zpfw&type=viewXjhxx&id="
						+ s[1] + " ";
				DataToTxt.wirte(urls, stream);
			}
			DataToTxt.wirte(elements.get(i).text() + " ", stream);
			if (Pattern.matches("(\\d{2})(：)(\\d{2})(\\-)(\\d{2})(：)(\\d{2})", elements.get(i).text())) {
				DataToTxt.enter(stream);
			}
		}
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			// String urlNameString = url + "?" + param;
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063");
			connection.setRequestProperty("Accept-Encoding", "utf-8");
			connection.setRequestProperty("Cookie",
					"JSESSIONID=AFD7FF787E0E372E21E5B65BF42339CE.488010; UM_distinctid=15e7f1d622a2fd-069091bd0928d2-5848211c-1fa400-15e7f1d622b862; CNZZDATA1256277512=1443434083-1505368803-http%253A%252F%252Fwww.swjtu.edu.cn%252F%7C1505368803; JSESSIONID=C6ED10441E78551B3502F5983B86A96F.488010");
			connection.setRequestProperty("Host", "106.3.35.197");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		getElement();
	}
}
