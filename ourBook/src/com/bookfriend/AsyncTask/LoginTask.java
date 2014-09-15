package com.bookfriend.AsyncTask;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import com.bookfriend.Activity.LoginActivity;
import com.bookfriend.Activity.LoginTransitActivity;
import com.bookfriend.http.HttpAgent;

public class LoginTask extends AsyncTask<String, Integer, Integer> {
    private HttpAgent httpAgent = new HttpAgent();
    private HashMap<String, Object> paras = new HashMap<String, Object>();
    private String code = "";
    private JSONObject msgBody;
    private String userEmail = "";
    private String password = "";
    private LoginTransitActivity login;


    public  LoginTask(String _userEmail ,String _passWord,LoginTransitActivity _loginActivity){
        userEmail=_userEmail;
        password =_passWord;
        login = _loginActivity;
    }
    /**
     * 执行任务后调用
     */
    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if(code.equals("200")){
            SharedPreferences preferences  = login.getSharedPreferences("cloud", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", userEmail);
            editor.putString("password", password);
            editor.commit();
            Message messageg = Message.obtain();
            messageg.obj = "success";
            try {
                login.setNickName(msgBody.getString("userName")) ;
            }catch (Exception e ){
                e.printStackTrace();
            }
            login.handler.sendMessage(messageg);

        }else{
            Toast.makeText(login, "用户名或密码错误", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 执行任务之前调用
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        paras.put("email", userEmail);
        paras.put("passwd", password);
    }

    /**
     * 执行任务中调用更新任务进度
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        
        
    }

    /**
     * 执行任务
     */
    @Override
    protected Integer doInBackground(String... arg0) {
        String result = httpAgent.request("api/app/login", paras, "");
        try{
            JSONObject mess=new JSONObject(result);
            code = mess.getString("code");
            msgBody = mess.getJSONObject("msg");
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}