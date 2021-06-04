package com.example.coolingyenews.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.coolingyenews.activity.AboutActivity;
import com.example.coolingyenews.R;
import com.example.coolingyenews.activity.SettingsActivity;
import com.example.coolingyenews.databinding.FragmentUserBinding;
import com.example.coolingyenews.collect.CollectActivity;
import com.example.coolingyenews.utils.UserClass;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context ctxWithTheme = new ContextThemeWrapper(getActivity().getApplicationContext(), R.style.TranslucentTheme);
        LayoutInflater localLayoutInflater = inflater.cloneInContext(ctxWithTheme);
        binding = FragmentUserBinding.inflate(localLayoutInflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initClick();
    }

    private void initView()
    {
//        Glide.with(getContext())
//                .asBitmap()
//                .load(UserClass.getIcon())
//                .transform(new BlurTransformation(50,1), new CenterCrop())
//                .into(binding.ivAvatarBig);
        Glide.with(getContext())
                .load(UserClass.getIcon())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(binding.ivAvatar);
        if(UserClass.getUname()!=null){
            binding.tvTip.setText("");
            binding.tvUserName.setText(UserClass.getUname());
            binding.tvUserId.setText("ID:"+UserClass.getUid());
        }else {
            binding.tvTip.setOnClickListener(v -> {
                ARouter.getInstance().build("/user/login").navigation();
            });
        }
    }
    private void recyclerViewToActivity(int position){
        switch (position){
            case 1:
                startActivity(new Intent(getContext(), CollectActivity.class));
                break;
            case 2:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            case 3:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
        }
    }
//    private View getHeaderView() {
//        return LayoutInflater.from(getContext()).inflate(R.layout.user_item_header_view,recyclerView,false);
//    }
//    private View getFooterView() {
//        return LayoutInflater.from(getContext()).inflate(R.layout.user_item_footer_view,recyclerView,false);
//    }
    private void initClick(){
        binding.IVAboutAuthor.setOnClickListener(v -> {
            ARouter.getInstance().build("/user/about").navigation();
        });
        binding.IVTools.setOnClickListener(v -> {
            ARouter.getInstance().build("/user/tools").navigation();
        });
        binding.IVMyCollect.setOnClickListener(v -> {
            ARouter.getInstance().build("/user/collect").navigation();
        });
        binding.IVWeather.setOnClickListener(v -> {
            ARouter.getInstance().build("/user/weather").navigation();
        });
        binding.IVMyHistory.setOnClickListener(v -> {
            ARouter.getInstance().build("/user/history").navigation();
        });
        binding.ivAvatarBig.setOnClickListener(v -> {
            if(UserClass.getUname()!=null){
                ARouter.getInstance().build("/user/settings/user").navigation();
            }else return;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.tvUserName.setText(UserClass.getUname());
    }
}