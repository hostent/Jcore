package com.jcore.Web;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcore.Frame.Request;
import com.jcore.Orm.XmlConfigManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ApiClient {

	public String serviceName = "";

	public ApiClient(String _serviceName) {

		serviceName = _serviceName;
	}

	public String getUrl() {
		
		String repsPath = ApiClient.class.getClassLoader().getResource("").getFile() + "reps\\";
		File file = new File(repsPath);
		
		return "";
	}

	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

	public <T> T post(Class<T> returnType, String method, Object[] params) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();

		Request req = new Request();
		UUID uuid = UUID.randomUUID();
		req.setId(uuid.toString());
		req.setMethod(method);
		req.setParams(params);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(req);

		RequestBody requestBody = RequestBody.create(JSON, json);
		okhttp3.Request request = new okhttp3.Request.Builder().url(getUrl()).post(requestBody).build();

		okhttp3.Response response = httpClient.newCall(request).execute();

		String str = response.body().string();
		T t = objectMapper.readValue(str, returnType);

		return t;

	}

}
