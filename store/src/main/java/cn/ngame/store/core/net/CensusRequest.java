/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.core.net;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.core.utils.Log;

/**
 * 自定义Volley的Request对象，用于将请求来的Json数据直接转化为对象
 * @author flan
 * @since   2015年11月7日
 */
public class CensusRequest<T> extends Request<T> {

	private static final String TAG = CensusRequest.class.getSimpleName();
	private Listener<T> successListener;
	private Gson gson;
	private Type mGsonType;

	public CensusRequest(int method , String url, Listener<T> successListener, ErrorListener errorListener, Type type) {
		super(method, url, errorListener);
		
		this.successListener = successListener;
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		//this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();// 不转换没有 @Expose 注解的字段   
		this.mGsonType = type;
	}

	@Override
	protected void deliverResponse(T response) {
		successListener.onResponse(response);
	}

	/**
	 * 后台将json字符串转换成JsonResult对象
	 * @param response Response对象
	 * @return 后台转换结果信息
     */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		

		/*String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		Log.d(TAG,"======>>>>HTTP response 参数："+jsonStr);

		T result = gson.fromJson(jsonStr, mGsonType);*/
		T result = (T)"success";
		return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));

		
	}

	/**
	 * 设置HTTP请求头
	 * @return http请求头信息
	 * @throws AuthFailureError
     */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {

		Map<String,String> params = new HashMap<>();
		params.put("Content-Type","application/json");

		return params;
	}

	/**
	 * 将挂参请求转成字节流
	 * @return http 请求的 body 体
	 * @throws AuthFailureError
     */
	@Override
	public byte[] getBody() throws AuthFailureError {

		Map<String,String> map = this.getParams();

		String str = gson.toJson(map);
		Log.d(TAG,"======>>>HTTP request 参数："+str);
		return str.getBytes();
	}
}















