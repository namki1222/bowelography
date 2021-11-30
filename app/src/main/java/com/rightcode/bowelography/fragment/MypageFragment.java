package com.rightcode.bowelography.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightcode.bowelography.activity.DetailReportActivity;
import com.rightcode.bowelography.activity.FAQActivity;
import com.rightcode.bowelography.activity.NoticeMypageActivity;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.GuideActivity;
import com.rightcode.bowelography.activity.PhotoGuideActivity;
import com.rightcode.bowelography.activity.ServiceNoticeActivity;
import com.rightcode.bowelography.databinding.FragmentMypageBinding;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;
import com.rightcode.bowelography.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageFragment extends BaseFragment<FragmentMypageBinding> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MypageFragment() {
        // Required empty public constructor
    }

    public static MypageFragment newInstance() {
        MypageFragment fragment = new MypageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return bindView(R.layout.fragment_mypage, inflater, container);
    }

    @Override
    protected void initBinding() {

    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void initClickListener() {
        binding.tvAccount2.setOnClickListener(view->{
            Intent intent = new Intent(activity, ServiceNoticeActivity.class);
            intent.putExtra("id",1);
            startActivity(intent);
        });
        binding.tvAccount3.setOnClickListener(view->{
            Intent intent = new Intent(activity, ServiceNoticeActivity.class);
            intent.putExtra("id",2);
            startActivity(intent);

        });
        binding.tvAccount4.setOnClickListener(view->{
            Intent intent = new Intent(activity, NoticeMypageActivity.class);
            intent.putExtra("activity","personal");
            startActivity(intent);

        });
        binding.tvCenter1.setOnClickListener(view->{
            Intent intent = new Intent(activity, GuideActivity.class);
            intent.putExtra("activity","mypage");
            startActivity(intent);
        });
        binding.tvCenter2.setOnClickListener(view->{
            Intent intent = new Intent(activity, PhotoGuideActivity.class);
            intent.putExtra("activity","mypage");
            startActivity(intent);
        });
        binding.tvCenter3.setOnClickListener(view->{
            Intent intent = new Intent(activity, NoticeMypageActivity.class);
            startActivity(intent);
        });
        binding.tvCenter4.setOnClickListener(view->{
            Intent intent = new Intent(activity, DetailReportActivity.class);
            startActivity(intent);
        });
        binding.tvCenter5.setOnClickListener(view->{
            Intent intent = new Intent(activity, FAQActivity.class);
            startActivity(intent);
        });
        binding.swPush.setOnCheckedChangeListener((view, isChecked) -> pushOnOff(isChecked));
    }
    public void pushOnOff(boolean check){
        String Token = PreferenceUtil.getInstance(activity).get(PreferenceUtil.PreferenceKey.Token, "");
        callApi(NetworkManager.getInstance(activity).getApiService().pushUpdate(Token,check), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                ToastUtil.show(activity, "푸쉬알림 설정완료!");
            }
            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(activity, result.message);
            }
            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(activity, result.message);
            }
            @Override
            public void onBadRequest(Throwable t) {
                ToastUtil.show(activity, "인터넷에 연결하여주세요");

            }
        });
    }
}