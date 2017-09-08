package com.trust.shengyu.rentalcarclient.activitys.checkinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.Config;
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.activitys.MainControllActivity;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans.ResultLoginBean;
import com.trust.shengyu.rentalcarclient.tools.dialog.TrustDialog;
import com.trust.shengyu.rentalcarclient.tools.gson.TrustAnalysis;
import com.trust.shengyu.rentalcarclient.tools.test.TimeSelector;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.MSG_DRIVER_TIME;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.MSG_USER_ID_NAME;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.MSG_USER_NAME;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_IMPROVE_USER_INFO;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_IMPROVE_USER_INFO;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_ERROR;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_ING;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_NOT_CERTIFIED;

public class UserMessageActivity extends BaseActivity {

    @BindView(R.id.user_msg_name_ed)
    EditText userMsgNameEd;
    @BindView(R.id.user_msg_id_number_ed)
    EditText userMsgIdNumberEd;
    @BindView(R.id.user_msg_drivers_license_time_btn)
    Button userMsgDriversLicenseTimeBtn;
    @BindView(R.id.user_msg_drivers_next)
    Button userMsgDriversNext;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.base_back)
    ImageView baseBack;
    @BindView(R.id.user_message_drivers_license_original)
    ImageView userMessageDriversLicenseOriginal;
    @BindView(R.id.user_message_drivers_license_copy)
    ImageView userMessageDriversLicenseCopy;
    @BindView(R.id.user_message_id_card_positive)
    ImageView userMessageIdCardPositive;
    @BindView(R.id.user_message_id_card_negative)
    ImageView userMessageIdCardNegative;
    @BindView(R.id.user_msg_file_name_ed)
    EditText userMsgFileNameEd;
    @BindView(R.id.user_msg_area)
    EditText userMsgArea;
    @BindView(R.id.user_msg_address)
    EditText userMsgAddress;

    private int ImgType;// 0 驾照正本   1 驾照副本  2 身份证正面 3 身份证反面
    private String imgPath;//图片路径
    private final int REQUEST_CODE_TAKE_PICTURES = 0, REQUEST_CODE_ALBUM = 1;//拍照  相册

    private String drivingTime, driveName, driveIdNumber;//驾照时间 , 司机姓名 , 身份证号
    private Context context = UserMessageActivity.this;
    private Bitmap driversLicenseOriginalBitmap, driversLicenseCopyBitmap, idCardPositiveBitmap,
            idCardNegativeBitmap;//驾照正本图,副本图,身份证正面,反面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        ButterKnife.bind(this);

        initView();

        int userStatus = getIntent().getIntExtra("userStatus", -1);
        switch (userStatus) {
            case USER_STATUS_NOT_CERTIFIED://没有认证
                showToast("没有认证");
                break;
            case USER_STATUS_CERTIFIED_ERROR://认证失败
                String msg = getIntent().getStringExtra("errorInfo");
                showToast("认证失败原因:" + msg);
                break;
        }
    }

    private void initView() {
        baseSetOnClick(userMsgDriversNext);
        baseSetOnClick(userMsgDriversLicenseTimeBtn);
        baseSetOnClick(baseBack);
        title.setText("完善用户资料");
        baseBack.setVisibility(View.VISIBLE);

        baseSetOnClick(userMessageDriversLicenseOriginal);
        baseSetOnClick(userMessageDriversLicenseCopy);
        baseSetOnClick(userMessageIdCardPositive);
        baseSetOnClick(userMessageIdCardNegative);
    }


    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.user_msg_drivers_license_time_btn:
                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        userMsgDriversLicenseTimeBtn.setText(time);
                        L.d("打印的时间:"+time);
                        L.d("打印的时间:"+TrustTools.getTime(time));
                    }
                }, "1977-01-01 00:00",TrustTools.getSystemTimeString());

                timeSelector.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR, TimeSelector.SCROLLTYPE.MONTH, TimeSelector.SCROLLTYPE.DAY, TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);
                timeSelector.show();
                break;
            case R.id.user_msg_drivers_next:
                submint();

                break;

            case R.id.base_back:
                finish();
                break;

            case R.id.user_message_drivers_license_original://驾照正本
                ImgType = 0;
                showChooseDialog();
                break;

            case R.id.user_message_drivers_license_copy://驾照副本
                ImgType = 1;
                showChooseDialog();
                break;

            case R.id.user_message_id_card_positive://身份证正面
                ImgType = 2;
                showChooseDialog();
                break;

            case R.id.user_message_id_card_negative://身份证反面
                ImgType = 3;
                showChooseDialog();
                break;
        }
    }

    //提交资料
    private void submint() {
        driveName = baseCheckIsNull(userMsgNameEd, MSG_USER_NAME);
        driveIdNumber = baseCheckIsNull(userMsgIdNumberEd, MSG_USER_ID_NAME);
        drivingTime = baseCheckIsNull(userMsgDriversLicenseTimeBtn, MSG_DRIVER_TIME);
        String fileId = baseCheckIsNull(userMsgFileNameEd,"档案号不能为空!");
        String area = baseCheckIsNull(userMsgArea,"区域不能为空!");
        String address = baseCheckIsNull(userMsgAddress,"详细地址不能为空!");

        if (null != driveName && null != driveIdNumber && null != drivingTime && null !=fileId && null != area &&
                null != address) {
            if (driveIdNumber.length() != 18) {
                showToast("身份证号位数有误!");
                return;
            } else {
                Map<String, Object> map = new WeakHashMap<>();
                map.put("cellPhone", Config.PHONE);
                map.put("name", driveName);
                map.put("idCard", driveIdNumber);
                map.put("archiveNumber", fileId);//档案号码
                map.put("licenseTime", TrustTools.getTime(drivingTime));//驾照领取时间
                map.put("area", area);//所在区域
                map.put("detailAddress", address);//详细地址
                map.put("faceCard", TrustTools.convertIconToString(idCardPositiveBitmap));//身份证正面
                map.put("reverseCard", TrustTools.convertIconToString(idCardNegativeBitmap));//身份证反面
                map.put("driverLicense", TrustTools.convertIconToString(driversLicenseOriginalBitmap));//驾照正本
                map.put("attachment", TrustTools.convertIconToString(driversLicenseCopyBitmap));//驾照副本
                requestCallBeack(URL_IMPROVE_USER_INFO, map, TAG_URL_IMPROVE_USER_INFO,
                        trustRequest.PUT, Config.token);
            }
        }
    }


    private void showChooseDialog() {
        trustDialog.showChooseGetImgType(context, ImgType).setOnChooseGetImgTypeListener(new TrustDialog.ChooseGetImgTypeListener() {
            @Override
            public void CollBack(int checkType, int imgType) {
                if (checkType == 0) {//拍照
                    takePictures(imgType);
                } else {//相册
                    album(imgType);
                }
            }
        });
    }

    /**
     * 拍照
     *
     * @param imgType
     */
    public void takePictures(int imgType) {
        imgPath = TrustTools.openCamera(this, REQUEST_CODE_TAKE_PICTURES);
    }

    /**
     * 相册
     *
     * @param imgType
     */
    public void album(int imgType) {
        TrustTools.openAlbum(this, REQUEST_CODE_ALBUM);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_TAKE_PICTURES://拍照
                if (resultCode == RESULT_OK) {
                    //将拍摄后的相片取出来
                    Bitmap bitmap = trustTools.bitmapCompressionRotate(imgPath);
                    showBitMap(bitmap);
                } else {
                    L.e("REQUEST_CODE_TAKE_PICTURES errror");
                }
                break;

            case REQUEST_CODE_ALBUM:
                if (resultCode == RESULT_OK) {
                    //从相册取出来
                    Bitmap bitmap = trustTools.getImages(data, this);
                    showBitMap(bitmap);

                } else {
                    L.e("REQUEST_CODE_ALBUM error");
                }
                break;

            default:
                L.e("default");
                break;

        }
    }


    private void showBitMap(Bitmap bitmap) {
        switch (ImgType) {
            case 0:
                driversLicenseOriginalBitmap = bitmap;
                userMessageDriversLicenseOriginal.setImageBitmap(driversLicenseOriginalBitmap);
                break;
            case 1:
                driversLicenseCopyBitmap = bitmap;
                userMessageDriversLicenseCopy.setImageBitmap(driversLicenseCopyBitmap);
                break;
            case 2:
                idCardPositiveBitmap = bitmap;
                userMessageIdCardPositive.setImageBitmap(idCardPositiveBitmap);
                break;
            case 3:
                idCardNegativeBitmap = bitmap;
                userMessageIdCardNegative.setImageBitmap(idCardNegativeBitmap);
                break;
        }

    }


    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type) {
            case TAG_URL_IMPROVE_USER_INFO:
                L.d("返回的结果 资料:" + msg);
                ResultLoginBean resultBean = TrustAnalysis.resultTrustBean(msg, ResultLoginBean.class);
                if (getResultStatus(resultBean.getStatus(), msg)) {
                    Intent intent = new Intent(context, MainControllActivity.class);
                    intent.putExtra("userStatus", USER_STATUS_CERTIFIED_ING);
                    startActivity(intent);
                }
                break;
        }
    }
}
