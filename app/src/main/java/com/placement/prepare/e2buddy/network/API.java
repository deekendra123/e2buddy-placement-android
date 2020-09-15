package com.placement.prepare.e2buddy.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    @Headers({"Accept: application/json"})
    @POST("ppGetSubCategory.php")
    @FormUrlEncoded
    Call <Object> get_sub_category(@Field("categoryId") int categoryId);


    @Headers({"Accept: application/json"})
    @POST("ppGetCategory.php")
    Call <Object> get_category();

    @Headers({"Accept: application/json"})
    @POST("userLogin.php")
    @FormUrlEncoded
    Call <Object> user_login(@Field("name") String name, @Field("email") String email, @Field("profilePic") String profilePic, @Field("date") String date, @Field("time") String time);

    @Headers({"Accept: application/json"})
    @POST("ppGetQuestion.php")
    @FormUrlEncoded
    Call <Object> get_question(@Field("categoryId") int categoryId, @Field("subCategoryId") int subCategoryId);

    @Headers({"Accept: application/json"})
    @POST("ppInsertAnswer.php")
    @FormUrlEncoded
    Call <Object> insert_user_answer(@Field("ppId") int ppId, @Field("userId") int userId, @Field("questionId") int questionId, @Field("answer") String answer, @Field("status") String status, @Field("date") String date, @Field("time") String time, @Field("answerStatus") String answerStatus);

    @Headers({"Accept: application/json"})
    @POST("ppGetUserAnswer.php")
    @FormUrlEncoded
    Call <Object> get_user_answer(@Field("userId") int userId, @Field("ppId") int ppId);

    @Headers({"Accept: application/json"})
    @POST("ppGetUserResult.php")
    @FormUrlEncoded
    Call <Object> get_user_result(@Field("userId") int userId, @Field("ppId") int ppId, @Field("questionId") int questionId);

    @Headers({"Accept: application/json"})
    @POST("ppInsertUserMark.php")
    @FormUrlEncoded
    Call <Object> insert_user_marks(@Field("ppId") int ppId, @Field("userId") int userId, @Field("correctAnswer") int correctAnswer, @Field("wrongAnswer") int wrongAnswer, @Field("ppMark") int ppMark, @Field("date") String date, @Field("time") String time);


    @Headers({"Accept: application/json"})
    @POST("ppGetInternShipDomain.php")
    Call <Object> get_internship_domain();

    @Headers({"Accept: application/json"})
    @POST("ppInternshipRequest.php")
    @FormUrlEncoded
    Call <Object> insert_request(@Field("userId") int userId, @Field("userName") String userName, @Field("domainId") int domainId, @Field("requestDate") String requestDate, @Field("internshipDuration") String internshipDuration);

}
