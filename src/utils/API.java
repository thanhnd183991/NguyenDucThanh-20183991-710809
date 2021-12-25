package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;

/**
 * class cung cap cho viec gui request và nhan response tu server
 * 12/7/2021
 * @author Nguyen Duc Thanh 20183991
 * @version 1.0
 */
public class API {

	/**
	 * Date formmater
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * @author Nguyen Duc Thanh
	 * @param url   - endpoint server
	 * @return response: phan hoi tu server (dang string)
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		HttpURLConnection conn = setupConnection(url,  "GET");
		return readResponse(conn);
	}

	/**
	 * phuong thuc cho phep goi cac api dang POST (thanh toan...)
	 * @author Nguyen Duc Thanh
	 * @param url  - endpoint server
	 * @param data - du lieu truyen (dang JSON)
	 * @return response - phan hoi tu server (dang string)
	 * @throws IOException
	 */
	public static String post(String url, String data) throws IOException {
		System.out.println("data " + data);
		//cho phep PATCH protocol
		allowMethods("PATCH");
		
		//phan 1: setup
		HttpURLConnection conn = setupConnection(url, "PATCH");
		
		//phan 2: gui du lieu
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();
		
		//phan 3: doc du lieu tu server
		return readResponse(conn);
	}

	/**
	 * Phuong thuc cho phep goi cac api dang khac nhu PUT, PATCH, ...(chi hoat dong
	 * voi Java 11)
	 * 
	 * @deprecated chi hoat dong voi java 11
	 * @param methods goi cac giao thuc cho phep (PUT, PATCH)
	 * 
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	/**
	 * @param url endpoint server
	 * @param Configs.TOKEN doan ma xac thuc nguoi dung
	 * @param method giao thuc goi
	 * @return connection 
	 * @throws IOException
	 */
	private static HttpURLConnection setupConnection(String url, String method) throws IOException{
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + Configs.TOKEN);
		return conn;
	}
	/**
	 * nguyen duc thanh 20183991
	 * phuong thuc doc du lieu tra ve
	 * @param conn connection toi server
	 * @return phan hoi tu server
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in;
		String inputLine;
		if (conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		response.append(inputLine + "\n");
		in.close();
		LOGGER.info("Respone Info: " + response.toString());
//		System.out.println("my" + response.toString());
		return response.substring(0, response.length() - 1).toString();
	}
}
