package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oilMap.client.R;
import com.oilMap.client.info.NavigationActivity;

public class LoginActivity extends Activity {

    private BackPressCloseHandler backPressCloseHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public  void onBackPressed(){
        backPressCloseHandler.onBackpressed();
    }


    public void mOnClick(View v) {
        // View v 에 어떤정보를 클릭했는가 하는 정보가 들어있다.
        EditText edittxt = (EditText) findViewById(R.id.txtid);
        EditText edittxt1 = (EditText) findViewById(R.id.txtpw);
        String id = edittxt.getText().toString();
        String pw = edittxt1.getText().toString();

        switch (v.getId()) {
            // 클릭한 버튼의 아이디가 리턴된다.
            case R.id.btnLogin:
                if ("guest".equals(id) && "1234".equals(pw)) {
                    String loginyes = "로그인에 성공하였습니다.";
                    Toast.makeText(LoginActivity.this, loginyes, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, NavigationActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    String loginno = "로그인에 실패하였습니다.";
                    Toast.makeText(LoginActivity.this, loginno, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnRegister:
                Intent userRegIntent = new Intent(this, UserRegisterActivity.class);
                startActivity(userRegIntent);
                finish();
                break;

            case R.id.btnFind:
                Intent findInform = new Intent(this, FindInformationActivity.class);
                startActivity(findInform);
                finish();
                break;

        }
    }
}