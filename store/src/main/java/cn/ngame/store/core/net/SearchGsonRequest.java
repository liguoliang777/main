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
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.SearchResult;
import cn.ngame.store.bean.SearchWrapper;
import cn.ngame.store.bean.VideoInfo;
import cn.ngame.store.core.utils.Log;

/**
 * 自定义Volley的Request对象，用于将请求来的Json数据直接转化为对象
 * @author flan
 * @since   2015年11月7日
 */
public class SearchGsonRequest<T> extends Request<T> {

	private static final String TAG = SearchGsonRequest.class.getSimpleName();
	private Listener<T> successListener;
	private Gson gson;
	private Type mGsonType;

	public SearchGsonRequest(int method , String url, Listener<T> successListener, ErrorListener errorListener, Type type) {
		super(method, url, errorListener);
		
		this.successListener = successListener;
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
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
		
		try {
			String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.d(TAG,"======>>>>HTTP response 参数："+jsonStr);
			JsonResult<SearchWrapper> wrapperResultList = gson.fromJson(jsonStr, mGsonType);

			JsonResult<ArrayList<SearchResult>> jsonResult = new JsonResult<>();
			if(wrapperResultList != null && wrapperResultList.data != null){

				SearchWrapper searchWrapper = wrapperResultList.data;
				List<VideoInfo> videoInfoList = searchWrapper.videoList;
				List<GameInfo> gameInfoList = searchWrapper.gameList;

				ArrayList<SearchResult> searchResultList = new ArrayList<>();

				if(videoInfoList != null){
					for(VideoInfo info : videoInfoList){
						SearchResult searchResult = new SearchResult();
						searchResult.id = info.id;
						searchResult.name = info.videoName;
						searchResult.summary = info.videoIntroduce;
						searchResult.imgUrl = info.videoImageLink;
						searchResult.percentage = info.percentage;
						searchResult.type = 2;
						searchResultList.add(searchResult);
					}
				}

				if(gameInfoList != null){
					for (GameInfo info : gameInfoList){

						SearchResult searchResult = new SearchResult();
						searchResult.id = info.id;
						searchResult.name = info.gameName;
						searchResult.summary = info.gameDesc;
						searchResult.imgUrl = info.gameLogo;
						searchResult.percentage = info.percentage;
						searchResult.downloadCount = info.downloadCount;
						searchResult.fileSize = info.gameSize;
						searchResult.type = 1;
						searchResultList.add(searchResult);
					}
				}

				jsonResult.data = searchResultList;
				jsonResult.msg = wrapperResultList.msg;
				jsonResult.totals = wrapperResultList.totals;
				jsonResult.page = wrapperResultList.page;
				jsonResult.pageSize = searchResultList.size();

			}
			T result = (T) jsonResult;

			return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
		
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















