package com.hipits.manager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.hipits.model.Sign;

public class DataManager {

	private static DataManager dataManager;
	public static List<Sign> signs = new ArrayList<Sign>();

	public static DataManager getInstance() {
		if (dataManager == null) {
			dataManager = new DataManager();
		}
		return dataManager;
	}

	public void getParsingData() {

		final String[] codeNumbers = new String[]{"1.1056", "1.2001", "1.3016", "1.4012", "1.5018", "2.1001", "2.2002", "2.3002", "2.4004", "2.5004"};
		final HttpClient client = new DefaultHttpClient();
		final ResponseHandler<String> responseHandler = new BasicResponseHandler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < codeNumbers.length; i++) {
						HttpGet httpGet = new HttpGet("http://api.ibtk.kr/openapi/publicAssistanceFigurecover_api.do?code=" + codeNumbers[i]);
						String response = client.execute(httpGet, responseHandler);

						JSONObject source = new JSONObject(response).getJSONArray("ksp_list").getJSONObject(0);
						String description = source.getString("description");
						String imageUrl = source.getString("fileName");

						Bitmap bitmap = loadImageFromWeb(imageUrl);

						Sign sign = new Sign();
						sign.setBitmap(bitmap);
						sign.setDescription(description);

						signs.add(sign);

						File path = new File(Environment.getExternalStorageDirectory() + "/signs");

						if (!path.exists()) {
							path.mkdir();
						}

						savePngFile(bitmap, "" + i, path);
						saveDescription(description, path);

					}

				} catch (Exception e) {
					Log.e("Exception", e.getMessage());
				} 
			}
		}).start();
	}

	private void savePngFile(Bitmap bmp, String fileName, File path) {

		File file = new File(path, fileName + ".png");

		OutputStream out = null;

		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			// Bitmap.CompressFormat.JPEG 도 있음
			// 저장 품질 선택 가능 (현재 100%)
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Bitmap loadImageFromWeb(String url) {
		try {
			InputStream inputStream = (InputStream)new URL(url).getContent();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);			
			Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
			return bitmap;

		} catch (Exception e) {
			Log.e("LoadImageException", e.getMessage());
			return null;
		}
	}
	
	public void saveDescription(String description, File path) throws IOException {

		File file = new File(path, "description.txt");

		if (!file.exists()) {
			file.createNewFile();
		} 

		FileWriter fileWriter = new FileWriter(file, true);
		fileWriter.write(description + "\n");
		fileWriter.flush();
		fileWriter.close();

	}
	
	public void getDataList() throws Exception {

		String path = Environment.getExternalStorageDirectory() + "/signs/";

		BufferedReader reader = new BufferedReader(new FileReader(path + "description.txt"));

		int i = 0;
		String str = null;

		while ((str = reader.readLine()) != null) {
			String fileName = path + i +".png";
			Bitmap bitmap = BitmapFactory.decodeFile(fileName);

			Sign sign = new Sign();
			sign.setBitmap(bitmap);
			sign.setDescription(str);
			signs.add(sign);
			i++;
		}
		reader.close();
	}
}
