package com.oilMap.client.user;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by 김현준 on 2015-04-07.
 */
public class ModifyCarInformActivity extends Fragment {

    UserFuel userfuel = new UserFuel();
    User user = new User();

    private int carInformInteger = 2000;
    private int costInformInteger = 20000;
    private int periodInformInteger = 5;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.car_register, container, false);

        Spinner carInform = (Spinner) view.findViewById(R.id.regCar);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.car, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        carInform.setAdapter(adapter1);

        Spinner costInform = (Spinner) view.findViewById(R.id.regCost);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.cost, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        costInform.setAdapter(adapter2);

        Spinner periodInform = (Spinner) view.findViewById(R.id.regPeriod);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.period, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        periodInform.setAdapter(adapter3);

        return view;
    }

    public void mOnClick(View v) {

        userfuel.setDisplacement(carInformInteger);
        userfuel.setCost(costInformInteger);
        userfuel.setPeriod(periodInformInteger);

        switch (v.getId()) {
            case R.id.btnRegComplete:
                UserFuel userFuel = new UserFuel(userfuel.getDisplacement(), userfuel.getCost(), userfuel.getPeriod());
                userFuel.setUserPn(user.getPn());
                new CarRegisterAsyncTask().execute(userFuel);
                break;

            case R.id.btnRegCarClear:
                String carRegLate = "추후 차량등록 후 이용하세요.";
                Toast.makeText(getActivity(), carRegLate, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class CarRegisterAsyncTask extends AsyncTask<UserFuel, Void, Map<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String, Object> doInBackground(UserFuel... users) {

            if(users[0] == null){
                return null;
            }

            try {
                String url = getString(R.string.contextPath) + "/user/fuel/update";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, users[0], Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;

            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            Log.d("fuel/update", map.toString());

            if((Boolean)map.get("success")){
                String carRegYes = "차량 등록에 성공하였습니다.";
                Toast.makeText(getActivity(), carRegYes, Toast.LENGTH_SHORT).show();
            }else{
                String carRegNo = "차량 등록에 실패하였습니다." + map.get("messages").toString();
                Toast.makeText(getActivity(), carRegNo, Toast.LENGTH_SHORT).show();
            }
        }
    }
}