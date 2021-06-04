package com.example.coolingyenews.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.SReview;
import com.example.coolingyenews.databinding.FragmentReviewItemListBinding;
import com.example.coolingyenews.utils.GsonUtils;
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

/**
 * 被回复的评论记录
 */
@Route(path = "/News/message/reply")
public class ReplyActivity extends AppCompatActivity {
    private List<SReview> list =new ArrayList<>();
    private ReplyAdapter replyAdapter;
    private FragmentReviewItemListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_review_item_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        getReply("http://"+localhost+":8080/NewsMaven/getSpeechByparent_comment_user_id?parent_comment_user_id="+UserClass.getUid());
    }

    private void initFragment() {

    }
    private void getReply(String httpUrl) {
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
                                    SReview sReview= GsonUtils.fromLocalJson(obj.toString(), SReview.class);
                                    sReview.setIcon(new ReplaceStr().newStr(sReview.getIcon(), localhost));
                                    list.add(sReview);
                                }
                                replyAdapter =new ReplyAdapter(list);
                                binding.reviewItemList.setAdapter(replyAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
}