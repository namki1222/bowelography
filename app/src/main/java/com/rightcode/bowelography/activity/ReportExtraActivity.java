package com.rightcode.bowelography.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.ArrayListModel;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.adapter.ExtraReportRecyclerViewAdapter;
import com.rightcode.bowelography.databinding.ActivityReportExtraBinding;
import com.rightcode.bowelography.dialog.FavoritePopupDialog;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkApi;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Request.ExtraReportRequest;
import com.rightcode.bowelography.network.Request.ExtraReportRequest_not_sure;
import com.rightcode.bowelography.network.Request.ReportRequest;
import com.rightcode.bowelography.network.Request.favoriteRequest;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.ExtraReportresponse;
import com.rightcode.bowelography.network.Response.favoritelistResponse;
import com.rightcode.bowelography.network.model.data;
import com.rightcode.bowelography.network.model.favorite;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

import java.util.ArrayList;

public class ReportExtraActivity extends BaseActivity<ActivityReportExtraBinding> {

    String Category = "음료";
    int report_user_id = 0;
    String calendar_String = "";
    boolean iv_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_report_extra);
    }
    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("부가기록", view -> finishWithAnim(),"RIGHT");
        report_user_id = getIntent().getIntExtra("report_id",0);
        calendar_String = getIntent().getStringExtra("report_String");
        if(calendar_String.equals("finish")){
            dataBinding.tvStart.setOnClickListener(view->{
                finishWithAnim();
            });
        }else if(calendar_String.equals("report")){
            dataBinding.tvStart.setOnClickListener(view->{
                notSure();
                Intent intent = new Intent(this, ResultAnimeActivity.class);
                intent.putExtra("report_id",report_user_id);
                startActivity(intent); //다음 액티비티 이동
                finish();
            });

        }
        deleteFavorite(report_user_id);
        favoriteReload();
    }

    @Override
    protected void initClickListener() {
        dataBinding.question1.setOnClickListener(view->{
            Toast.makeText(mContext, "괜찮습니다! 대변사진을 포함한 기록이 쌓이면 보고서를 받을 수 있습니다.", Toast.LENGTH_SHORT).show();
        });
        dataBinding.ivCheckbox.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
                iv_check = false;
            }else{
                view.setSelected(true);
                iv_check = true;
            }
        });
        dataBinding.ivFavorite.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
            }else{
                view.setSelected(true);
            }
        });
        dataBinding.tvAddButton.setOnClickListener(view->{
            if(dataBinding.ivFavorite.isSelected()){
                favoriteAdd();
                noFavoriteAdd(null,null,report_user_id,false);
            }else{
                noFavoriteAdd(null,null,report_user_id,false);
            }
        });

        dataBinding.ivEmote.setOnClickListener(view->{
            Intent intent = new Intent(this, FavoritePopupDialog.class);
            startActivityForResult(intent,4000);
            overridePendingTransition(0, 0);
        });
    }
    public void favoriteReload(){
        callApi(NetworkManager.getInstance(mContext).getApiService().favorite_join_list(), new ApiResponseHandler<favoritelistResponse>() {
            @Override
            public void onSuccess(favoritelistResponse result) {
                ArrayList<favorite> data = result.getList();
                ExtraReportRecyclerViewAdapter adapter = new ExtraReportRecyclerViewAdapter(mContext,data);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                dataBinding.rvFavorite.setLayoutManager(layoutManager);
                adapter.setListener((contents, category) -> noFavoriteAdd(contents,category, report_user_id,true));
                adapter.setListener(id -> deleteFavorite(id));
                dataBinding.rvFavorite.setAdapter(adapter);
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(favoritelistResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
    public void noFavoriteReload(int report_id){
        callApi(NetworkManager.getInstance(mContext).getApiService().no_favorite_join_list(report_id), new ApiResponseHandler<favoritelistResponse>() {
            @Override
            public void onSuccess(favoritelistResponse result) {
                ArrayList<favorite> data = result.getList();
                ExtraReportRecyclerViewAdapter adapter = new ExtraReportRecyclerViewAdapter(mContext,data);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                dataBinding.rvNoFavorite.setLayoutManager(layoutManager);
                adapter.setListener(id -> deleteNoFavorite(id,report_id));
                dataBinding.rvNoFavorite.setAdapter(adapter);
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(favoritelistResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
    private void deleteNoFavorite(Integer id,int report_id){
        callApi(NetworkManager.getInstance(mContext).getApiService().delete_no_list(id), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                noFavoriteReload(report_id);
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
    private void deleteFavorite(Integer id){
        callApi(NetworkManager.getInstance(mContext).getApiService().delete_list(id), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                favoriteReload();
            }
            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 4000:
                    String result = data.getStringExtra("result");
                    if(result.equals("emote_1")){
                        Category = null;
                        Category = "식사";
                        Glide.with(mContext).load(R.drawable.mini_emote1).into(dataBinding.ivEmote);
                    }else if(result.equals("emote_2")){
                        Category = null;
                        Category = "음료";
                        Glide.with(mContext).load(R.drawable.mini_emote2).into(dataBinding.ivEmote);
                    }else if(result.equals("emote_3")){
                        Category = null;
                        Category = "약";
                        Glide.with(mContext).load(R.drawable.mini_emote3).into(dataBinding.ivEmote);
                    }else if(result.equals("emote_4")){
                        Category = null;
                        Category = "운동";
                        Glide.with(mContext).load(R.drawable.mini_emote4).into(dataBinding.ivEmote);
                    }else if(result.equals("emote_5")){
                        Category = null;
                        Category = "기타";
                        Glide.with(mContext).load(R.drawable.mini_emote5).into(dataBinding.ivEmote);
                    }
            }
        }
    }
    public void favoriteAdd(){
        favoriteRequest add = new favoriteRequest();
        add.setContents(dataBinding.etString.getText().toString());
        add.setCategory(Category);
        Log.d(add);
        callApi(NetworkManager.getInstance(mContext).getApiService().extra_add_report(add), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                favoriteReload();
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
    public void noFavoriteAdd(String contents,String category,int report_id,boolean favorite){
        ExtraReportRequest add = new ExtraReportRequest();
        if(favorite ==true){
            add.setRecordId(report_id);
            add.setContents(contents);
            add.setCategory(category);

        }else{
            add.setRecordId(report_id);
            add.setContents(dataBinding.etString.getText().toString());
            add.setCategory(Category);
            dataBinding.etString.setText(null);
        }
        callApi(NetworkManager.getInstance(mContext).getApiService().extra_no_add_report(add), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                noFavoriteReload(report_id);
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
    public void notSure(){
        ExtraReportRequest_not_sure body = new ExtraReportRequest_not_sure();
        if(iv_check == true){
            body.setNotSure(true);
        }else{
            body.setNotSure(false);
        }
        callApi(NetworkManager.getInstance(mContext).getApiService().not_sure(body,report_user_id), new ApiResponseHandler<ExtraReportresponse>() {
            @Override
            public void onSuccess(ExtraReportresponse result) {
                favoriteReload();
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(ExtraReportresponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

}