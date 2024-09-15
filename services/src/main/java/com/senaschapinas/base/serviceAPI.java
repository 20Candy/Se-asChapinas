package com.senaschapinas.base;

import com.senaschapinas.flows.AddDictionary.AddDictionaryRequest;
import com.senaschapinas.flows.ChangePassword.ChangePasswordRequest;
import com.senaschapinas.flows.FavTraduction.FavTraductionRequest;
import com.senaschapinas.flows.GetDictionary.GetDictionaryRequest;
import com.senaschapinas.flows.GetDictionary.GetDictionaryResponse;
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
    Call<Void> removeVideo(@Path("id_video") String idVideo);

    @POST("send_traduction")
    Call<Void> sendTraduction(@Body SendTraductionRequest request);

    @POST("fav_traduction")
    Call<Void> favTraduction(@Body FavTraductionRequest request);

    @DELETE("remove_traduction/{id_sentence}")
    Call<Void> removeTraduction(@Path("id_sentence") String idSentence);

    @POST("add_dictionary")
    Call<Void> addDictionary(@Body AddDictionaryRequest request);

    @DELETE("remove_dictionary/{id_user}/{id_word}")
    Call<Void> removeDictionary(@Path("id_user") String idUser, @Path("id_word") String idWord);

    @POST("get_dictionary")
    Call<GetDictionaryResponse> getDictionary(@Body GetDictionaryRequest request);



}
