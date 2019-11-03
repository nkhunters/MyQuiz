package com.codelaxy.myquiz.Api;

import com.codelaxy.myquiz.Models.DefaultResponse;
import com.codelaxy.myquiz.Models.HistoryResponse;
import com.codelaxy.myquiz.Models.QuestionResponse;
import com.codelaxy.myquiz.Models.QuizResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("login")
    Single<DefaultResponse> login(@Field("mobile") String mobile,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<DefaultResponse> register(@Field("password") String password,
                                   @Field("name") String name,
                                   @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("changePassword")
    Single<DefaultResponse> changePassword(@Field("old_password") String old_password,
                                     @Field("new_password") String new_password,
                                     @Field("mobile") String mobile);

    @GET("getQuizes")
    Single<QuizResponse> getQuizes();

    @FormUrlEncoded
    @POST("getQuestions")
    Single<QuestionResponse> getQuestions(@Field("quiz_id") String quiz_id);

    @FormUrlEncoded
    @POST("updateRewardCount")
    Single<DefaultResponse> updateRewardCount(@Field("mobile") String mobile, @Field("rewards") int rewards);

    @FormUrlEncoded
    @POST("getUserRewards")
    Single<DefaultResponse> getUserRewards(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("createWithdrawRequest")
    Single<DefaultResponse> createWithdrawRequest(@Field("mobile") String mobile,
                                                  @Field("name") String name,
                                                  @Field("amount") String amount);

    @FormUrlEncoded
    @POST("getHistory")
    Single<HistoryResponse> getHistory(@Field("mobile") String mobile);
}
