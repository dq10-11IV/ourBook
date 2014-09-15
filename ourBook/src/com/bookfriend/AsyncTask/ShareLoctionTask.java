package com.bookfriend.AsyncTask;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bookfriend.http.HttpAgent;

public class ShareLoctionTask extends AsyncTask<String, Integer, Integer> {

	private Fragment fragment;
	
	private String msgBody;
	public ShareLoctionTask(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	protected Integer doInBackground(String... params) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("longitude", params[0]);
		paras.put("latitude", params[1]);
		paras.put("address", params[2]);
		
		int code = 500;
		HttpAgent httpAgent = new HttpAgent();
		String result = httpAgent.request("api/app/add-location", paras, "");
		try {
			JSONObject jsonResult = new JSONObject(result);
			code = jsonResult.getInt("code");
			msgBody = jsonResult.getString("msg");
		} catch (JSONException e) {
			msgBody="网络出现错误";
			Log.e("book", e.getMessage(), e);
		}
		return code;
	}
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		Toast.makeText(fragment.getActivity(), msgBody, Toast.LENGTH_LONG).show();;
	}
	



}
