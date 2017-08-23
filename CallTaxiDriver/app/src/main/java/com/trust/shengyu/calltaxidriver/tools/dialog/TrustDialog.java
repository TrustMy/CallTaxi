package com.trust.shengyu.calltaxidriver.tools.dialog;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.tools.L;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustDialog {
    //显示订单消息dialog
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
    //订单消息回调
    public interface onTrustDialogListener{
        void resultOrderDialog(String startName,String endName,int taxiCast);
    }

    private onTrustDialogListener trustDialogListener;

    public void setOnTrustDialogListener(onTrustDialogListener trustDialogListener){
        this.trustDialogListener = trustDialogListener;
    }


    public TrustDialog showExceptionDescription(Activity activity){
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.order_exception_description_dialog,null);
        final EditText description = (EditText) view.findViewById(R.id.order_exception_description_ed);
        view.findViewById(R.id.order_exception_description_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = description.getText().toString().trim();
                dialogClickListener.resultMessager(msg);
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
        }
        cacelButton(dialog,(Button)view.findViewById(R.id.order_exception_description_cancel));
        return this;
    }
    public interface onDialogClickListener{
        void resultMessager(String msg);
    }
    public onDialogClickListener dialogClickListener;

    public void setOnClickListener(onDialogClickListener dialogClickListener){
        this.dialogClickListener = dialogClickListener;
    }

    //通用取消按钮
    public void cacelButton(final Dialog dialog , Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void showWaitDialog(Activity activity){

        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.order_wait_dialog,null);
        final LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("imgs.json");
        lottieAnimationView.setImageAssetsFolder("images");
        lottieAnimationView.loop(true);


        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                L.d("onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                L.d("onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animator) {

                L.d("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                L.d("onAnimationRepeat");
            }
        });

        lottieAnimationView.playAnimation();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
        }
    }


    //取消订单 dialog

    public Dialog showErrorOrderDialog(final Activity activity , String msg){
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.error_order_dialog_layout,null);
        Button determineBtn = (Button) view.findViewById(R.id.error_order_dialog_determine);
        determineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.finish();
            }
        });
        TextView msgTv = (TextView) view.findViewById(R.id.error_order_dialog_msg);
        msgTv.setText(msg);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
        }

        return dialog;
    }
}
