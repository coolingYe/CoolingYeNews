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
    /* 头像名称 */
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
        binding.ivName.setOnClickListener(v -> ARouter.getInstance().build("/user/settings/user/base").withString("str",UserClass.getUname()).withString("title","修改昵称").navigation());
        binding.ivSex.setOnClickListener(v -> {

        });
        binding.ivBirthday.setOnClickListener(v -> ARouter.getInstance().build("/user/settings/user/base").withString("str",UserClass.getBirthday()).withString("title","修改生日").navigation());
        binding.ivSignature.setOnClickListener(v -> ARouter.getInstance().build("/user/settings/user/base").withString("str",UserClass.getSignature()).withString("title","修改个性签名").navigation());
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
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                    // 从文件中创建uri
                    Uri uri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                startActivityForResult(intent, new UploadAvatarUtil().PHOTO_REQUEST_CAREMA);
            });
            textView2.setOnClickListener(v1 -> {
                onPauseByBooler_1=true;
                // 激活系统图库，选择一张图片
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
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
                            //ToastUtil.show(UserActivity.this,"修改成功");
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
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        //判断ＳＤ卡手否是安装好的　　　media_mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, new UploadAvatarUtil().PHOTO_REQUEST_CUT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == new UploadAvatarUtil().PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == new UploadAvatarUtil().PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == new UploadAvatarUtil().PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                /**
                 * 获得图片
                 */
                binding.ivIcon.setImageBitmap(bitmap);
                //保存到SharedPreferences
                saveBitmapToSharedPreferences(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //保存图片到SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();

        //上传头像
        new UploadAvatarUtil().setImgByStr(imageString,"http://"+localhost+":8080/NewsMaven/DownloadUserAvatar","newAvatar");
    }
    //从SharedPreferences获取图片
    private void getBitmapFromSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("testSP", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString=sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray= Base64.decode(imageString, Base64.DEFAULT);
        if(byteArray.length==0){
            iv_img.setImageResource(R.mipmap.ic_launcher);
        }else{
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);

            //第三步:利用ByteArrayInputStream生成Bitmap
            Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
            binding.ivIcon.setImageBitmap(bitmap);
        }
    }

}
