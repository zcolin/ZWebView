# ZWebView
## 封装常用功能
1. 支持全屏播放视频
1. 支持打开网页时顶部带有导航条
1. 支持文件选择
1. 支持自动缩放网页
1. 支持和网页进行js交互
1. 支持H5定位
1. 支持加载失败view

## Gradle
app的build.gradle中添加
```
dependencies {
    implementation 'com.github.zcolin:ZWebView:latest.release'
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
class structure=![](class_uml.png)

## USAGE

ZWebView：
```
webView = (ZWebView) findViewById(R.id.webView);
webView.setSupportVideoFullScreen(this)  //支持全屏播放视频
        .setSupportHorizontalProgressBar()//支持打开网页时顶部带有进度条
        .setSupportCircleProgressBar()    //支持圆形进度条
        .setSupportChooeFile(activity)   //支持文件选择
        .setSupportAutoZoom()            //支持自动缩放网页
        .setSupportH5Location()          //支持H5定位
        .setSupportErrorView()           //支持加载失败view
        .setSupportJsBridge();           //支持和网页进行js交互（网页端需要的js见app目录下assets/bridgewebview_html_demo）
```
