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
import com.senaschapinas.flows.ForgotPassword.ForgotPasswordRequest;
import com.senaschapinas.flows.LogIn.LogInResponse;
import com.senaschapinas.flows.LogIn.LoginRequest;
import com.senaschapinas.flows.RemoveTraduction.RemoveTraductionRequest;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRequest;
import com.senaschapinas.flows.ReportVideo.ReportVideoRequest;
import com.senaschapinas.flows.SendTraduction.SendTraductionRequest;
import com.senaschapinas.flows.SendTraduction.SendTraductionResponse;
import com.senaschapinas.flows.SendVideo.SendVideoResponse;
import com.senaschapinas.flows.SignUp.SignUpRequest;
import com.senaschapinas.flows.SignUp.SignUpResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    @Multipart
    @POST("send_video")
    Call<SendVideoResponse> doSendVideo(
            @Part("id_user") RequestBody id_user,
            @Part MultipartBody.Part video
    );

    @POST("report_video")
    Call<Void> reportVideo(@Body ReportVideoRequest request);

    @Multipart
    @POST("fav_video")
    Call<Void> favVideo(
            @Part("id_user") RequestBody id_user,
            @Part("id_video") RequestBody id_video,
            @Part MultipartBody.Part image
    );

    @POST("remove_fav_video")
    Call<Void> removeVideo(@Body RemoveVideoRequest request);

    @POST("send_traduction")
    Call<SendTraductionResponse> sendTraduction(@Body SendTraductionRequest request);

    @POST("fav_traduction")
    Call<Void> favTraduction(@Body FavTraductionRequest request);

    @POST("remove_fav_traduction")
    Call<Void> removeTraduction(@Body RemoveTraductionRequest request);


    @POST("add_dictionary")
    Call<Void> addDictionary(@Body AddDictionaryRequest request);

    @DELETE("remove_dictionary")
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
