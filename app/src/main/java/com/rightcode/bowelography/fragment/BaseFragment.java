package com.rightcode.bowelography.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;


import com.google.gson.Gson;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.BaseActivity;
import com.rightcode.bowelography.dialog.CommonTextDialog;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.util.Log;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class BaseFragment<T extends ViewDataBinding> extends Fragment {

    public T binding;
    public Activity activity;
    private static final long CLICK_TIME_INTERVAL = 300;
    private long mLastClickTime = System.currentTimeMillis();
    private CommonTextDialog errorDialog;

    public static final String RESPONSE_NULL_MSG = "서버로부터 응답이 존재하지 않습니다.";

    protected View bindView(int layoutId, @NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        ButterKnife.bind(this, binding.getRoot());
        initBinding();
        initFragment();
        initClickListener();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            activity = (Activity) context;
        }
    }

    public void startActivity(Intent intent) {
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).startActivity(intent);
        }
    }
    abstract protected void initBinding();
    abstract protected void initFragment();
    abstract protected void initClickListener();

    public <Q extends CommonResponse> void callApi(Call<Q> call, ApiResponseHandler<Q> handler) {
        Callback<Q> callback = new Callback<Q>() {
            @Override
            public void onResponse(Call<Q> call, Response<Q> response) {
                Q result = response.body();
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
    public void showErrorDialog(String message) {
        showErrorDialog(message, v -> errorDialog.dismiss());
    }
    public void showErrorDialog(String message, View.OnClickListener listener) {
        if (errorDialog == null) {
            errorDialog = new CommonTextDialog(activity);
        }

        errorDialog.setTitle("에러");
        errorDialog.setMessage(message);
        errorDialog.setPositiveButton("확인", listener);

        if (errorDialog != null && !errorDialog.isAdded()) {
            errorDialog.show();
        }
    }

}
