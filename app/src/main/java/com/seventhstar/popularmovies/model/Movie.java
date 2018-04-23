package com.seventhstar.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    private static final String previewPrefix = "http://image.tmdb.org/t/p/w780";
    private static final String originalPrefix = "http://image.tmdb.org/t/p/original";

    @SerializedName("id")
    private final long mId;
    @SerializedName("original_title")
    private final String mTitle;
    @SerializedName("poster_path")
    private final String mPoster;

    @SerializedName("vote_average")
    private final String mRating;

    @SerializedName("popularity")
    private final String mPopularity;

    @SerializedName("release_date")
    private final String mReleaseDate;

    @SerializedName("overview")
    private final String mOverview;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
//    private List<Genre> genres;


    public String getTitle() {
        return mTitle;
    }

//    public String getImageURL() {
//        return mPoster;
//    }

    public String getOriginalURL() {
        return originalPrefix + mPoster;
    }

    public String getPreviewURL() {
        return previewPrefix + mPoster;
    }

    private Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mPoster = in.readString();
        mRating = in.readString();
        mReleaseDate = in.readString();
        mOverview = in.readString();
        mPopularity = in.readString();
//        genres = in.readArrayList(Genre.class.getClassLoader());
        in.readList(this.genres, (Genre.class.getClassLoader()));
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
        dest.writeList(genres);
    }

    public String getRating() {
        return mRating;
    }

    public String getReleaseDate() {
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
