package com.trust.shengyu.calltaxi.activitys.orderstatus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.TrustTools;
import com.trust.shengyu.calltaxi.tools.beans.Bean;
import com.trust.shengyu.calltaxi.tools.beans.MqttResultBean;

import org.json.JSONObject;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStatusActivity extends BaseActivity {
    TextView textView;
    @BindView(R.id.map_order_end)
    Button mapOrderEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        textView = (TextView) findViewById(R.id.tttttttttt);
        mqttServer.appStatus = true;
        mqttServer.filterOrder();
        ButterKnife.bind(this);
        baseSetOnClick(mapOrderEnd);
    }


    @Override
    protected void baseSetOnClick(View v) {
        Map<String, Object> map = new WeakHashMap<>();
        Map<String, Object> maps = new WeakHashMap<>();
      switch (v.getId()){
          case R.id.map_order_end:
              maps.put("msg", "我不想坐车了!");
              map.put("status", true);
              map.put("time", TrustTools.getSystemTimeString());
              map.put("type", Config.MQTT_TYPE_REFUSED_ORDER);
              map.put("msg", maps);
              sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
              break;

      }

    }

    @Override
    public void resultMqttTypeStartOrder(MqttResultBean bean) {
        showSnackbar(textView, "已经上车!", null);
    }

    @Override
    public void resultMqttTypeEndOrder(MqttResultBean bean) {
        showSnackbar(textView, "已经下车", null);
    }

    @Override
    public void resultMqttTypeRefusedOrder(MqttResultBean bean) {
        L.e("司机拒绝接单");
        showSnackbar(textView, "司机拒绝:" + bean.getMsg(), null);
    }

    @Override
    public void resultMqttTypePlaceAnOrder(Bean bean) {
        showSnackbar(textView, "司机接单了!", null);
    }
}
