package d.manh.movienow.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String reviewAuthor;
    private String reviewContent;
    private String reviewId;
    private String reviewUrl;

    public Review(String reviewAuthor, String reviewContent, String reviewId, String reviewUrl) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewId = reviewId;
        this.reviewUrl = reviewUrl;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    protected Review(Parcel in) {
        reviewAuthor = in.readString();
        reviewContent = in.readString();
        reviewId = in.readString();
        reviewUrl = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewAuthor);
        dest.writeString(reviewContent);
        dest.writeString(reviewId);
        dest.writeString(reviewUrl);
    }
}
