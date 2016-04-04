package com.example.frankjunior.taidoido.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.frankjunior.taidoido.R;
import com.example.frankjunior.taidoido.model.Post;
import com.example.frankjunior.taidoido.util.Util;

/**
 * Created by frankjunior on 07/03/16.
 */
public class PostDetailFragment extends Fragment implements SlidingPaneLayout.PanelSlideListener {

    private static final String ON_RESUME = "onResume";
    private static final String ON_PAUSE = "onPause";
    private static final String ENCONDING = "UTF-8";
    private static final String MIME_TYPE = "text/html";
    private static final String BASE_URL = "file:///android_asset/";
    private static final String HISTORY_URL = null;
    private Post mPost;
    private WebView mWebView;

    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.mPost = post;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_post_detail, null);

        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setSaveEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        String data = Util.formatHtml(getActivity(), mPost.getContent(), mPost.getTitle());
        mWebView.loadDataWithBaseURL(BASE_URL, data, MIME_TYPE, ENCONDING, HISTORY_URL);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            WebView.class.getMethod(ON_RESUME).invoke(mWebView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            WebView.class.getMethod(ON_PAUSE).invoke(mWebView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {
        if (mWebView != null)
            mWebView.requestLayout();
    }

    @Override
    public void onPanelClosed(View panel) {
        if (mWebView != null)
            mWebView.requestLayout();
    }
}
