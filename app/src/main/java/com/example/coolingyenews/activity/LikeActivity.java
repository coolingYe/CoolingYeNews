package com.example.coolingyenews.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.databinding.FragmentReviewItemListBinding;
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
 * 被点赞的评论记录
 */
@Route(path = "/News/message/like")
public class LikeActivity extends AppCompatActivity {
    private LikeAdapter likeAdapter;
    private FragmentReviewItemListBinding binding;
    private List<Review> list=new ArrayList<>();
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
        getLike("http://"+localhost+":8080/NewsMaven/getReviewByLikes?uid="+UserClass.getUid());
    }

    private void initFragment() {

    }
    private void getLike(String httpUrl) {
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
                                    Review review = new Review();
                                    review.setReviewId(obj.optInt("rid"));
                                    review.setReviewUId(obj.optInt("uid"));
                                    review.setUserName(obj.optString("uname"));
                                    review.setReviewContent(obj.optString("text"));
                                    review.setReviewTime(obj.optString("time"));
                                    review.setLikes_count(obj.optString("likes_count"));
                                    review.setUser_value(obj.getString("user_value"));
                                    review.setUserIconSrc(new ReplaceStr().newStr(obj.optString("icon"), localhost));
                                    review.setLikes_img(review.getUser_value().equals("1") ? R.drawable.icon_like_fill : R.drawable.icon_like);
                                    review.setNid(obj.getString("nid"));
                                    review.setVid(obj.getString("vid"));
                                    review.setId(!(review.getNid()=="")?obj.getString("nid"):obj.getString("vid"));
                                    review.setSpeech_count(obj.optString("speech_count"));
                                    list.add(review);
                                }
                                likeAdapter =new LikeAdapter(list);
                                binding.reviewItemList.setAdapter(likeAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
}