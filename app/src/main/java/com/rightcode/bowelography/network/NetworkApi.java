package com.rightcode.bowelography.network;


import com.rightcode.bowelography.network.Request.ConditionRequest;
import com.rightcode.bowelography.network.Request.ConditionRequest_2;
import com.rightcode.bowelography.network.Request.ExtraReportRequest;
import com.rightcode.bowelography.network.Request.ExtraReportRequest_not_sure;
import com.rightcode.bowelography.network.Request.JoinRequest;
import com.rightcode.bowelography.network.Request.Precision_analysisRequest;
import com.rightcode.bowelography.network.Request.ReportRequest;
import com.rightcode.bowelography.network.Request.favoriteRequest;
import com.rightcode.bowelography.network.Response.AdvertisementResponse;
import com.rightcode.bowelography.network.Response.AdvertisementdetailResponse;
import com.rightcode.bowelography.network.Response.CalendarCondtionResponse;
import com.rightcode.bowelography.network.Response.CalendardayResponse;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.ExtraReportresponse;
import com.rightcode.bowelography.network.Response.FaqResponse;
import com.rightcode.bowelography.network.Response.NoticeContentResponse;
import com.rightcode.bowelography.network.Response.NoticeResponse;
import com.rightcode.bowelography.network.Response.ResultResponse;
import com.rightcode.bowelography.network.Response.StatisticResponse;
import com.rightcode.bowelography.network.Response.UserIdresponse;
import com.rightcode.bowelography.network.Response.CalendarSpotResponse;
import com.rightcode.bowelography.network.Response.ai_photoResponse;
import com.rightcode.bowelography.network.Response.favoritelistResponse;
import com.rightcode.bowelography.network.model.fcmrequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkApi {

    //=========================================
    // register
    // ========================================

    @POST("/v1/auth/register")
    Call<CommonResponse> join(
            @Body JoinRequest body
    );
    @POST("/v1/record/create")
    Call<UserIdresponse> report(
            @Body ReportRequest body
//            @Part MultipartBody.Part image
    );

    //=========================================
    // favorite
    // ========================================
    @POST("/v1/additional-record-favorites/add")
    Call<CommonResponse> extra_add_report(
            @Body favoriteRequest body
    );
    @GET("/v1/additional-record-favorites/list")
    Call<favoritelistResponse> favorite_join_list(
    );
    @DELETE("/v1/additional-record-favorites/{id}")
    Call<CommonResponse> delete_list(
            @Path("id") Integer id
    );


    //=========================================
    // no_favorite
    // ========================================
    @POST("/v1/additional-record/add")
    Call<CommonResponse> extra_no_add_report(
            @Body ExtraReportRequest body
    );
    @GET("/v1/record/{id}/additional")
    Call<favoritelistResponse> no_favorite_join_list(
            @Path("id") Integer id
    );
    @DELETE("/v1/additional-record/{id}")
    Call<CommonResponse> delete_no_list(
            @Path("id") Integer id
    );
    //=========================================
    // recode_Score
    // ========================================
    @GET("/v1/record/{id}/result")
    Call<ResultResponse> record_result(
            @Path("id") Integer id
    );
    @PATCH("/v1/record/{id}/not-sure")
    Call<ExtraReportresponse> not_sure(
            @Body ExtraReportRequest_not_sure body,
            @Path("id") Integer id
    );
    //=========================================
    // Calendar
    // ========================================
    @GET("/v1/calendar")
    Call<CalendarSpotResponse> calendar_change(
            @Query("year") Integer year,
            @Query("month") Integer month
    );
    @GET("/v1/calendar/detail")
    Call<CalendardayResponse> calendar_day_data(
            @Query("year") Integer year,
            @Query("month") Integer month,
            @Query("date") Integer date
    );
    @GET("/v1/calendar/condition")
    Call<CalendarCondtionResponse> calendar_condtion(
            @Query("year") Integer year,
            @Query("month") Integer month,
            @Query("date") Integer date
    );
    //=========================================
    // condition
    // ========================================
    @POST("/v1/condition/record")
    Call<CommonResponse> Condition_add(
            @Body ConditionRequest body
    );
    @POST("/v1/condition/record")
    Call<CommonResponse> Condition_add_2(
            @Body ConditionRequest_2 body
    );

    //=========================================
    // Notice
    // ========================================
    @GET("/v1/notice/list")
    Call<NoticeResponse> notice_reload_list(
            @Query("page") Integer Page,
            @Query("limit") Integer limit

    );
    @GET("/v1/notice/list/user")
    Call<NoticeResponse> notice_reload_user_list(
            @Query("page") Integer Page,
            @Query("limit") Integer limit

    );
    @GET("/v1/notice/{id}")
    Call<NoticeContentResponse> notice_reload(
            @Path("id") Integer id
    );

    //=========================================
    // FAQ
    // ========================================
    @GET("/v1/faq/list")
    Call<FaqResponse> faq_reload(
            @Query("page") Integer Page,
            @Query("limit") Integer limit,
            @Query("category") String category
    );
    //=========================================
    // ADVERTISEMENT
    // ========================================
    @GET("/v1/ad-banner/progress")
    Call<AdvertisementResponse> ad_banner(
    );
    @GET("/v1/ad-banner/{id}")
    Call<AdvertisementdetailResponse> ad_detail(
            @Path("id") Integer id
    );
    //=========================================
    // precision-analysis
    // ========================================
    @POST("/v1/precision-analysis")
    Call<CommonResponse> precision_analysis(
            @Body Precision_analysisRequest body
    );
    //=========================================
    // precision-analysis
    // ========================================

    @GET("/v1/user/statistic")
    Call<StatisticResponse> user_statistic(
            @Query("startDate") String start,
            @Query("endDate") String end
    );

    //=========================================
    // report_photo
    // ========================================
    @Multipart
    @POST("/v1/record/file/register")
    Call<CommonResponse> report_photo(
            @Query("recordId") Integer recorid,
            @Part MultipartBody.Part... images
    );

    //=========================================
    // push
    // ========================================
    @POST("/v1/push/register")
    Call<CommonResponse> notificationRegister(
            @Body fcmrequest body
    );
    @PUT("/v1/push/update")
    Call<CommonResponse> pushUpdate(
            @Query("pushToken") String token,
            @Query("active") boolean active
    );
    //=========================================
    // photo_ai
    // ========================================

    @Multipart
    @POST("/main/")
    Call<ai_photoResponse> ai_photo(
            @Part MultipartBody.Part... file
    );
}
