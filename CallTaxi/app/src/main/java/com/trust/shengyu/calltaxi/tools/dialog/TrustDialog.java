package com.trust.shengyu.calltaxi.tools.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.tools.TrustTools;

import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustDialog {
    //订单信息
    public Dialog showOrderDialog(Activity activity , final String startName , final String endName , final int money){
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

        return dialog;
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

    public void dissDialog(Dialog dialog){
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    //请求失败 dialog

    public Dialog showErrorOrderDialog(final Activity activity , String msg){
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.error_order_dialog_layout,null);
        Button determineBtn = (Button) view.findViewById(R.id.error_order_dialog_determine);
        determineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorOrderDialogListener.CallBack();
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

    public interface onErrorOrderDialogListener{void CallBack();};
    public onErrorOrderDialogListener errorOrderDialogListener;
    public void setErrorOrderDialogListener(onErrorOrderDialogListener errorOrderDialogListener){
        this.errorOrderDialogListener = errorOrderDialogListener;
    }

    /**
     * 等待dialog
     * @param activity
     * @return
     */
    public Dialog showWaitDialog(Activity activity){
        TrustTools trustTools = new TrustTools();
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.wait_dialog,null);
        ImageView logo = (ImageView) view.findViewById(R.id.order_wait_img);
        Glide.with(activity).load(R.mipmap.wait_logo).
                placeholder(R.mipmap.wait_log_one)
                .error(R.mipmap.wait_log_one).
                diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(1000).bitmapTransform(
                new RoundedCornersTransformation(activity,30,0,
                        RoundedCornersTransformation.
                                CornerType.ALL)).into(logo);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
            trustTools.setCountdown(15).setCountdownCallBack(new TrustTools.CountdownCallBack() {
                @Override
                public void callBackCountDown() {
                    dialog.dismiss();
                }
            });
        }
        return dialog;
    }

    /**
     * 等待司机接单
     * @param activity
     * @return
     */

    public Dialog showOrderWaitDialog(Activity activity){
        TrustTools trustTools = new TrustTools();
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.order_wait_dialog,null);
        Button cancler = (Button) view.findViewById(R.id.order_wait_dialog_cancel);
        cancler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.CallBack();
            }
        });
        ImageView gifImg = (ImageView) view.findViewById(R.id.order_dialog_img_gif);
        Glide.with(activity).load(R.drawable.wait_dialog).
                placeholder(R.mipmap.wait_log_one)
                .error(R.mipmap.wait_log_one).
                diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(1000).bitmapTransform(
                new RoundedCornersTransformation(activity,30,0,
                        RoundedCornersTransformation.
                                CornerType.ALL)).into(gifImg);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
            trustTools.setCountdown(120).setCountdownCallBack(new TrustTools.CountdownCallBack() {
                @Override
                public void callBackCountDown() {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
        }
        return dialog;
    }

    public interface onClick{
        void CallBack();
    };
    public onClick click;
    public void setOnClick(onClick click){
        this.click = click;
    }


    /**
     * 取消订单
     * @param activity
     * @return
     */
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


    /**
     * 长时间无相应提示用户继续等待或者取消订单
     * @param activity
     * @return
     */
    public TrustDialog showNoOneForALongTime(Activity activity){
        final Dialog dialog = new Dialog(activity, R.style.customDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.order_no_one_for_a_long_time_dialog,null);
        Button determineOrderBtn = (Button) view.findViewById(R.id.order_no_one_for_a_long_time_determine_btn);
        determineOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noOneForALongTimeListener.CallBack(view);
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        if(!activity.isFinishing()){
            dialog.show();
        }
        cacelButton(dialog,(Button) view.findViewById(R.id.order_no_one_for_a_long_time_wait_btn));
        return  this;
    }

    public interface  NoOneForALongTimeListener{void CallBack(View view);}
    public NoOneForALongTimeListener noOneForALongTimeListener;
    public void setNoOneForALongTimeListener(NoOneForALongTimeListener noOneForALongTimeListener){
        this.noOneForALongTimeListener = noOneForALongTimeListener;
    }

}
