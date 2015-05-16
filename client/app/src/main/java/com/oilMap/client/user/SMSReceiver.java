package com.oilMap.client.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice(){
        return price;
    }

    private String price;
    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMsg[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMsg[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            String msg = smsMsg[0].getMessageBody().toString();

            if(msg.contains("주유소"))
            {
                int stopLength = 0;
                int idx = msg.indexOf(",");

                for(int i=1; i<5; i++)
                {
                    if(msg.charAt(idx-i) == ' ')
                    {
                        stopLength = idx-i+1;
                        break;
                    }
                }
                setPrice(msg.substring(stopLength,idx + 4));
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}