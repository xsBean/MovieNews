package d.manh.movienow.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {
    private String posterPath;
    private String backgroundPath;
    private String title;
    private String description;
    private String releaseDate;
    private Double rating;
    private ArrayList trailers;
    private ArrayList reviews;

    public Movie(String posterPath,String backgroundPath, String title, String releaseDate, Double rating, String description, ArrayList<Trailer> trailers, ArrayList<Review> reviews ) {
        this.posterPath = posterPath;
        this.backgroundPath = backgroundPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
        this.trailers = trailers;
        this.reviews = reviews;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        backgroundPath = in.readString();
        title = in.readString();
        description = in.readString();
        releaseDate = in.readString();
        rating = in.readDouble();
        trailers = in.readArrayList(null);
        reviews = in.readArrayList(null);
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

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(backgroundPath);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(releaseDate);
        dest.writeDouble(rating);
        dest.writeList(trailers);
        dest.writeList(reviews);
    }
}
