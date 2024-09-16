package com.senaschapinas.base;

import com.senaschapinas.flows.AddDictionary.AddDictionaryRequest;
import com.senaschapinas.flows.AddStreak.AddStreakRequest;
import com.senaschapinas.flows.ChangePassword.ChangePasswordRequest;
import com.senaschapinas.flows.FavTraduction.FavTraductionRequest;
import com.senaschapinas.flows.GetDictionary.GetDictionaryRequest;
import com.senaschapinas.flows.GetDictionary.GetDictionaryResponse;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoRequest;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoResponse;
import com.senaschapinas.flows.GetVideo.GetVideoRequest;
import com.senaschapinas.flows.GetVideo.GetVideoResponse;
import com.senaschapinas.flows.LogIn.FavVideo.FavVideoRequest;
import com.senaschapinas.flows.ForgotPassword.ForgotPasswordRequest;
import com.senaschapinas.flows.LogIn.LogInResponse;
import com.senaschapinas.flows.LogIn.LoginRequest;
import com.senaschapinas.flows.ReportVideo.ReportVideoRequest;
import com.senaschapinas.flows.SendTraduction.SendTraductionRequest;
import com.senaschapinas.flows.SendVideo.SendVideoRequest;
import com.senaschapinas.flows.SendVideo.SendVideoResponse;
import com.senaschapinas.flows.SignUp.SignUpRequest;
import com.senaschapinas.flows.SignUp.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface serviceAPI {

    @POST("login")
    Call<LogInResponse> doLogIn(@Body LoginRequest request);

    @POST("signup")
    Call<SignUpResponse> doSignUp(@Body SignUpRequest request);

    @POST("forgot_password")
    Call<Void> doForgotPassword(@Body ForgotPasswordRequest request);


    @POST("change_password")
    Call<Void> doChangePassword(@Body ChangePasswordRequest request);


    @POST("send_video")
    Call<SendVideoResponse> doSendVideo(@Body SendVideoRequest request);

    @POST("report_video")
    Call<Void> reportVideo(@Body ReportVideoRequest request);

    @POST("fav_video")
    Call<Void> favVideo(@Body FavVideoRequest request);

    @DELETE("remove_video/{id_video}")
    Call<Void> removeVideo(@Query("id_video") String idVideo);

    @POST("send_traduction")
    Call<Void> sendTraduction(@Body SendTraductionRequest request);

    @POST("fav_traduction")
    Call<Void> favTraduction(@Body FavTraductionRequest request);

    @DELETE("remove_traduction/{id_sentence}")
    Call<Void> removeTraduction(@Query("id_sentence") String idSentence);

    @POST("add_dictionary")
    Call<Void> addDictionary(@Body AddDictionaryRequest request);

    @DELETE("remove_dictionary/{id_user}/{id_word}")
    Call<Void> removeDictionary(@Query("id_user") String idUser, @Query("id_word") String idWord);

    @POST("get_dictionary")
    Call<GetDictionaryResponse> getDictionary(@Body GetDictionaryRequest request);

    @POST("get_user_info")
    Call<GetUserInfoResponse> getUserInfo(@Body GetUserInfoRequest request);

    @DELETE("delete_user")
    Call<Void> deleteUser(@Query("id_user") String idUser);


    @POST("add_streak")
    Call<Void> addStreak(@Body AddStreakRequest request);

    @POST("get_video")
    Call<GetVideoResponse> getVideo(@Body GetVideoRequest request);




}
