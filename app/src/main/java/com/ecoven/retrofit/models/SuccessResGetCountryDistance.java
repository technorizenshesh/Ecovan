package com.ecoven.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccessResGetCountryDistance {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class CategoryDetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("category_name_sp")
        @Expose
        public String categoryNameSp;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("status")
        @Expose
        public String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryNameSp() {
            return categoryNameSp;
        }

        public void setCategoryNameSp(String categoryNameSp) {
            this.categoryNameSp = categoryNameSp;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class CountryDetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("sortname")
        @Expose
        public String sortname;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("currency_code")
        @Expose
        public String currencyCode;
        @SerializedName("currency_name")
        @Expose
        public String currencyName;
        @SerializedName("phone_code")
        @Expose
        public String phoneCode;
        @SerializedName("capital")
        @Expose
        public String capital;
        @SerializedName("flag")
        @Expose
        public String flag;
        @SerializedName("status")
        @Expose
        public String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class DistanceDetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("km")
        @Expose
        public String km;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("country")
        @Expose
        public String country;
        @SerializedName("country_code")
        @Expose
        public String countryCode;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("green_footprint")
        @Expose
        public String greenFootprint;
        @SerializedName("card_number")
        @Expose
        public String cardNumber;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("country_details")
        @Expose
        public List<CountryDetail> countryDetails = null;
        @SerializedName("distance_details")
        @Expose
        public List<DistanceDetail> distanceDetails = null;
        @SerializedName("category_details")
        @Expose
        public List<CategoryDetail> categoryDetails = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getGreenFootprint() {
            return greenFootprint;
        }

        public void setGreenFootprint(String greenFootprint) {
            this.greenFootprint = greenFootprint;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public List<CountryDetail> getCountryDetails() {
            return countryDetails;
        }

        public void setCountryDetails(List<CountryDetail> countryDetails) {
            this.countryDetails = countryDetails;
        }

        public List<DistanceDetail> getDistanceDetails() {
            return distanceDetails;
        }

        public void setDistanceDetails(List<DistanceDetail> distanceDetails) {
            this.distanceDetails = distanceDetails;
        }

        public List<CategoryDetail> getCategoryDetails() {
            return categoryDetails;
        }

        public void setCategoryDetails(List<CategoryDetail> categoryDetails) {
            this.categoryDetails = categoryDetails;
        }

    }

}