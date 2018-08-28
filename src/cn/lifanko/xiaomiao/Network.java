package cn.lifanko.xiaomiao;
/**
 * Created By IntelliJ IDEA 15.0.2
 * Author lifanko lee
 * Date 2017/10/28
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Network {

	public static String name, stuId, ver = "2.0";
	public static String timeOnline = "0";

	public static String status = "";

	public static String login(String stuId, String password) {
		Network.stuId = stuId;
		String line = "无法联网，请检查网络后重试";
		try {
			URL url = new URL("http://ali.lifanko.cn/idp/miao/login.php?ver=" + ver + "&stuId=" + stuId + "&password="
					+ password);
			try {
				URLConnection connection = url.openConnection();
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader bReader = new BufferedReader(isr);

				StringBuilder builder = new StringBuilder();
				while ((line = bReader.readLine()) != null) {
					builder.append(line);
				}
				bReader.close();
				isr.close();
				is.close();

				line = builder.toString();
				if (line.substring(0, 3).equals("你好！")) {
					name = line.substring(3, line.length());
					line = "登录成功，" + line;
				} else if (line.equals("欢迎访问考勤系统登录接口。 1420实验室 By lifanko")) {
					line = "登录失败，请检查网络";
				}
			} catch (IOException e1) {
				System.err.println("登录失败！");
				// e1.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		}
		return line;
	}

	public static void update() {
		String line;
		try {
			URL url = new URL("http://ali.lifanko.cn/idp/miao/update.php?stuId=" + stuId);
			try {
				URLConnection connection = url.openConnection();
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader bReader = new BufferedReader(isr);

				StringBuilder builder = new StringBuilder();
				while ((line = bReader.readLine()) != null) {
					builder.append(line);
				}
				bReader.close();
				isr.close();
				is.close();

				line = builder.toString();

				status = line.substring(4, 8);
				timeOnline = line.substring(12);

				MouseMove.normal = true;

			} catch (IOException e1) {
				MouseMove.normal = false;
				// e1.printStackTrace();
			}
		} catch (MalformedURLException e) {
			MouseMove.normal = false;
			// e.printStackTrace();
		}
	}
}
