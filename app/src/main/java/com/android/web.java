package com.android;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebSettingsImpl;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import okhttp3.Call;

import static android.content.ContentValues.TAG;


public class web extends AppCompatActivity {

    AgentWeb mAgentWeb;
    WebView cordWebView;

    HashMap<String, String> h;
    private String[] hideArr = new String[]{"class==mHeader w100 pr z2"};
    private static final String o = "order_id";
    private static final String order_id = "order_id";
    private static final String spName = "spName";
    public final String Data = "data";
    private static boolean urlFlag = false;
    private LinearLayout mView;

    private String mSkipurl;
    private String referer;
    private SpUtils mSpUtils;
    View dView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        mSpUtils = new SpUtils(this);
        dView = getWindow().getDecorView();
//        mUrl = getIntent().getStringExtra("aaurl");
//        mSkipurl = getIntent().getStringExtra("skipurls");
//        referer = getIntent().getStringExtra("referer");
        initaadfd();


        aa();
    }

    private void initaadfd() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(-1, 3)
                .setAgentWebWebSettings(getSettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DERECT)
                .createAgentWeb()
                .ready()
                .go(goUrl());
        cordWebView = mAgentWeb.getWebCreator().getWebView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(cordWebView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        cordWebView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        cordWebView.getSettings().setAppCacheEnabled(true);
        cordWebView.getSettings().setUseWideViewPort(true);
        cordWebView.getSettings().setLoadWithOverviewMode(true);
        cordWebView.setOnLongClickListener(sadasdas);

    }

    private String goUrl() {
//        if (urlFlag) {
            return BuildConfig.URL;
//        }
//        return "";

    }

    private IAgentWebSettings getSettings() {
        AbsAgentWebSettings instance = AgentWebSettingsImpl.getInstance();
        return instance;
    }

    public String getComeUrl() {
        return BuildConfig.URL;
    }


    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    boolean needClearHistory =true;
    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            if (needClearHistory) {
                needClearHistory = false;
                cordWebView.clearHistory();//清除历史记录
            }
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.w("web", url);
        }


        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.w("abc", url);
            return super.shouldInterceptRequest(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request.getMethod().equals("POST"))
                Log.w("web", request.getUrl() + "");
            return super.shouldInterceptRequest(view, request);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            Log.w("aaa", url);
            try {
                if (!TextUtils.isEmpty(mSkipurl)) {
                    String[] strings = mSkipurl.split("&");
                    for (String s : strings) {
                        if (url.contains(s)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            return super.shouldOverrideUrlLoading(view, request);

        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            Log.w("bbb", url);
            try {
                if (!TextUtils.isEmpty(mSkipurl)) {
                    String[] strings = mSkipurl.split("&");
                    for (String s : strings) {
                        if (url.contains(s)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };


    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Bitmap bmp;
    private View.OnLongClickListener sadasdas = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
//            MainActivityPermissionsDispatcher.downloadWithPermissionCheck(OfficalMainActivity.this,"");
            final WebView.HitTestResult hitTestResult = mAgentWeb.getWebCreator().getWebView().getHitTestResult();
            // 如果是图片类型或者是带有图片链接的类型
            if (hitTestResult.getType() == android.webkit.WebView.HitTestResult.IMAGE_TYPE ||
                    hitTestResult.getType() == android.webkit.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                // 弹出保存图片的对话框
                final AlertDialog.Builder builder = new AlertDialog.Builder(web.this);
                builder.setTitle("提示");
                builder.setMessage("保存图片到本地");
                builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        bmp = createBitmapFromView(dView);
                        if (null == bmp) {
                            Toast.makeText(web.this, "请手动截屏", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ApplyPermission2();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    // 自动dismiss
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            return false;

        }
    };


    private void ApplyPermission2() {

        AndPermission.with(web.this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        try {
                            saveImageToGallery(web.this, bmp);
                        } catch (Exception e) {
                            Toast.makeText(web.this, "保存失败,请手动截屏", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        }
                        ShowJumpOtherApp();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(web.this, data)) {

                            new AlertDialog.Builder(web.this).setTitle("申请权限").setMessage("保存图片需要给予保存权限请选择存储权限同意")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AndPermission.with(web.this)
                                                    .runtime()
                                                    .setting()
                                                    .onComeback(new Setting.Action() {
                                                        @Override
                                                        public void onAction() {
                                                            // 用户从设置回来了。
                                                            ApplyPermission2();
                                                        }
                                                    })
                                                    .start();

                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        }
                    }
                })
                .start();

    }


    public View aaa(int resId) {


        View view2 = View.inflate(web.this, R.layout.aaa, null);
        ViewGroup group = (ViewGroup) view2.findViewById(R.id.aac);
        ImageView imageView = new ImageView(web.this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(resId);
        group.addView(imageView);


        return view2;
    }

    public void ShowJumpOtherApp() {
        new AlertDialog.Builder(web.this).setTitle("扫码支付").setMessage("请打开微信或支付宝-->扫码-->点击相册-->选取二维码进行扫码支付")
                .setPositiveButton("打开微信", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int wechatcount = mSpUtils.getInt("wechatcount", 0);

                        if (wechatcount <= 2) {

                            new AlertDialog.Builder(web.this).setTitle("温馨提示").setView(aaa(R.mipmap.aaa))

                                    .setPositiveButton("我知道啦", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                                intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.setComponent(cmp);
                                                startActivity(intent);
                                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(web.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).show();

                        } else {
                            try {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setComponent(cmp);
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(web.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_SHORT).show();
                            }
                        }

                        mSpUtils.putInt("wechatcount", ++wechatcount);

                    }

                }).setNegativeButton("打开支付宝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int zhicount = mSpUtils.getInt("zhicount", 0);
                if (zhicount <= 2) {

                    new AlertDialog.Builder(web.this).setTitle("温馨提示").setView(aaa(R.mipmap.pnsn))
                            .setPositiveButton("我知道啦", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(web.this, "检查到您手机没有安装支付宝，请安装后使用该功能", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).show();


                } else {
                    try {
                        Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(web.this, "检查到您手机没有安装支付宝，请安装后使用该功能", Toast.LENGTH_SHORT).show();
                    }
                }

                mSpUtils.putInt("zhicount", ++zhicount);
            }
        }).setNeutralButton("取消", null).show();

    }


    public class SpUtils {
        private SharedPreferences.Editor mEdit;
        private SharedPreferences mSp;

        public SpUtils(Context context) {
            mSp = context.getSharedPreferences("SSQS", Context.MODE_PRIVATE);
            mEdit = mSp.edit();
        }

        public void putBoolean(String str, boolean value) {
            mEdit.putBoolean(str, value);
            mEdit.commit();
        }

        public void putInt(String key, int value) {
            mEdit.putInt(key, value);
            mEdit.commit();
        }

        public void putString(String key, String value) {
            mEdit.putString(key, value);
            mEdit.commit();
        }

        public boolean getBoolean(String key, boolean defValue) {
            return mSp.getBoolean(key, defValue);
        }

        public int getInt(String key, int defValue) {
            return mSp.getInt(key, defValue);
        }

        public String getString(String key, String Value) {
            return mSp.getString(key, Value);
        }


    }


    public static Bitmap createBitmapFromView(View view) {
        view.clearFocus();
        Bitmap bitmap = createBitmapSafely(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888, 1);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }


    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }


    public static void saveImageToGallery(Context context, Bitmap bmp) throws Exception {
        String fileName;
        File file;
        String bitName = SystemClock.currentThreadTimeMillis() + ".jpg";
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        out = new FileOutputStream(file);
        // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
        if (bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
            out.flush();
            out.close();
            // 插入图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }


    //  数据初始化
    private void aa() {
        boolean wi = isWifiProxy(this);
        boolean vp = isVpnUsed();
        if (vp || wi) {
            go(getComeUrl());
            return;
        }


        //进行网络请求

        OkHttpUtils
                .post()
                .addParams("version", "v2")
                .addParams("origin_id", BuildConfig.app_id.trim())
                .addParams("fisrt_open_time", getFirstOpenTime(web.this))
                .addParams(o, getAppid(web.this, BuildConfig.app_id.trim()))
                .url(url1)
                .build()
                .connTimeOut(3000)
                .readTimeOut(3000)
                .execute(callback);


//        510231644
        Log.d("aaa", "请求的ID   : " + getAppid(web.this, BuildConfig.app_id.trim()));


    }

    String url1 = "http://sz.llcheng888.com/switch/api2/get_url";
    String url2 = "http://sz2.html2api.com/switch/api2/get_url";

    boolean flag = true;
    Callback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            Log.d("aaa", "请求的ID   : " + getAppid(web.this, BuildConfig.app_id.trim()));

            Log.d("aaa", "json 数据为 : " + e.getMessage());
            Log.d("aaa", "call 数据为 : " + call.request().body());
            Log.d("aaa", "getAppid(this, mAppid) : " + getAppid(web.this, BuildConfig.app_id.trim()));
            //二次网络请求
            if (flag) {
                OkHttpUtils
                        .post()

                        .addParams("origin_id", BuildConfig.app_id.trim())
                        .addParams("version", "v2")
                        .addParams("fisrt_open_time", getFirstOpenTime(web.this))
                        .addParams(o, getAppid(web.this, BuildConfig.app_id.trim()))

                        .url(url2)
                        .build()
                        .connTimeOut(3000)
                        .readTimeOut(3000)
                        .execute(callback);
                flag = false;
            }
        }

        String data;
        String sk;

        @Override
        public void onResponse(String response, int id) {
            Log.d("aaa", "json 数据为 : " + response);

            if (response.contains("not found")) {
                //新ID 或者請求的ID 、
                Log.d("aaa", "错误的事情为  : 111");
                go(getComeUrl());
                return;
            } else {
                // 判断json  array   进行解密数据。
                try {
                    JSONArray json = new JSONArray(response.trim());
                    Log.d("aaa", "json 数据为 : " + response);
                    String results = Myjiemi(json);
                    Log.d("aaa", "String results: " + results);

                    //数据解析
                    JSONObject ob = new JSONObject(results);
                    String errmsg = ob.optString("errmsg");

                    if (errmsg.contains("not found this")) {
                        go(getComeUrl());
                        Log.d("aaa", "String results:1 " + results);
                        return;
                    }
                    Log.d("aaa", "String results:2 " + results);

                    boolean jump = false;

                    try {
                        jump = ob.getBoolean("jump");

                    } catch (JSONException E) {
                        Log.d("aaa", "跑进来了 这个地方 : 3" + E.getMessage());
                        go(getComeUrl());
                        return;
                    }

                    if (jump) {
                        Log.d("aaa", "String results: 3" + results);
                        try {
                            String new_id = (String) ob.opt("new_id");
                            putAppid(web.this, new_id);

                        } catch (Exception e1) {
                            Log.d("aaa", "String results: 4" + results);
                            Integer new_id = (Integer) ob.opt("new_id");
                            putAppid(web.this, new_id + "");
                        }
                        sk = (String) ob.opt("sk");
                        data = (String) ob.opt("data");
                        if (!TextUtils.isEmpty(data)) {
                            putStringData(web.this, data);
                        }
                        try {
                            ShowUpdate(web.this, ob.getJSONObject("update_data"));
                        } catch (Exception e) {
                            Log.d("aaa", "String results:5 " + results);
                            e.printStackTrace();
                            Log.w(TAG, "e:" + e);
                        }


                        String stringData = getStringData(web.this);

                        Log.d("aaa", "____________String results__________________ ");
                        Log.d("aaa", "____________stringData__________________ " + stringData + "___________stringData_______");

                        Log.d("aaa", "____________esponse.trim()__________________ " + response.trim() + "____________esponse.trim()__________________ ");
                        Log.d("aaa", "____________String results__________________ ");
                        Log.d("aaa", "____________!TextUtils.isEmpty(stringData)__________________ " + !TextUtils.isEmpty(stringData));
                        Log.d("aaa", "____________!TextUtils.isEmpty(response.trim())__________________ " + !TextUtils.isEmpty(response.trim()));
                        if (!TextUtils.isEmpty(stringData) && !TextUtils.isEmpty(response.trim())) {
                            // Log.e(TAG, "pa:  判斷返回數據為空， 和存儲的數據不為空 ，然後加載上次請求的數據。 缓存的数据链接为  " + stringData);
                            data = stringData;
                            go(DES_Decrypt(data));
                            Log.d(TAG, "data 链接为 : " + data);
                            return;
                        } else {
                            Log.d("aaa", "String results: 6" + results);
                            go(getComeUrl());
                        }
                    } else {
                        go(getComeUrl());
                    }
                } catch (Exception e) {
                    //数据解密失败。
                    Log.d("aaa", "数据解析失败，跳转原始的web界面");
                    go(getComeUrl());
                }
            }
        }
    };

    private String DES_Decrypt(String data) throws Exception {

        String key = "20171117";
        byte[] decrypted = DES_CBC_Decrypt(hexStringToByte(data.toUpperCase()), key.getBytes());
        return new String(decrypted);

    }

    private void go(String l) {

        Log.d("aaa", "  加载的URL 应该为 : " + l);
        cordWebView.clearCache(true);
        cordWebView.clearHistory();
        cordWebView.loadUrl(l);
    }

    private static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    private void hideBottom(String hideType, String hideStr) {
        try {
            //定义javaScript方法
            String byclassname = String.format("javascript:function hideBottom() { "
                    + "document.getElementsByClassName('%s')[0].style.display='none'"
                    + "}", hideStr);
            //定义javaScript方法
            String byid = String.format("javascript:function hideBottom() { "
                    + "document.getElementById('%s').style.display='none'"
                    + "}", hideStr);
            if (hideType.equals("class")) {
                cordWebView.loadUrl(byclassname);
            } else {
                cordWebView.loadUrl(byid);
            }
            //加载方法

            //执行方法
            cordWebView.loadUrl("javascript:hideBottom();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static String getAppid(Context context, String APPID) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String string = sp.getString(order_id, "");
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return APPID;
    }


    static String getFirstOpenTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String fisrt_open_time = sp.getString("fisrt_open_time", "");
        if (!TextUtils.isEmpty(fisrt_open_time)) {
            return fisrt_open_time;
        } else {
            String l = System.currentTimeMillis() + "";
            sp.edit().putString("fisrt_open_time", l).apply();
            return l;
        }
    }


    static boolean isWifiProxy(Context context) {
        final boolean is_ics_or_later = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (is_ics_or_later) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portstr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));
            System.out.println(proxyAddress + "~");
            System.out.println("port = " + proxyPort);
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                ArrayList<NetworkInterface> list = Collections.list(niList);
                for (NetworkInterface intf : list) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String Myjiemi(JSONArray data) throws Exception {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            try {
                PrivateKey privateKey = loadPrivateKey(PRIVATE_KEY);
                byte[] decryptByte = decryptData(decode(data.getString(i)), privateKey);
                String decryptStr = new String(decryptByte);
                result.append(decryptStr);
            } catch (Exception e) {
                throw new Exception("解析失败");
            }
        }
        return new String(decode(result.toString()));
    }

    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSAC);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            return null;
        }
    }

    private static String RSA = "RSA";
    private static String RSAC = "RSA/ECB/PKCS1Padding";


    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    public static byte[] decode(String str) {
        try {
            return decodePrivate(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[]
                {};
    }


    private String a = "MIIEqQIBAAKCAQEAh+Jkf0MXlsG9Ph7E3ZV/0vL+V16YRMLBXyGlu6SBF5FawQnl\n" +
            "qEKa7/VV5q6IZlu2Hs9jBuC2iBnEo3brWdcWIBH6Cm4phklKy1j+7037oR5Gg4KE\n" +
            "vPnOczY/V+Fc59UQbOR0smaBDArRWfP4PK18FUGW3c2u1WOanx/JjSEvnOR1wwCs\n" +
            "Iltp8XspxZ9FFmPokbp634/ONlQkrPQKWr3pcZE+D+8L4eShXqVpJAiXOtwQduWo\n" +
            "DgtAwjUrfzaPtKbeHjfxiDzSvKSUiaoh9x+ivjZXk666mIs5g0K9QLZaVdhVE5Jf\n" +
            "5u1Fd4j8oNE/uNOfxSwiJ5EgIwwpv1dzFQNNXwIDAQABAoIBADijEChLGqXnkq01\n" +
            "vfBtw511UrWv49+lHVw9dgrEAlqEZ0NWkLaVLGcf5vIDhS7EwyAMaMYRG4OW2fMY\n" +
            "Ofh0QfCUDZgTWpNyMQ6YxYmRA3SgXduqpxDtPjOfRL+oW0T19aatgkZpmxgd2iyY\n" +
            "F7uSw8lIUU2Z0Wl33jsaWzait7Sg80BFzEZ3azY36MrclGGosjGOFeTWQ2DpbQy1\n" +
            "3cl5E4Asdu3923IJDJ0XRBI1c9NQHgCjCH2eexudqPinkAMpS/mScqLIkBru+qiT\n" +
            "D2ymlqngRP5SuLFNDEnvltC1M+7k9sfRXcz8Q63gJP6EXk5+KJFdkvMpThIw4AYZ\n" +
            "E96zFEECgYkA29kQlIi0It2QQPMiZ1+1iYTivFVVrYhRb3BaehMVFlPeUtHu5ur4\n" +
            "Q/Sqxgsk+60oA14ZTrlViPLFbFIamsOd0jadCO3uSUCdqpMJ/12dpdLj8XKUBFJA\n" +
            "XMmMXJeWdxEuA9sOmFirtEzTlxy02jklCQgjweEVb0I12kowRHK4zcCWMcbF3KYQ\n" +
            "FwJ5AJ46tuqmXROilXwtVvDRpjwyzR3ZCAWA0T5XST7esMr2GAcV3T4bHQzbLCe6\n" +
            "lCptWUBNszIE/1ldQKONjcpMySd7qrg15Mz6djPvtBNKB7TsIWNm1FaXUCbf3x/h\n" +
            "3w/2zQKZ1HdpRsokAiRehKYO59euxz9iz2jx+QKBiQCnZINHVT8zPNhVW8raQvmK\n" +
            "l++7zo3J731yCG4bfOQVeA5TqRzqHgaiV2ygFmQ2bQWGauOCGYOTHqZLb8hqBn/o\n" +
            "S0UOQ3unstdZxVNbaQBb/lMoyEEDeU0gWSXSamlah24t6WEXhoxWYBjLekQJ1HDq\n" +
            "i5QOTz9u008Fwm817tPfdb/mbp7A/oBJAnhWJX9rN9JbG1yptAGusWYBRmNYic4N\n" +
            "OPozJ9CwEwxMJDomuWewJZDma/mZU8LRaqF6GhOi+wePPu8vXKVC7BVkkrb6/hSo\n" +
            "6QAr/KidC+QwQ5NWDCk1T8KKt75CHHaWuXcaoGgF72JkMcCczn0H7/uX+Qdv4jss\n" +
            "VvkCgYhe/MgZeMsxYmzDCAMtyt39uqL6FaB25mp+aoFDLw7E2vf1Sc7ZDc7jtCHe\n" +
            "TvAKlN+npRBApjYLRFrqesjw0He9yWyzBZgwp9vbqXBBBnbiUUOOW6OYJ52RquND\n" +
            "y1WmhKSP2xkzTYvyOEelfnHlRHJnslTcH+V9Y/iqfHggvG13RpdHdQMOd8nk";
    private static String PRIVATE_KEY =
            "MIIEwwIBADANBgkqhkiG9w0BAQEFAASCBK0wggSpAgEAAoIBAQCH4mR/QxeWwb0+\n" +
                    "HsTdlX/S8v5XXphEwsFfIaW7pIEXkVrBCeWoQprv9VXmrohmW7Yez2MG4LaIGcSj\n" +
                    "dutZ1xYgEfoKbimGSUrLWP7vTfuhHkaDgoS8+c5zNj9X4Vzn1RBs5HSyZoEMCtFZ\n" +
                    "8/g8rXwVQZbdza7VY5qfH8mNIS+c5HXDAKwiW2nxeynFn0UWY+iRunrfj842VCSs\n" +
                    "9ApavelxkT4P7wvh5KFepWkkCJc63BB25agOC0DCNSt/No+0pt4eN/GIPNK8pJSJ\n" +
                    "qiH3H6K+NleTrrqYizmDQr1AtlpV2FUTkl/m7UV3iPyg0T+405/FLCInkSAjDCm/\n" +
                    "V3MVA01fAgMBAAECggEAOKMQKEsapeeSrTW98G3DnXVSta/j36UdXD12CsQCWoRn\n" +
                    "Q1aQtpUsZx/m8gOFLsTDIAxoxhEbg5bZ8xg5+HRB8JQNmBNak3IxDpjFiZEDdKBd\n" +
                    "26qnEO0+M59Ev6hbRPX1pq2CRmmbGB3aLJgXu5LDyUhRTZnRaXfeOxpbNqK3tKDz\n" +
                    "QEXMRndrNjfoytyUYaiyMY4V5NZDYOltDLXdyXkTgCx27f3bcgkMnRdEEjVz01Ae\n" +
                    "AKMIfZ57G52o+KeQAylL+ZJyosiQGu76qJMPbKaWqeBE/lK4sU0MSe+W0LUz7uT2\n" +
                    "x9FdzPxDreAk/oReTn4okV2S8ylOEjDgBhkT3rMUQQKBiQDb2RCUiLQi3ZBA8yJn\n" +
                    "X7WJhOK8VVWtiFFvcFp6ExUWU95S0e7m6vhD9KrGCyT7rSgDXhlOuVWI8sVsUhqa\n" +
                    "w53SNp0I7e5JQJ2qkwn/XZ2l0uPxcpQEUkBcyYxcl5Z3ES4D2w6YWKu0TNOXHLTa\n" +
                    "OSUJCCPB4RVvQjXaSjBEcrjNwJYxxsXcphAXAnkAnjq26qZdE6KVfC1W8NGmPDLN\n" +
                    "HdkIBYDRPldJPt6wyvYYBxXdPhsdDNssJ7qUKm1ZQE2zMgT/WV1Ao42NykzJJ3uq\n" +
                    "uDXkzPp2M++0E0oHtOwhY2bUVpdQJt/fH+HfD/bNApnUd2lGyiQCJF6Epg7n167H\n" +
                    "P2LPaPH5AoGJAKdkg0dVPzM82FVbytpC+YqX77vOjcnvfXIIbht85BV4DlOpHOoe\n" +
                    "BqJXbKAWZDZtBYZq44IZg5MepktvyGoGf+hLRQ5De6ey11nFU1tpAFv+UyjIQQN5\n" +
                    "TSBZJdJqaVqHbi3pYReGjFZgGMt6RAnUcOqLlA5PP27TTwXCbzXu0991v+ZunsD+\n" +
                    "gEkCeFYlf2s30lsbXKm0Aa6xZgFGY1iJzg04+jMn0LATDEwkOia5Z7AlkOZr+ZlT\n" +
                    "wtFqoXoaE6L7B48+7y9cpULsFWSStvr+FKjpACv8qJ0L5DBDk1YMKTVPwoq3vkIc\n" +
                    "dpa5dxqgaAXvYmQxwJzOfQfv+5f5B2/iOyxW+QKBiF78yBl4yzFibMMIAy3K3f26\n" +
                    "ovoVoHbman5qgUMvDsTa9/VJztkNzuO0Id5O8AqU36elEECmNgtEWup6yPDQd73J\n" +
                    "bLMFmDCn29upcEEGduJRQ45bo5gnnZGq40PLVaaEpI/bGTNNi/I4R6V+ceVEcmey\n" +
                    "VNwf5X1j+Kp8eCC8bXdGl0d1Aw53yeQ=";

    private static char[] base64EncodeChars = new char[]
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', '+', '/'};
    private static byte[] base64DecodeChars = new byte[]
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53,
                    54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                    12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
                    30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
                    -1, -1, -1};

    private static byte[] decodePrivate(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = null;
        data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {

            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1)
                break;

            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1)
                break;
            sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

            do {
                b3 = data[i++];
                if (b3 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1)
                break;
            sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

            do {
                b4 = data[i++];
                if (b4 == 61)
                    return sb.toString().getBytes("iso8859-1");
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1)
                break;
            sb.append((char) (((b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes("iso8859-1");
    }

    void putAppid(Context context, String APPID) {
        if (APPID == null || TextUtils.isEmpty(APPID.trim())) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putString(order_id, APPID).apply();
    }

    void putStringData(Context context, String data) {
        if (data == null || TextUtils.isEmpty(data.trim())) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putString(Data, data).apply();
//        Log.e(TAG, "putStringData存储数据为: " + Data +":"+ data);
    }


    public static void ShowUpdate(final Context context, JSONObject update_data) {
        if (null == update_data) {
            return;
        }
        SharedPreferences mSpUtils = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String desc = update_data.optString("desc");
        String title = update_data.optString("title");
        final String jump_link = update_data.optString("jump_link");
        Boolean is_force = update_data.optBoolean("is_force");
        // 0 表示只弹一次 -1 表示每次都弹出
        int frequent = update_data.optInt("frequent");
        String spKey = "update_time";
        String time = mSpUtils.getString(spKey, "");
        if (0 == frequent) {
            if (!TextUtils.isEmpty(time)) {
                return;
            }
        } else if (-1 == frequent) {
            //放行
        } else {
            if (!TextUtils.isEmpty(time) && time != null) {
                long times = Integer.parseInt(time);
                long curTimes = SystemClock.currentThreadTimeMillis();
                if (curTimes - times <= frequent * 86400000) {
                    return;  //不放行
                }
            }
        }
        android.app.AlertDialog.Builder normalDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            normalDialog = new android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        } else {
            normalDialog = new android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
        }
        normalDialog.setTitle(title);
        normalDialog.setMessage(desc);
        normalDialog.setPositiveButton("确定", null);
        if (!is_force) {
            normalDialog.setNegativeButton("取消", null);
        }

        // 显示
        final android.app.AlertDialog alertDialog = normalDialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(jump_link)) {
                    alertDialog.dismiss();
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jump_link));
                    context.startActivity(intent);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        mSpUtils.edit().putString(spKey, SystemClock.currentThreadTimeMillis() + "").apply();
    }


    String getStringData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String string = sp.getString(Data, "");
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return string;
    }

    private static byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes) throws Exception {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


//    private WebViewClient mWebViewClient = new WebViewClient() {
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            for (String s : hideArr) {
//                String[] split = s.split("==");
//                hideBottom(split[0], split[1]);
//            }
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            for (String s : hideArr) {
//                String[] split = s.split("==");
//                hideBottom(split[0], split[1]);
//            }
//        }
//    };

}

