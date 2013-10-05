package es.jiayu.datawidgetjiayu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

public class DummyDataActivity extends Activity {

    private static final int DELAYED_MESSAGE = 1;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == DELAYED_MESSAGE) {
                    DummyDataActivity.this.finish();
                }
                super.handleMessage(msg);
            }
        };

        Intent brightnessIntent = this.getIntent();
        float brightness = brightnessIntent.getFloatExtra("brightness value", 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);

        Message message = handler.obtainMessage(DELAYED_MESSAGE);
        //this next line is very important, you need to finish your activity with slight delay
        handler.sendMessageDelayed(message,1000);

    }
}