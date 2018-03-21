package com.seventhstar.popularmovies.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.seventhstar.popularmovies.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rm on 20.03.2018.
 */

public class Movie implements Parcelable {

    private static final String previewPrefix = "http://image.tmdb.org/t/p/w780";
    private static final String originalPrefix = "http://image.tmdb.org/t/p/original";


    @SerializedName("id")
    private long mId;
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("poster_path")
    private String mPoster;

    @SerializedName("vote_average")
    private String mRating;

    @SerializedName("popularity")
    private String mPopularity;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("genres")
    private List<Genre> genres = new ArrayList<>();

    public String getTitle() {
        return mTitle;
    }

    public String getImageURL() {
        return mPoster;
    }

    public String getOriginalURL() {
        return originalPrefix + mPoster;
    }

    public String getPreviewURL() {
        return previewPrefix + mPoster;
    }

//    public Movie(long id, String title, String poster) {
//        mId = id;
//        mTitle = title;
//        mPoster = poster;
//    }

    protected Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mPoster = in.readString();
        mRating = in.readString();
        mReleaseDate = in.readString();
        mOverview = in.readString();
        mPopularity = in.readString();
//        genres = in.readArrayList(Genre.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mRating);
        dest.writeString(mReleaseDate);
        dest.writeString(mOverview);
        dest.writeString(mPopularity);
//        dest.writeTypedList(genres);
    }

    public String getRating() {
        return mRating;
    }

    public String getReleaseDate(Context context) {
       return mReleaseDate;
    }

    public String getOverview() {
        return mOverview;
    }


    public String getYear() {
        return mReleaseDate.substring(0, 4);
    }

    public String getPopularity() {
        int point = mPopularity.lastIndexOf(".");
        return (point > 0) ? mPopularity.substring(0, point) : mPopularity;
    }

    public List<Genre> getGenres() {
        return genres;
    }

}
