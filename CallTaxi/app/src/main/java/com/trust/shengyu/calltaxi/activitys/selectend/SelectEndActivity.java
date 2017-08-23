package com.trust.shengyu.calltaxi.activitys.selectend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.tools.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectEndActivity extends BaseActivity {
    @BindView(R.id.select_end_actv)
    AutoCompleteTextView selectEndActv;
    @BindView(R.id.select_end_list)
    ListView selectEndList;
    @BindView(R.id.select_end_city)
    EditText selectEndCity;
    private int RESULT_CODE = 2;
    @BindView(R.id.finishbtn)
    Button finishbtn;
    private String city = "北京";
    private List<Tip> ml = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_end);
        ButterKnife.bind(this);

        baseSetOnClick(finishbtn);

        initView();
    }

    private void initView() {
        selectEndActv.addTextChangedListener(textWatcher);
        selectEndList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent rIntent = new Intent();
                rIntent.putExtra("endAddress", ml.get(pos).getName());
                rIntent.putExtra("endLatLng", ml.get(pos).getPoint());
                setResult(RESULT_CODE, rIntent);
                finish();
            }
        });


        selectEndCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                L.d("charSequence:"+charSequence);
                city = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /*
        List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", "this is "+i);
            map.put("address", "地点:"+i);
            listString.add(map);
        }
        SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.order_history_item,
                new String[] {"name","address"}, new int[] {R.id.orderhistory_item_tv, R.id.order_history_item_licensePlate});

        selectEndActv.setAdapter(aAdapter);
        aAdapter.notifyDataSetChanged();
        */
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String newText = charSequence.toString().trim();
            InputtipsQuery inputquery = new InputtipsQuery(newText, city);
            inputquery.setCityLimit(true);
            Inputtips inputTips = new Inputtips(SelectEndActivity.this, inputquery);
            inputTips.setInputtipsListener(inputtipsListener);
            inputTips.requestInputtipsAsyn();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {
        /**
         * 输入提示结果的回调
         * @param tipList
         * @param rCode
         */

        @Override
        public void onGetInputtips(List<Tip> tipList, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                ml = tipList;
                List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < tipList.size(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", tipList.get(i).getName());
                    map.put("address", tipList.get(i).getDistrict());
                    map.put("test", tipList.get(i).getAddress());
                    Log.d("lhh", "这是name: " + tipList.get(i).getName() + "|address:" +
                            tipList.get(i).getDistrict() + "|坐标:" + tipList.get(i).getPoint().getLatitude()
                            + "|" + tipList.get(i).getPoint().getLongitude() + "|" + tipList.get(i).getAddress()
                    );
                    listString.add(map);
                }
                SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_layout,
                        new String[]{"name", "address", "test"}, new int[]{R.id.poi_field_id, R.id.poi_value_id, R.id.poi_value_id_test});

                selectEndList.setAdapter(aAdapter);
                aAdapter.notifyDataSetChanged();

            } else {
                L.e("错误信息:" + rCode);
            }

        }
    };

    @Override
    public void baseClickResult(View v) {
        Intent rIntent = new Intent();
        rIntent.putExtra("end", "结束地址");
        setResult(RESULT_CODE, rIntent);
        finish();
    }


}
