package com.trust.shengyu.rentalcarclient.activitys.checkinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.dialog.TrustDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.MSG_CHOOSE_IMG;


public class IdCardActivity extends BaseActivity {
    private static final int CHOODE_PHOTO = 2;
    @BindView(R.id.id_card_positive_btn)
    ImageView idCardPositiveBtn;
    @BindView(R.id.id_card_negative_btn)
    ImageView idCardNegativeBtn;
    @BindView(R.id.id_card_next_btn)
    Button idCardNextBtn;
    @BindView(R.id.base_back)
    ImageView baseBack;
    @BindView(R.id.title)
    TextView title;
    private int ImgType;// 0 正面   1 反面
    private String imgPath;//图片路径
    private final int REQUEST_CODE_TAKE_PICTURES = 0, REQUEST_CODE_ALBUM = 1;//拍照  相册
    TrustTools trustTools;
    private Context context = IdCardActivity.this;


    private Bitmap idCardPositiveBtnBitMap ,idCardNegativeBtnBitMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        ButterKnife.bind(this);

        trustTools = new TrustTools<>();
        initView();
    }

    private void initView() {
        baseSetOnClick(idCardPositiveBtn);
        baseSetOnClick(idCardNegativeBtn);
        baseSetOnClick(idCardNextBtn);
        baseSetOnClick(baseBack);
        baseBack.setVisibility(View.VISIBLE);
        title.setText("完善用户资料");
    }


    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.id_card_positive_btn:
                ImgType = 0;
                showChooseDialog();
                break;
            case R.id.id_card_negative_btn:
                ImgType = 1;
                showChooseDialog();
                break;
            case R.id.id_card_next_btn:

                if (null!=idCardPositiveBtnBitMap && null!= idCardNegativeBtnBitMap) {
                    startActivity(new Intent(context, DriversLicenseActivity.class));
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
        if (ImgType == 0) {//正面
            idCardPositiveBtnBitMap = bitmap;
            idCardPositiveBtn.setImageBitmap(bitmap);
        } else {//反面
            idCardNegativeBtnBitMap = bitmap;
            idCardNegativeBtn.setImageBitmap(bitmap);
        }
    }


}
