package com.oilMap.client.user;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by 김현준 on 2015-04-07.
 */
public class ModifyPasswordFragment extends Fragment {

    User user = new User();

    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmPassword;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.modify_password, container, false);

        editOldPassword = (EditText) view.findViewById(R.id.modOldPassword);
        user.setPassword(editOldPassword.getText().toString());

        editNewPassword = (EditText) view.findViewById(R.id.modNewPassword);
        user.setPassword(editNewPassword.getText().toString());

        editConfirmPassword = (EditText) view.findViewById(R.id.modRePasswrod);
        user.setPassword(editConfirmPassword.getText().toString());

        return view;
    }

    public void mOnClick(View v){

        switch (v.getId()) {

            case R.id.btnModifyOk:
                new ModifyPasswordAsyncTask().execute(user);
                break;

            case R.id.btnModifyCancel:
                editOldPassword.setText("");
                editNewPassword.setText("");
                editConfirmPassword.setText("");

                TextView OldPasswordView = (TextView) getView().findViewById(R.id.OldPasswordView);
                OldPasswordView.setVisibility(View.INVISIBLE);

                TextView NewPWView = (TextView) getView().findViewById(R.id.PWView);
                NewPWView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private class ModifyPasswordAsyncTask extends AsyncTask<User, Void, Map<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String, Object> doInBackground(User... users) {

            if(users[0] == null){
                return null;
            }

            try {
                String url = getString(R.string.contextPath) + "/user/updateNewPassword";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, users[0], Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;

            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                return null;
            }

        }

        //================================== 아래는 협의 후 수정해야할 부분 =====================================//
        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            Log.d("updateNewPassword", map.toString());

            //정보 일치 시 새로운 비밀번호 뷰에 표시
            if((Boolean)map.get("success")){

                if(map.get("newPassword") != null){
                    TextView newPasswordView = (TextView) getView().findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.VISIBLE);
                    newPasswordView.setText(map.get("newPassword").toString());
                    newPasswordView.setTextColor(0xffff0000);
                }
                else{
                    TextView newPasswordView = (TextView) getView().findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.INVISIBLE);
                }
            }

            //입력정보 오류 or 등록되지 않은 정보
            else{

                if(map.get("messages") != null){
                    TextView newPasswordView = (TextView) getView().findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.VISIBLE);
                    newPasswordView.setText(map.get("messages").toString());
                    newPasswordView.setTextColor(0xffff0000);
                }

                else{
                    TextView newPasswordView = (TextView) getView().findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
