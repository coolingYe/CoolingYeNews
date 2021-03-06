package com.example.coolingyenews.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.coolingyenews.R;
import com.example.coolingyenews.databinding.ActivitySettingsUserBinding;
import com.example.coolingyenews.utils.DensityUtil;
import com.example.coolingyenews.utils.NewsClass;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UploadAvatarUtil;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.example.coolingyenews.utils.HttpURL.localhost;

@Route(path = "/user/settings/user")
public class UserActivity extends AppCompatActivity {
    private ActivitySettingsUserBinding binding;
    private boolean onPauseByBooler,onPauseByBooler_1 = false;
    private File tempFile;
    private View viewDialog;
    private Dialog bottomDialog;
    /* ???????????? */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private ImageView iv_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings_user);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding.ivName.setRightDesc(UserClass.getUname());
        binding.ivSex.setRightDesc(UserClass.getSex());
        binding.ivBirthday.setRightDesc(UserClass.getBirthday());
        binding.ivSignature.setRightDesc(UserClass.getSignature());
        binding.ivUid.setRightDesc(String.valueOf(UserClass.getUid()));
        Glide.with(this)
                .load(UserClass.getIcon())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(binding.ivIcon);
        initOnClick();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initOnClick() {
        binding.ivName.setOnClickListener(v -> ARouter.getInstance().build("/user/settings/user/base").withString("str",UserClass.getUname()).withString("title","????????????").navigation());
        binding.ivSex.setOnClickListener(v -> {

        });
        binding.ivBirthday.setOnClickListener(v -> ARouter.getInstance().build("/user/settings/user/base").withString("str",UserClass.getBirthday()).withString("title","????????????").navigation());
        binding.ivSignature.setOnClickListener(v -> ARouter.getInstance().build("/user/settings/user/base").withString("str",UserClass.getSignature()).withString("title","??????????????????").navigation());
        binding.ivIcon.setOnClickListener(v -> {
            show2(binding.getRoot());
            TextView textView = viewDialog.findViewById(R.id.tv_camera);
            TextView textView2 = viewDialog.findViewById(R.id.tv_photo);
            TextView textView3 = viewDialog.findViewById(R.id.tv_cancel);
            textView3.setOnClickListener(v1 -> {
                bottomDialog.cancel();
            });
            textView.setOnClickListener(v1 -> {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                // ???????????????????????????????????????????????????
                if (hasSdcard()) {
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                    // ??????????????????uri
                    Uri uri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
                // ??????????????????????????????Activity???????????????PHOTO_REQUEST_CAREMA
                startActivityForResult(intent, new UploadAvatarUtil().PHOTO_REQUEST_CAREMA);
            });
            textView2.setOnClickListener(v1 -> {
                onPauseByBooler_1=true;
                // ???????????????????????????????????????
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                // ??????????????????????????????Activity???????????????PHOTO_REQUEST_GALLERY
                startActivityForResult(intent1, new UploadAvatarUtil().PHOTO_REQUEST_GALLERY);
            });
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        onPauseByBooler=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onPauseByBooler){
            getWeather("http://"+localhost+":8080/NewsMaven/changeUserInformation?uid="+UserClass.getUid()+"&uname="+UserClass.getUname()+"&sex="+UserClass.getSex()+"&birthday="+UserClass.getBirthday()+"&signature="+UserClass.getSignature());
            onPauseByBooler=false;
        }
        if(onPauseByBooler_1){
            bottomDialog.cancel();
            onPauseByBooler_1 =false;
        }
    }
    private void getWeather(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            binding.ivName.setRightDesc(UserClass.getUname());
                            binding.ivSex.setRightDesc(UserClass.getSex());
                            binding.ivBirthday.setRightDesc(UserClass.getBirthday());
                            binding.ivSignature.setRightDesc(UserClass.getSignature());
                            binding.ivUid.setRightDesc(String.valueOf(UserClass.getUid()));
                            //ToastUtil.show(UserActivity.this,"????????????");
                        }
                    }
                });
    }
    private void show2(View view) {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_upload_avatar, null);
        bottomDialog.setContentView(viewDialog);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewDialog.getLayoutParams();
        params.width = view.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        viewDialog.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }
    /*
     * ??????sdcard???????????????
     */
    private boolean hasSdcard() {
        //?????????????????????????????????????????????media_mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /*
     * ????????????
     */
    private void crop(Uri uri) {
        // ??????????????????
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // ?????????????????????1???1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // ????????????????????????????????????
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// ????????????
        intent.putExtra("noFaceDetection", true);// ??????????????????
        intent.putExtra("return-data", true);
        // ??????????????????????????????Activity???????????????PHOTO_REQUEST_CUT
        startActivityForResult(intent, new UploadAvatarUtil().PHOTO_REQUEST_CUT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == new UploadAvatarUtil().PHOTO_REQUEST_GALLERY) {
            // ????????????????????????
            if (data != null) {
                // ????????????????????????
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == new UploadAvatarUtil().PHOTO_REQUEST_CAREMA) {
            // ????????????????????????
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == new UploadAvatarUtil().PHOTO_REQUEST_CUT) {
            // ??????????????????????????????
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                /**
                 * ????????????
                 */
                binding.ivIcon.setImageBitmap(bitmap);
                //?????????SharedPreferences
                saveBitmapToSharedPreferences(bitmap);
            }
            try {
                // ?????????????????????
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //???????????????SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //?????????:???Bitmap??????????????????????????????ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //?????????:??????Base64??????????????????????????????????????????????????????String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //?????????:???String?????????SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();

        //????????????
        new UploadAvatarUtil().setImgByStr(imageString,"http://"+localhost+":8080/NewsMaven/DownloadUserAvatar","newAvatar");
    }
    //???SharedPreferences????????????
    private void getBitmapFromSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("testSP", Context.MODE_PRIVATE);
        //?????????:????????????????????????Bitmap
        String imageString=sharedPreferences.getString("image", "");
        //?????????:??????Base64?????????????????????ByteArrayInputStream
        byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
        if(byteArray.length==0){
            iv_img.setImageResource(R.mipmap.ic_launcher);
        }else{
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);

            //?????????:??????ByteArrayInputStream??????Bitmap
            Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
            binding.ivIcon.setImageBitmap(bitmap);
        }
    }

}
