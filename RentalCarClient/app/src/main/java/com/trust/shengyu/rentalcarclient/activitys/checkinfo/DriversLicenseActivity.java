package com.trust.shengyu.rentalcarclient.activitys.checkinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.activitys.MainControllActivity;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.dialog.TrustDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.MSG_CHOOSE_IMG;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_ING;

public class DriversLicenseActivity extends BaseActivity {


    @BindView(R.id.drivers_license_dirver_btn)
    ImageView driversLicenseDirverBtn;//驾驶证
    @BindView(R.id.drivers_license_driving_btn)
    ImageView driversLicenseDrivingBtn;//行驶证
    @BindView(R.id.drivers_license_submit_btn)
    Button driversLicenseSubmitBtn;
    @BindView(R.id.base_back)
    ImageView baseBack;
    @BindView(R.id.title)
    TextView title;

    private int ImgType;// 0 驾驶证   1 行驶证
    private String imgPath;//图片路径
    private final int REQUEST_CODE_TAKE_PICTURES = 0, REQUEST_CODE_ALBUM = 1;//拍照  相册
    TrustTools trustTools;
    private Context context = DriversLicenseActivity.this;
    private Bitmap driversLicenseDirverBtnBitmap,driversLicenseDrivingBtnBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_license);
        ButterKnife.bind(this);
        trustTools = new TrustTools<>();
        initView();
    }

    private void initView() {
        baseSetOnClick(driversLicenseDirverBtn);
        baseSetOnClick(driversLicenseDrivingBtn);
        baseSetOnClick(driversLicenseSubmitBtn);

        baseSetOnClick(baseBack);
        baseBack.setVisibility(View.VISIBLE);
        title.setText("完善用户资料");
    }

    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.drivers_license_dirver_btn:
                ImgType = 0;
                showChooseDialog();
                break;

            case R.id.drivers_license_driving_btn:
                ImgType = 1;
                showChooseDialog();
                break;

            case R.id.drivers_license_submit_btn:
                if (null != driversLicenseDirverBtnBitmap && null != driversLicenseDrivingBtnBitmap) {
                    showToast("ok提交成功");
                    Intent intent = new Intent(context, MainControllActivity.class);
                    intent.putExtra("status",USER_STATUS_CERTIFIED_ING);
                    startActivity(intent);
                }else{
                    showToast(MSG_CHOOSE_IMG);
                }
                break;

            case R.id.base_back:
                finish();
                break;

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
        if (ImgType == 0) {//驾驶证
            driversLicenseDirverBtnBitmap = bitmap;
            driversLicenseDirverBtn.setImageBitmap(bitmap);
        } else {//行驶证
            driversLicenseDrivingBtnBitmap = bitmap;
            driversLicenseDrivingBtn.setImageBitmap(bitmap);
        }
    }

}
