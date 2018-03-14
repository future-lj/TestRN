package com.learn.rnproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;

public class MainActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private static final int OVERLAY_PERMISSION_CODE = 11;
    private ReactRootView mReactRootView;
    private ReactInstanceManager mInstanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReact();
        checkPermission();
    }


    private void checkPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(!Settings.canDrawOverlays(this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,OVERLAY_PERMISSION_CODE);

            }
        }

    }


    private void initReact(){
        mReactRootView = new ReactRootView(this);
        mInstanceManager = ReactInstanceManager.builder().
                setApplication(getApplication())
                .setJSMainModulePath("index.android")
                .setBundleAssetName("index.android.bundle")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactRootView.startReactApplication(mInstanceManager,"RNProject",null);
        setContentView(mReactRootView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(getApplicationContext(),"没有浮窗权限",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mInstanceManager != null) {
            mInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mInstanceManager != null) {
            mInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mInstanceManager != null) {
            mInstanceManager.onHostDestroy();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mInstanceManager != null) {
            mInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
