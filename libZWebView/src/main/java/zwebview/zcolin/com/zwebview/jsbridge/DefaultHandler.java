/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package zwebview.zcolin.com.zwebview.jsbridge;


public class DefaultHandler implements BridgeHandler {

    String TAG = "DefaultHandler";

    @Override
    public void handler(String data, CallBackFunction function) {
        if (function != null) {
            function.onCallBack("DefaultHandler response data");
        }
    }

}
