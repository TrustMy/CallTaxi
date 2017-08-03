package com.trust.shengyu.calltaxidriver.tools.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trust.shengyu.calltaxidriver.R;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustDialog {

    public void showOrderDialog(Activity activity , final String startName , final String endName , final int money){
        final Dialog dialog = new Dialog(activity, R.style.customDialog);

        View view = LayoutInflater.from(activity).inflate(R.layout.order_dialog,null);
        ((TextView)view.findViewById(R.id.order_dialog_msg)).setText("起点:"+startName+"终点:"+endName + "|预估费用:"+money+"元");
        ((Button) view.findViewById(R.id.order_dialog_determine)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trustDialogListener.resultOrderDialog(startName,endName,money);
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
        }
        cacelButton(dialog ,(Button) view.findViewById(R.id.order_dialog_cancel));
    }

    public interface onTrustDialogListener{
        void resultOrderDialog(String startName,String endName,int taxiCast);
    }

    private onTrustDialogListener trustDialogListener;

    public void setOnTrustDialogListener(onTrustDialogListener trustDialogListener){
        this.trustDialogListener = trustDialogListener;
    }


    public void cacelButton(final Dialog dialog , Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
