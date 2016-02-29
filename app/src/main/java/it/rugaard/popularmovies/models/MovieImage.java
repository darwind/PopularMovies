package it.rugaard.popularmovies.models;

import android.net.Uri;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Kasper on 29/02/16.
 */
public class MovieImage {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({W_92, W_154, W_185, W_342, W_500, W_780, W_ORIGINAL})
    public @interface ImageWidth {}

    public static final String W_92 = "w92";
    public static final String W_154 = "w154";
    public static final String W_185 = "w185";
    public static final String W_342 = "w342";
    public static final String W_500 = "w500";
    public static final String W_780 = "w780";
    public static final String W_ORIGINAL = "original";

    private static final String IMAGE_BASE_URL = "image.tmdb.org";

    private final String backdropPath;
    private @MovieImage.ImageWidth String imageWidthPath;

    public MovieImage(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public MovieImage withWidth(@ImageWidth String imageWidthPath) {
        this.imageWidthPath = imageWidthPath;
        return this;
    }

    public Uri toUri() {
        return new Uri.Builder()
                .scheme("http")
                .authority(IMAGE_BASE_URL)
                .appendPath("t")
                .appendPath("p")
                .appendPath(imageWidthPath != null ? imageWidthPath : "w185")
                .appendEncodedPath(backdropPath)
                .build();
    }
}
