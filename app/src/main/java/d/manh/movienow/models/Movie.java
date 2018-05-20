package d.manh.movienow.models;


import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Movie implements Parcelable {
    private int movieId;
    private String posterPath;
    private String backgroundPath;
    private String title;
    private String description;
    private String releaseDate;
    private Double rating;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    public Movie(int movieId, String posterPath, String backgroundPath, String title, String releaseDate, Double rating, String description, ArrayList<Trailer> trailers, ArrayList<Review> reviews ) {
        this.movieId = movieId;
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
        movieId = in.readInt();
        posterPath = in.readString();
        backgroundPath = in.readString();
        title = in.readString();
        description = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
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

    public void setMovieId(int id) {this.movieId = id;}
    public int getMovieId() {
        return movieId;
    }
    public void setReviews(ArrayList<Review> reviews){ this.reviews = reviews;}
    public ArrayList getReviews() {
        return reviews;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ArrayList getTrailers() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(movieId);
        dest.writeString(posterPath);
        dest.writeString(backgroundPath);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(releaseDate);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
    }


}
