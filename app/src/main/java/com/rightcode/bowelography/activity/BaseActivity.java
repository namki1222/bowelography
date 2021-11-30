package com.rightcode.bowelography.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.dialog.CommonTextDialog;
import com.rightcode.bowelography.dialog.LoadingDialog;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.Common_1Response;
import com.rightcode.bowelography.util.FileUtil;
import com.rightcode.bowelography.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected T dataBinding;
    protected Context mContext;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private CommonTextDialog errorDialog;
    private LoadingDialog mLoadingDialog;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    protected void bindView(int layoutId){
        dataBinding = DataBindingUtil.setContentView(this, layoutId);
        initActivity();
        initClickListener();
    }
    public void startActivity(Intent intent) {
        // 더블 탭으로 인한 이중 액션 막기
        long now = System.currentTimeMillis();
        Log.d(now - mLastClickTime);
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_activity_from_right, R.anim.slide_out_activity_to_left);
    }
    public void startActivityForResult(Intent intent, int requestCode) {
        // 더블 탭으로 인한 이중 액션 막기
        super.startActivityForResult(intent, requestCode);
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        overridePendingTransition(R.anim.slide_in_activity_from_right, R.anim.slide_out_activity_to_left);
    }
    public MultipartBody.Part transformImageToMultipart(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData("image", file.getName(), requestBody);
    }
    public MultipartBody.Part aiTransformImageToMultipart(File file) {
        Log.d(file.getAbsolutePath()+"경로");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestBody);
    }
    protected abstract void initActivity();
    protected abstract void initClickListener();

    public void finishWithAnim() {
        finish();
        overridePendingTransition(R.anim.slide_in_activity_from_left, R.anim.slide_out_activity_to_right);
    }
    public <Q extends CommonResponse> void callApi(Call<Q> call, ApiResponseHandler<Q> handler) {
        Callback<Q> callback = new Callback<Q>() {
            @Override
            public void onResponse(Call<Q> call, Response<Q> response) {
                Q result = response.body();
                Log.d(result+"!!!!");
                if (response.code() == 200 || response.code() == 201) {
                    handler.onSuccess(result);
                } else if (response.code() > 201) {
                    if (response.body() != null) {
                        CommonResponse error = response.body();
                        handler.onServerFail(error);
                        return;
                    }
                    if (response.errorBody() == null) {
                        showErrorDialog(getString(R.string.error_msg));
                        return;
                    }
                    try {
                        Gson gson = new Gson();
                        CommonResponse error = gson.fromJson(response.errorBody().string(), CommonResponse.class);
                        handler.onServerFail(error);
                    } catch (Exception e) {
                        Log.e(e.getMessage());
                    }
                } else {
                    handler.onNoResponse(result);
                }
            }

            @Override
            public void onFailure(Call<Q> call, Throwable t) {
                Log.e(t.getMessage());
                handler.onBadRequest(t);
            }
        };

        call.enqueue(callback);
    }
    public <Q extends CommonResponse> void callApiWithLoading(Call<Q> call, ApiResponseHandler<Q> handler) {
        showLoading();
        Callback<Q> callback = new Callback<Q>() {
            @Override
            public void onResponse(Call<Q> call, Response<Q> response) {
                Q result = response.body();
                if (response.code() == 200 || response.code() == 201) {
                    hideLoading();
                    handler.onSuccess(result);
                } else if (response.code() > 201) {
                    hideLoading();
                    if (response.body() != null) {
                        CommonResponse error = response.body();
                        handler.onServerFail(error);
                        return;
                    }
                    if (response.errorBody() == null) {
                        showErrorDialog(getString(R.string.error_msg));
                        return;
                    }
                    try {
                        Gson gson = new Gson();
                        CommonResponse error = gson.fromJson(response.errorBody().string(), CommonResponse.class);
                        handler.onServerFail(error);
                    } catch (Exception e) {
                        Log.e(e.getMessage());
                    }
                } else {
                    hideLoading();
                    handler.onNoResponse(result);
                }
            }

            @Override
            public void onFailure(Call<Q> call, Throwable t) {
                hideLoading();
                Log.e(t.getMessage());
                handler.onBadRequest(t);
            }
        };

        call.enqueue(callback);
    }
    public void showErrorDialog(String message) {
        showErrorDialog(message, v -> errorDialog.dismiss());
    }

    public void showErrorDialog(String message, View.OnClickListener listener) {
        if (errorDialog == null) {
            errorDialog = new CommonTextDialog(this);
        }

        errorDialog.setTitle("에러");
        errorDialog.setMessage(message);
        errorDialog.setPositiveButton("확인", listener);

        if (errorDialog != null && !errorDialog.isAdded()) {
            errorDialog.show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Glide.get(this).clearMemory();
        if (mLoadingDialog != null)
            mLoadingDialog = null;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_activity_from_left, R.anim.slide_out_activity_to_right);
    }
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }

        if (!mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.show();
            Log.d("isShowing");
        }
    }
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            Log.d("isHide");
        }
    }

}
