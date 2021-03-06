package com.example.coolingyenews.video;

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

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.databinding.FragmentReviewItemBinding;
import com.example.coolingyenews.databinding.FragmentVideoReviewItemBinding;
import com.example.coolingyenews.utils.DensityUtil;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.NewsClass;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.example.coolingyenews.utils.utilsTools;
import com.example.coolingyenews.widget.dialog.InputTextMsgDialog;
import com.example.coolingyenews.widget.help.SoftKeyBoardListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class VideoReviewAdapter extends BaseQuickAdapter<Review, BaseViewHolder> {

    private boolean reviewByLike =false;
    private boolean reviewByHate =false;
    private FragmentVideoReviewItemBinding fragmentVideoReviewItemBinding;
    private View viewDialog;
    private Dialog bottomDialog;
    private List<Review> data;
    private InputTextMsgDialog inputTextMsgDialog;
    private SoftKeyBoardListener mKeyBoardListener;
    private Context context;

    public VideoReviewAdapter(List<Review> data, Context context) {
        super(R.layout.fragment_video_review_item, data);
        this.data = data;
        this.context = context;
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }
    @Override
    protected void convert(BaseViewHolder helper, Review item) {
         fragmentVideoReviewItemBinding = helper.getBinding();
        if (fragmentVideoReviewItemBinding != null) {
            fragmentVideoReviewItemBinding.ivLike.setOnClickListener(v -> {
                if ((item.getUser_value().equals("0") || item.getUser_value() == "0")) {
                    addNewsSaveImage(new HttpURL().HTTP_REVIEW_ADD_OR_DEL_URL("insertReviewByLikesNumber", UserClass.getUid(), String.valueOf(item.getReviewId())), item);

                } else {
                    delNewsSaveImage(new HttpURL().HTTP_REVIEW_ADD_OR_DEL_URL("delReviewByLikesNumber", UserClass.getUid(), String.valueOf(item.getReviewId())), item);
                }

            });
            fragmentVideoReviewItemBinding.ivMore.setOnClickListener(v -> {
                show2(helper.itemView);
                TextView textView = viewDialog.findViewById(R.id.tv_del_something);
                TextView textView2 = viewDialog.findViewById(R.id.tv_cancel);
                textView2.setOnClickListener(v1 -> {
                    bottomDialog.cancel();
                });
                if(item.getReviewUId()==UserClass.getUid()){
                    textView.setText("????????????");
                    textView.setOnClickListener(v1 -> {
                        delReviewByUser(new HttpURL().HTTP_REVIEW_ADD_OR_DEL_URL("delReviewByUser", UserClass.getUid(), String.valueOf(item.getReviewId())),helper.getPosition());
                    });
                }else {
                    textView.setVisibility(View.GONE);
                }
            });
            try {
                if(Integer.parseInt(item.getSpeech_count())!=0){
                    helper.itemView.findViewById(R.id.ll_speech).setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            helper.itemView.setOnClickListener(v -> {
                //ToastUtil.show(getContext(),item.getReviewId()+"");
                initSendReview(item,helper);
            });
            fragmentVideoReviewItemBinding.llSpeech.setOnClickListener(v -> {
//                data.get(helper.getPosition()).setPosition(helper.getPosition());
                NewsClass.setPosition(helper.getPosition());
                ARouter.getInstance().build("/Main/speech").withString("rId",String.valueOf(data.get(helper.getPosition()).getReviewId())).withSerializable("review",data.get(helper.getPosition())).navigation();
            });
            fragmentVideoReviewItemBinding.setReview(data.get(helper.getPosition()));
            fragmentVideoReviewItemBinding.executePendingBindings();
        }
    }
    private void initSendReview(Review review,BaseViewHolder helper) {
            //dismissInputDialog();
            if (inputTextMsgDialog == null) {
                inputTextMsgDialog = new InputTextMsgDialog(getContext(), R.style.dialog);
                inputTextMsgDialog.setHint("??????"+review.getUserName());
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        if (UserClass.getUid() == 0) {
                            ToastUtil.show(getContext(), "???????????????????????????");
                            return;
                        }
                        sendReviewWithHttpURLConnection("http://"+localhost+":8080/NewsMaven/setSpeech?uid="+UserClass.getUid()+"&parent_comment_id="+review.getReviewId()+"&parent_comment_user_id="+review.getReviewUId()+"&reply_comment_user_id="+review.getUserName()+"&content="+msg+"&times="+utilsTools.getTime(),review,helper);
                        Log.d("asdasdasdddddd","http://"+localhost+":8080/NewsMaven/setSpeech?uid="+UserClass.getUid()+"&parent_comment_id="+review.getReviewId()+"&parent_comment_user_id="+review.getReviewUId()+"&reply_comment_user_id="+review.getUserName()+"&content="+msg+"&times="+utilsTools.getTime());
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
    private void addNewsSaveImage(String httpUrl,Review item) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            reviewByLike=true;
                            item.setLikes_img(R.drawable.icon_like_fill);
                            item.setLikes_count(String.valueOf(Integer.parseInt(item.getLikes_count())+1));
                            item.setUser_value(String.valueOf(Integer.parseInt(item.getUser_value()+1)));
//                            item.setLikes_count(reviewByLike ? String.valueOf(Integer.parseInt(item.getLikes_count()) + 1) : (Integer.parseInt(item.getLikes_count()) > 0 ? String.valueOf(Integer.parseInt(item.getLikes_count()) - 1) : String.valueOf(Integer.parseInt(item.getLikes_count()))));
                            item.setUser_value("1");
                        }
                    }
                });
    }
    private void delNewsSaveImage(String httpUrl,Review item) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            item.setLikes_img(R.drawable.icon_like);
                            item.setLikes_count(String.valueOf(Integer.parseInt(item.getLikes_count())-1));
//                            item.setLikes_count(reviewByLike ? String.valueOf(Integer.parseInt(item.getLikes_count()) - 1) : (Integer.parseInt(item.getLikes_count()) > 0 ? String.valueOf(Integer.parseInt(item.getLikes_count()) + 1) : String.valueOf(Integer.parseInt(item.getLikes_count()))));
                            reviewByLike=false;
                            item.setUser_value("0");
                        }
                    }
                });
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
    private void sendReviewWithHttpURLConnection(String httpUrl,Review review,BaseViewHolder helper) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            try {
                                review.setSpeech_count(String.valueOf(Integer.parseInt(review.getSpeech_count())+1));
                                if(Integer.parseInt(review.getSpeech_count())!=0){
                                    helper.itemView.findViewById(R.id.ll_speech).setVisibility(View.VISIBLE);
                                }
                            }catch (Exception e){e.printStackTrace();}
                        }
                    }
                });
    }


}
