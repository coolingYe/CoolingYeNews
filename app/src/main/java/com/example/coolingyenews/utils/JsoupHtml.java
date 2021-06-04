package com.example.coolingyenews.utils;

import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.example.coolingyenews.widget.help.MImageGetter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/***
 * 自动解析html
 */
//J-article-content article-content
public class JsoupHtml {
    public Document JsoupNewsHtml(String url) throws IOException {
        Connection conn = Jsoup.connect(url); // 建立与url中页面的连接
        Document doc = conn.get(); // 解析页面
//        String j = null;
//        Elements divs = doc.getElementsByClass("J-contain_detail_cnt contain_detail_cnt"); // 获取页面中article_description类的div
//        for (Element p:divs) {
//            j=p.text();
//        }
        return doc;
        //return Html.fromHtml(String.valueOf(doc),new MImageGetter(textView,this));
    }
}
