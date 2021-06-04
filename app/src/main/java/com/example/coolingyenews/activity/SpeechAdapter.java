package com.example.coolingyenews.activity;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.bean.SReview;
import com.example.coolingyenews.databinding.ActivitySpeechDetailBinding;
import com.example.coolingyenews.databinding.FragmentReviewItem2Binding;
import com.example.coolingyenews.databinding.FragmentReviewItemBinding;
import com.example.coolingyenews.news.activity.NewsActivity;
import com.example.coolingyenews.news.adapter.UserNewsReviewAdapter;
import com.example.coolingyenews.utils.DensityUtil;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.example.coolingyenews.utils.utilsTools;
import com.example.coolingyenews.widget.dialog.InputTextMsgDialog;
import com.example.coolingyenews.widget.help.SoftKeyBoardListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class SpeechAdapter extends BaseQuickAdapter<SReview, BaseViewHolder> {
    private List<SReview> data;
    private View viewDialog;
    private Dialog bottomDialog;
    private InputTextMsgDialog inputTextMsgDialog;
    private SoftKeyBoardListener mKeyBoardListener;
    private Context context;
    private ActivitySpeechDetailBinding binding;
    public SpeechAdapter(@Nullable List<SReview> data, Context context,ActivitySpeechDetailBinding binding) {
        super(R.layout.fragment_review_item_2, data);
        this.data = data;
        this.context = context;
        this.binding =binding;
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SReview sReview) {
        FragmentReviewItem2Binding binding =baseViewHolder.getBinding();
        if(binding!=null){
            try {
                if(!sReview.getReply_comment_id().equals("null")){
                    data.get(baseViewHolder.getPosition()).setContent("回复"+sReview.getReply_comment_user_id()+":"+sReview.getContent());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            binding.setSReview(data.get(baseViewHolder.getPosition()));
            binding.executePendingBindings();
            baseViewHolder.itemView.setOnClickListener(v -> {
                ToastUtil.show(getContext(),sReview.getReply_comment_id());
                initSendReview(sReview,baseViewHolder);
            });
            binding.ivMore.setOnClickListener(v -> {
                show2(baseViewHolder.itemView);
                TextView textView = viewDialog.findViewById(R.id.tv_del_something);
                TextView textView2 = viewDialog.findViewById(R.id.tv_cancel);
                textView2.setOnClickListener(v1 -> {
                    bottomDialog.cancel();
                });
                if(sReview.getUid()== UserClass.getUid()){
                    textView.setText("删除评论");
                    textView.setOnClickListener(v1 -> {
                        delReviewByUser(new HttpURL().HTTP_SREVIEW_ADD_OR_DEL_URL("delsReviewByUser", UserClass.getUid(), String.valueOf(sReview.getSid())),baseViewHolder.getPosition());
                    });
                }else {
                    textView.setVisibility(View.GONE);
                }
            });
        }
    }
    private void initSendReview(SReview review, BaseViewHolder helper) {
        //dismissInputDialog();
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(getContext(), R.style.dialog);
            inputTextMsgDialog.setHint("回复"+review.getUname());
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    if (UserClass.getUid() == 0) {
                        ToastUtil.show(getContext(), "登录，发表你的评论");
                        return;
                    }
                    sendReviewWithHttpURLConnection("http://"+localhost+":8080/NewsMaven/setSpeech?uid="+UserClass.getUid()+"&parent_comment_id="+review.getParent_comment_id()+"&parent_comment_user_id="+review.getUid()+"&reply_comment_id="+review.getSid()+"&reply_comment_user_id="+review.getUname()+"&content="+msg+"&times="+ utilsTools.getTime(),msg,review);
                }

                @Override
                public void dismiss() {

                }
            });
        }
        inputTextMsgDialog.show();
        mKeyBoardListener = new SoftKeyBoardListener((AppCompatActivity)context, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
    }
    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }
    private void show2(View view) {
        bottomDialog = new Dialog(getContext(), R.style.BottomDialog);
        viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_content_circle, null);
        bottomDialog.setContentView(viewDialog);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewDialog.getLayoutParams();
        params.width = view.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getContext(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getContext(), 8f);
        viewDialog.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }
    public void delReviewByUser(String httpUrl,int i){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            notifyItemRemoved(i);
                            data.remove(i);
                            bottomDialog.cancel();
                        }
                    }
                });
    }
    private void sendReviewWithHttpURLConnection(String httpUrl, String msg,SReview review1) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            SReview review = new SReview();
                            review.setUid(UserClass.getUid());
                            review.setUname(UserClass.getUname());
                            //review.setContent("回复11"+review1.getUname()+":"+msg);
                            review.setContent(msg);
                            review.setTimes(utilsTools.getTime());
                            review.setIcon(UserClass.getIcon());
                            review.setUser_value("0");
                            review.setLikes_count("0");
                            review.setReply_comment_id(String.valueOf(review.getSid()));
                            review.setParent_comment_user_id(review1.getUname());
                            //review.setSpeech_count("0");
                            review.setLikes_img(review.getUser_value().equals("1") ? R.drawable.icon_like_fill : R.drawable.icon_like);
                            data.add(0, review);
                            //activityNewsBinding.tvReviewCount.setText(String.valueOf(list.size()));
                            if (data.size() != 1) {
                                notifyItemInserted(0);
                                binding.speechItemList.getLayoutManager().scrollToPosition(0);
                            } else {
//                                SpeechAdapter S =speechDetailAdapter;
//                                S = new SpeechAdapter(list, context);
//                                binding.speechItemList.setAdapter(speechDetailAdapter);
                            }
                        }
                    }
                });
    }
}
