package com.web.swufe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.web.util.DataToTxt;

public class SwufeSpider {

	private static File file;
	private static FileOutputStream stream;

	/**
	 * 加载文件和流
	 */
	static {
		file = DataToTxt.createFile("C:/Users/Administrator/Desktop/swufe.txt", file);
		stream = DataToTxt.createStream(file, stream);
	}

	private static void readData(String url) {
		String result = sendGet(url);
		Document doc = Jsoup.parse(result);
		doc.setBaseUri(url);
		Elements elements = doc.select(".table.table-hover tbody td");
		for (Element item : elements) {
			if(item.select("a").attr("href")!=""&&item.select("a").attr("href")!=null){
				String items="http://jobzpgl.swufe.edu.cn/RCPT/Pub01/xyzp/"+item.select("a").attr("href");
				DataToTxt.wirte(items + " ", stream);
			}
			if (Pattern.matches("((【)([\u4E00-\u9FA5]){2,}(】)(\\s))?(【)(\\d{2})(\\-)(\\d{2})(\\-)(\\d{2})(】)", item.text())
					||Pattern.matches("((\\s)?(【)([\u4E00-\u9FA5]){2,}(】)){1,}", item.text())) {
				
				String date=null;
				if(Pattern.matches("((【)([\u4E00-\u9FA5]){2,}(】)(\\s))?(【)(\\d{2})(\\-)(\\d{2})(\\-)(\\d{2})(】)", item.text())){
					if (item.text().length()>10) 
						date=item.text().substring(6, item.text().length()-1);
					date=item.text().substring(1, item.text().length()-1);
				}else if(Pattern.matches("((\\s)?(【)([\u4E00-\u9FA5]){2,}(】)){1,}", item.text()) ){
					date="17-01-01";
				}
				DataToTxt.wirte("20"+date + " ", stream);
			}else{
				DataToTxt.wirte(item.text()+ " ", stream);
			}
			if (Pattern.matches("(\\d{4})(\\-)(\\d{2})(\\-)(\\d{2})", item.text())) {
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
			connection.setRequestProperty("Cookie", "JSESSIONID=02EFBBAE570F5F771EA120637B4F40D9.tomcat103; JSESSIONID=89ECA7836D2B4EBC760E279AA9F175C5.tomcat103");
			connection.setRequestProperty("Host", "jy.scu.edu.cn");
			connection.setRequestProperty("Referer",
					"http://jy.scu.edu.cn/eweb/jygl/zpfw.so?modcode=jygl_xjhxxck&subsyscode=zpfw&type=searchXjhxx&xjhType=yjb");
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
		for (int i = 0; i < 10; i++) {
			readData("http://jobzpgl.swufe.edu.cn/RCPT/Pub01/xyzp/UnRecruitList.aspx?page="+i);
		}
	}
}
