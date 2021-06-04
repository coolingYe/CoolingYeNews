package com.example.coolingyenews.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.bean.SpeechNews;
import com.example.coolingyenews.collect.TextFragment;
import com.example.coolingyenews.databinding.FragmentNewsItemListBinding;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class MessageNewsFragment extends Fragment {
    private List<SpeechNews> list = new ArrayList<>();
    private MessageNewsAdapter  messageNewsAdapter;
    private static final String KEY_USER_ID = "param1";
    private String mParam1;
    private FragmentNewsItemListBinding binding;
    public static MessageNewsFragment newInstance(String param1) {
        MessageNewsFragment messageNewsFragment = new MessageNewsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, param1);
        messageNewsFragment.setArguments(args);
        return messageNewsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        if(getArguments()!=null){
            mParam1=getArguments().getString(KEY_USER_ID);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsItemListBinding.inflate(inflater);
        getNewsByHistory("http://" + localhost + ":8080/NewsMaven/getReviewNewsByUser?uid=" + UserClass.getUid());
        return binding.getRoot();
    }
    private void getNewsByHistory(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            try {
                                JSONArray array = new JSONArray(s);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    SpeechNews speechNews = GsonUtils.fromLocalJson(obj.toString(), SpeechNews.class);
                                    speechNews.setuImg(new ReplaceStr().newStr(speechNews.getuImg(), localhost));
                                    list.add(speechNews);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            messageNewsAdapter = new MessageNewsAdapter(list);
                            binding.newsItemList.setAdapter(messageNewsAdapter);
                        }
                    }
                });
    }
}
