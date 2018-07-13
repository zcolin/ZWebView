/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.zwebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.zcolin.zwebview.jsbridge.BridgeWebView;

import zwebview.zcolin.com.zwebview.R;


/**
 * 封装的Webview的控件
 */
public class ZWebView extends BridgeWebView {

    private ZWebViewClientWrapper   webViewClientWrapper;
    private ZWebChromeClientWrapper webChromeClientWrapper;
    private ProgressBar             horizontalProBar;            //横向加载進度条
    private View                    circleProBar;            //圆形加载進度条
    private boolean                 isSupportJsBridge;
    private boolean                 isSupportH5Location;
    private View                    errorView;

    public ZWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    //网页属性设置
    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        setWebChromeClient(new WebChromeClient());

        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);//支持JS
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口 
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭webview中缓存 
        webSetting.setDomStorageEnabled(true);  // 开启 DOM storage API 功能
        webSetting.setDatabaseEnabled(true);    //开启 database storage API 功能
        webSetting.setGeolocationEnabled(true);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSetting.setAllowUniversalAccessFromFileURLs(true);//解决跨域问题
        }

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    public void setWebViewClient(@NonNull WebViewClient webViewClient) {
        this.webViewClientWrapper = new ZWebViewClientWrapper(webViewClient);
        webViewClientWrapper.setHorizontalProgressBar(horizontalProBar);
        webViewClientWrapper.setCircleProgressBar(circleProBar);
        if (isSupportJsBridge) {
            webViewClientWrapper.setSupportJsBridge();
        }
        super.setWebViewClient(webViewClientWrapper);
    }

    @Override
    public void setWebChromeClient(@NonNull WebChromeClient webChromeClient) {
        if (webChromeClientWrapper == null) {
            this.webChromeClientWrapper = new ZWebChromeClientWrapper(webChromeClient);
        } else if (webChromeClientWrapper instanceof ZChooseFileWebChromeClientWrapper || webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            this.webChromeClientWrapper.setWebChromeClient(webChromeClient);
        } else {
            this.webChromeClientWrapper = new ZWebChromeClientWrapper(webChromeClient);
        }

        webChromeClientWrapper.setHorizontalProgressBar(horizontalProBar);
        if (isSupportH5Location) {
            webChromeClientWrapper.setSupportH5Location();
        }
        super.setWebChromeClient(webChromeClientWrapper);
    }

    /**
     * 支持文件选择
     * <p>
     * <p>
     * 需要在Activity的onActivityResult中调用:
     * <pre>
     *  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
     *       super.onActivityResult(requestCode, resultCode, intent);
     *      webView.processResult(requestCode, resultCode, intent);
     *  }
     * </pre>
     *
     * @param pickFile 文件选择自定义处理方式
     *                 <p>
     *                 pickFile参考如下
     *                 Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
     *                 chooserIntent.setType(acceptType);
     *                 if (fragment != null) {
     *                 fragment.startActivityForResult(chooserIntent, REQUEST_CODE);
     *                 } else if (activity != null) {
     *                 activity.startActivityForResult(chooserIntent, REQUEST_CODE);
     *                 }
     */
    public ZWebView setSupportChooseFile(Activity activity, IPickFile pickFile) {
        webChromeClientWrapper = new ZChooseFileWebChromeClientWrapper(webChromeClientWrapper.getWebChromeClient(), activity, pickFile);
        setWebChromeClient(webChromeClientWrapper.getWebChromeClient());
        return this;
    }

    /**
     * 支持文件选择
     * <p>
     * <p>
     * 需要在Activity的onActivityResult中调用:
     * <pre>
     *  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
     *       super.onActivityResult(requestCode, resultCode, intent);
     *      webView.processResult(requestCode, resultCode, intent);
     *  }
     * </pre>
     */
    public ZWebView setSupportChooseFile(Activity activity) {
        return this.setSupportChooseFile(activity, null);
    }

    /**
     * 支持文件选择
     * <p>
     * <p>
     * 需要在Fragment的onActivityResult中调用:
     * <pre>
     *  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
     *      webView.processResult(requestCode, resultCode, intent);
     *  }
     * </pre>
     *
     * @param pickFile 文件选择自定义处理方式
     *                 <p>
     *                 pickFile参考如下
     *                 Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
     *                 chooserIntent.setType(acceptType);
     *                 if (fragment != null) {
     *                 fragment.startActivityForResult(chooserIntent, REQUEST_CODE);
     *                 } else if (activity != null) {
     *                 activity.startActivityForResult(chooserIntent, REQUEST_CODE);
     *                 }
     */
    public ZWebView setSupportChooseFile(Fragment fragment, IPickFile pickFile) {
        webChromeClientWrapper = new ZChooseFileWebChromeClientWrapper(webChromeClientWrapper.getWebChromeClient(), fragment, pickFile);
        setWebChromeClient(webChromeClientWrapper.getWebChromeClient());
        return this;
    }

    /**
     * 支持文件选择
     * <p>
     * <p>
     * 需要在Activity的onActivityResult中调用:
     * <pre>
     *  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
     *       super.onActivityResult(requestCode, resultCode, intent);
     *      webView.processResult(requestCode, resultCode, intent);
     *  }
     * </pre>
     */
    public ZWebView setSupportChooseFile(Fragment fragment) {
        return this.setSupportChooseFile(fragment, null);
    }

    /**
     * 支持视频全屏
     * <p>
     * <strong>必须在Activity的manifest文件中指定 android:configChanges="keyboardHidden|orientation|screenSize"</strong>
     * <p>
     * <pre>
     * <strong>在Activity的OnKeyDown中如下：</strong>
     *  public boolean onKeyDown(int keyCode, KeyEvent event) {
     *      if (keyCode == KeyEvent.KEYCODE_BACK) {
     *          if (webView.hideCustomView()) {
     *              return true;
     *          } else if (webView.canGoBack()) {
     *               webView.goBack();
     *              return true;
     *           }
     *       }
     *      return super.onKeyDown(keyCode, event);
     *  }
     * </pre>
     */
    public ZWebView setSupportVideoFullScreen(Activity activity) {
        ViewGroup group = (ViewGroup) this.getParent();
        FrameLayout container = new FrameLayout(getContext());
        int index = group.indexOfChild(this);

        //将原来的布局之间添加一层，用来盛放webView和视频全屏控件
        group.removeView(this);
        group.addView(container, index, this.getLayoutParams());
        container.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //添加视频ViewContainer
        FrameLayout flCustomContainer = new FrameLayout(getContext());
        flCustomContainer.setVisibility(View.INVISIBLE);
        container.addView(flCustomContainer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View videoProgressView = LayoutInflater.from(activity).inflate(R.layout.zwebview_view_webview_video_progress, null);
        webChromeClientWrapper = new ZVideoFullScreenWebChromeClient(webChromeClientWrapper.getWebChromeClient(), activity, this, flCustomContainer, 
                videoProgressView);
        setWebChromeClient(webChromeClientWrapper.getWebChromeClient());
        return this;
    }

    public void setCustomViewShowStateListener(ZVideoFullScreenWebChromeClient.CustomViewShowStateListener customViewShowStateListener) {
        if (webChromeClientWrapper != null && webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            ((ZVideoFullScreenWebChromeClient) webChromeClientWrapper).setCustomViewShowStateListener(customViewShowStateListener);
        }
    }

    /**
     * 支持显示自定义加载View
     */
    public ZWebView setSupportProgressBar(@LayoutRes int layoutId) {
        setSupportProgressBar(LayoutInflater.from(getContext()).inflate(layoutId, null));
        return this;
    }

    /**
     * 支持显示自定义加载View
     */
    public ZWebView setSupportProgressBar(View view) {
        ViewGroup group = (ViewGroup) this.getParent();
        RelativeLayout container = new RelativeLayout(getContext());
        int index = group.indexOfChild(this);
        group.removeView(this);
        group.addView(container, index, this.getLayoutParams());
        container.addView(this, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        circleProBar = view;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(circleProBar, params);
        webViewClientWrapper.setCircleProgressBar(circleProBar);
        return this;
    }


    /**
     * 支持圆形显示进度条
     */
    public ZWebView setSupportCircleProgressBar() {
        setSupportProgressBar(R.layout.zwebview_view_webview_circle_progressbar);
        return this;
    }

    /**
     * 支持显示进度条
     */
    public ZWebView setSupportHorizontalProgressBar() {
        ViewGroup group = (ViewGroup) this.getParent();
        FrameLayout container = new FrameLayout(getContext());
        int index = group.indexOfChild(this);
        group.removeView(this);
        group.addView(container, index, this.getLayoutParams());
        container.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        horizontalProBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.zwebview_view_webview_horizontal_progressbar, null);
        container.addView(horizontalProBar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dip2px(getContext(), 4)));
        webChromeClientWrapper.setHorizontalProgressBar(horizontalProBar);
        webViewClientWrapper.setHorizontalProgressBar(horizontalProBar);
        return this;
    }

    /**
     * 支持显示加载失败view
     */
    public ZWebView setSupportErrorView() {
        View errorView = LayoutInflater.from(getContext()).inflate(R.layout.zwebview_view_webview_error, null);
        errorView.findViewById(R.id.ll_container).setOnClickListener(v -> {
            reload();
        });
        setSupportErrorView(errorView);
        return this;
    }


    /**
     * 支持显示加载失败view
     */
    public ZWebView setSupportErrorView(View view) {
        ViewGroup group = (ViewGroup) this.getParent();
        RelativeLayout container = new RelativeLayout(getContext());
        int index = group.indexOfChild(this);
        group.removeView(this);
        group.addView(container, index, this.getLayoutParams());
        container.addView(this, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorView = view;
        errorView.setVisibility(GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(errorView, params);
        webViewClientWrapper.setErrorView(errorView);
        return this;
    }

    /**
     * 隐藏errorview
     */
    public void hideErrorView() {
        if (errorView != null) {
            errorView.setVisibility(GONE);
        }
    }


    @Override
    public void reload() {
        webViewClientWrapper.reset();
        hideErrorView();
        super.reload();
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 设置是否支持JsBridge
     */
    public void setSupportJsBridge() {
        isSupportJsBridge = true;
        webViewClientWrapper.setSupportJsBridge();
    }

    /**
     * 设置是否支持自动缩放
     */
    public void setSupportAutoZoom() {
        WebSettings webSettings = getSettings();
        webSettings.setUseWideViewPort(true);//关键点  
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
    }

    /**
     * 设置是否支持H5定位
     * <p>
     * 需要声明权限
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     */
    public void setSupportH5Location() {
        isSupportH5Location = true;
        webChromeClientWrapper.setSupportH5Location();
        WebSettings webSettings = getSettings();
        webSettings.setDomStorageEnabled(true);
    }

    /**
     * 支持网页下载
     */
    public void setSupportDownLoad() {
        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        });
    }

    public WebViewClient getWebViewClient() {
        return webViewClientWrapper.getWebViewClient();
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClientWrapper.getWebChromeClient();
    }

    /**
     * 支持文件选择的时候需要在onActivity中调用此函数
     */
    public boolean processResult(int requestCode, int resultCode, Intent intent) {
        if (webChromeClientWrapper instanceof ZChooseFileWebChromeClientWrapper) {
            return ((ZChooseFileWebChromeClientWrapper) webChromeClientWrapper).processResult(requestCode, resultCode, intent);
        }
        return false;
    }

    /**
     * 支持文件选择的时候需要在onActivity中调用此函数
     */
    public boolean processResult(Uri uri) {
        if (webChromeClientWrapper instanceof ZChooseFileWebChromeClientWrapper) {
            return ((ZChooseFileWebChromeClientWrapper) webChromeClientWrapper).processResult(uri);
        }
        return false;
    }

    /**
     * 支持文件选择的时候需要在onActivity中调用此函数
     */
    public boolean processResult(Uri[] uris) {
        if (webChromeClientWrapper instanceof ZChooseFileWebChromeClientWrapper) {
            return ((ZChooseFileWebChromeClientWrapper) webChromeClientWrapper).processResult(uris);
        }
        return false;
    }


    /**
     * 如果在视频全屏播放状态，取消全屏播放
     */
    public boolean hideCustomView() {
        if (webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            ZVideoFullScreenWebChromeClient client = ((ZVideoFullScreenWebChromeClient) webChromeClientWrapper);
            if (client.isCustomViewShow()) {
                client.onHideCustomView();
                return true;
            }
        }
        return false;
    }


    /**
     * 是否在全屏播放页面
     */
    public boolean isVideoFullScreen() {
        if (webChromeClientWrapper instanceof ZVideoFullScreenWebChromeClient) {
            ZVideoFullScreenWebChromeClient client = ((ZVideoFullScreenWebChromeClient) webChromeClientWrapper);
            return client.isCustomViewShow();
        }
        return false;
    }

    /**
     * 是否已经注入了js
     */
    public boolean isInjectJSBridge() {
        return webViewClientWrapper.isInjectJSBridge();
    }

    @Override
    public void destroy() {
        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
        // destory()
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }

        stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        getSettings().setJavaScriptEnabled(false);
        clearHistory();
        clearView();
        removeAllViews();

        try {
            super.destroy();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
