package com.seventhstar.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("backdrop_path")
    private final String mBackdrop;

    @SerializedName("overview")
    private final String mOverview;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    private String genresString;

    public Movie(String id, String title, String overview, String releaseDate, String rating, String popularity, String posterPath, String backdropPath, String genresString) {

        mId = Long.parseLong(id);
        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mRating = rating;
        mPopularity = popularity;
        mPoster = posterPath;
        mBackdrop = backdropPath;
        this.genresString = genresString;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBackdropURL() {
        if (mBackdrop.contains(originalPrefix)) {
            return mBackdrop;
        }
        return originalPrefix + mBackdrop;
    }

    public String getPreviewURL() {
        if (mPoster.contains(previewPrefix)) {
            return mPoster;
        }
        return previewPrefix + mPoster;
    }

    public Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mRating = in.readString();
        mPopularity = in.readString();
        mPoster = in.readString();
        mBackdrop = in.readString();
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
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mRating);
        dest.writeString(mPopularity);
        dest.writeString(mPoster);
        dest.writeString(mBackdrop);
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

    public String getGenresString() {

        if (genresString != null && !genresString.isEmpty()) return genresString;

        StringBuilder stringBuilder = new StringBuilder();
        for (Genre g : genres) {
            stringBuilder.append(g.getName()).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        genresString = stringBuilder.toString();
        return genresString;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public long getId() {
        return mId;
    }

    public String fieldByColumnId(int i) {
        switch (i) {
            case 1:
                return getTitle();
            case 2:
                return getOverview();
            case 3:
                return getReleaseDate();
            case 4:
                return getRating();
            case 5:
                return getPopularity();
            case 6:
                return getPreviewURL();
            case 7:
                return getBackdropURL();
            case 8:
                return getGenresString();
        }
        return String.valueOf(mId);
    }
}
