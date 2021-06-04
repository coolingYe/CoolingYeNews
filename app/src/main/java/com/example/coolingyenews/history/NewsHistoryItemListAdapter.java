package com.example.coolingyenews.history;

import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.databinding.FragmentNewsCollectItemBinding;
import com.example.coolingyenews.utils.DensityUtil;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NewsHistoryItemListAdapter extends BaseQuickAdapter<News,BaseViewHolder>{
    private View viewDialog;
    private Dialog bottomDialog;
    private List<News> data;
    public NewsHistoryItemListAdapter(@Nullable List<News> data) {
        super(R.layout.fragment_news_collect_item, data);
        this.data = data;
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, News news) {
        FragmentNewsCollectItemBinding fragmentNewsCollectItemBinding = baseViewHolder.getBinding();
        if(fragmentNewsCollectItemBinding!=null){
            baseViewHolder.itemView.setOnClickListener(v -> {
                ARouter.getInstance()
                        .build("/News/News")
                        .withSerializable("news",news)
                        .navigation();
            });
            fragmentNewsCollectItemBinding.ivMore.setOnClickListener(v -> {
                show2(fragmentNewsCollectItemBinding.getRoot());
                TextView textView = viewDialog.findViewById(R.id.tv_del_something);
                textView.setText("删除记录");
                TextView textView2 = viewDialog.findViewById(R.id.tv_cancel);
                textView.setOnClickListener(v1 -> {
                    delNewsByHistory(new HttpURL().HTTP_HISTORY_ADD_OR_DEL_URL("delNewsByHistory", UserClass.getUid(), news.getNid()),baseViewHolder.getPosition());
                });
                textView2.setOnClickListener(v1 -> {
                    bottomDialog.cancel();
                });
            });
            fragmentNewsCollectItemBinding.setViewModel(data.get(baseViewHolder.getPosition()));
            fragmentNewsCollectItemBinding.executePendingBindings();
        }
    }
    private void show2(View view) {
        bottomDialog = new Dialog(view.getContext(), R.style.BottomDialog);
        viewDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_content_circle, null);
        bottomDialog.setContentView(viewDialog);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewDialog.getLayoutParams();
        params.width = view.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(view.getContext(), 16f);
        params.bottomMargin = DensityUtil.dp2px(view.getContext(), 8f);
        viewDialog.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }
    public void delNewsByHistory(String httpUrl,int i){
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
}
