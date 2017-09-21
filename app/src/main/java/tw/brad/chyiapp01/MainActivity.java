package tw.brad.chyiapp01;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import static tw.brad.chyiapp01.MainApp.sp;
import static tw.brad.chyiapp01.MainApp.speditor;

public class MainActivity extends AppCompatActivity {
    private EditText account, passwd;

    private String inputAccount, inputPasswd;

    private UIHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();

        account = (EditText)findViewById(R.id.account);
        passwd = (EditText)findViewById(R.id.passwd);

        inputAccount = sp.getString("account","");
        inputPasswd = sp.getString("passwd","");
        account.setText(inputAccount);

        check();

    }

    public void login(View view){
        inputAccount = account.getText().toString();
        inputPasswd = passwd.getText().toString();
        check();
    }

    private void check(){
        new Thread(){
            @Override
            public void run() {
                try {
                    String url = "http://www.chyi.com.tw/api/api.AppQuery_exec.php";
                    MultipartUtility mu = new MultipartUtility(url,"","UTF-8");
                    mu.addFormField("action","LoginCheck");
                    mu.addFormField("deviceKey","Android");
                    mu.addFormField("loginUser",inputAccount);
                    mu.addFormField("loginPass",inputPasswd);
                    List<String> ret = mu.finish();
                    parseJSON(ret.get(0));

                }catch (Exception e){
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }

    private void parseJSON(String json){
        // {} ==> JSONObject
        // [] ==> JSONArray

//        json = "{\"success\":\"false\",\"error\":\"\"}";
        json = "{\"success\":false,\"error\":\"\"}";
        Log.i("brad", json);
        try {
            JSONObject root = new JSONObject(json);
            boolean isOK = root.getBoolean("success");
//            // "key":value ==> "key":true ==> boolean
            if (isOK){
                speditor.putString("account", inputAccount);
                speditor.putString("passwd", inputPasswd);
                speditor.commit();

                Intent it = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(it);
                finish();
            }else{
                handler.sendEmptyMessage(0);
            }
        }catch(Exception e){
            Log.i("brad", e.toString());
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, "Login ERROTR", Toast.LENGTH_SHORT).show();
        }
    }



}
