package d.manh.movienow.utils;

import java.util.ArrayList;

import d.manh.movienow.models.Review;
import d.manh.movienow.models.Trailer;

public interface LoadReviewAndTrailerInt {
    void loadTrailer(ArrayList<Trailer> trailers);
    void loadReview(ArrayList<Review> reviews);
}
