package com.ecoven.retrofit;

import com.ecoven.retrofit.models.GetProductModal;
import com.ecoven.retrofit.models.MachineListModal;
import com.ecoven.retrofit.models.SuccessResContactUs;
import com.ecoven.retrofit.models.SuccessResForgetPassword;
import com.ecoven.retrofit.models.SuccessResGetActivity;
import com.ecoven.retrofit.models.SuccessResGetAllNews;
import com.ecoven.retrofit.models.SuccessResGetCountryDistance;
import com.ecoven.retrofit.models.SuccessResGetCoutries;
import com.ecoven.retrofit.models.SuccessResGetProfile;
import com.ecoven.retrofit.models.SuccessResLogin;
import com.ecoven.retrofit.models.SuccessResNewsDetails;
import com.ecoven.retrofit.models.SuccessResSignUp;
import com.ecoven.retrofit.models.SuccessResSocialLogin;
import com.ecoven.retrofit.models.SuccessResUpdateProfile;
import com.ecoven.retrofit.models.TermsConditionModal;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EcoVanInterface {

    @Multipart
    @POST("signup")
    Call<SuccessResSignUp> signUp (@Part("name") RequestBody first_name,
                                   @Part("email") RequestBody last_name,
                                   @Part("password") RequestBody email,
                                   @Part("mobile") RequestBody mobile,
                                   @Part("country_code") RequestBody cc,
                                   @Part("country") RequestBody selectedCountry,
                                   @Part("register_id") RequestBody address,
                                   @Part("lat") RequestBody lat,
                                   @Part("lon") RequestBody lng,
                                   @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("login")
    Call<SuccessResLogin> login(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<SuccessResForgetPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("social_login")
    Call<SuccessResSocialLogin> socialLogin (@Part("name") RequestBody first_name,
                                             @Part("email") RequestBody last_name,
                                             @Part("mobile") RequestBody mobile,
                                             @Part("lat") RequestBody lat,
                                             @Part("lon") RequestBody lng,
                                             @Part("register_id") RequestBody registerId,
                                             @Part("social_id") RequestBody socialId,
                                             @Part("language") RequestBody language,
                                             @Part ("image") RequestBody image);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_privacy_policy")
    Call<TermsConditionModal> get_privacy_policy(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("update_profile")
    Call<SuccessResUpdateProfile> updateProfile (@Part("user_id") RequestBody userId,
                                                 @Part("name") RequestBody first_name,
                                                 @Part("email") RequestBody last_name,
                                                 @Part("mobile") RequestBody address,
                                                 @Part("country_code") RequestBody cc,
                                                 @Part("lat") RequestBody lat,
                                                 @Part("lon") RequestBody lng,
                                                 @Part("green_footprint") RequestBody greenFootprint,
                                                 @Part("card_number") RequestBody card_number ,
                                                 @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("get_country")
    Call<SuccessResGetCoutries> getCountries(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_password")
    Call<SuccessResForgetPassword> changePass(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_news")
    Call<SuccessResGetAllNews> getNews(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_related_news")
    Call<SuccessResGetAllNews> getRelatedNews(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_activity")
    Call<SuccessResGetActivity> getActivity(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_product")
    Call<GetProductModal> get_product(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("filter_product_by_price_range")
    Call<GetProductModal> get_product_filter(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("filter_machines")
    Call<MachineListModal> get_machines(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_distance_country_category")
    Call<SuccessResGetCountryDistance> getCountriesDistance(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("news_details")
    Call<SuccessResNewsDetails> getNewsDetails(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_language")
    Call<SuccessResForgetPassword> updateLang(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search_product")
    Call<GetProductModal> searchProducts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("contact_us")
    Call<SuccessResContactUs> contactUs(@FieldMap Map<String, String> paramHashMap);

}
