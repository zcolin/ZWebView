# ZWebView
## 封装常用功能
1. 支持全屏播放视频
1. 支持打开网页时顶部带有导航条
1. 支持文件选择
1. 支持自动缩放网页
1. 支持和网页进行js交互


## Gradle
app的build.gradle中添加
```
dependencies {
    compile 'com.github.zcolin:ZWebView:latest.release'
}
```
工程的build.gradle中添加
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

## USAGE

ZWebView：
```
webView = (ZWebView) findViewById(R.id.webView);
webView.setSupportVideoFullScreen(this)  //支持全屏播放视频
        .setSupportProgressBar()         //支持打开网页时顶部带有导航条
        .setSupportChooeFile(activity)   //支持文件选择
        .setSupportAutoZoom()            //支持自动缩放网页
        .setSupportJsBridge();           //支持和网页进行js交互（网页端需要的js见app目录下assets/bridgewebview_html_demo）
```
