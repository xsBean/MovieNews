package d.manh.movienow.utils;

import d.manh.movienow.data.StoreContract;
import d.manh.movienow.models.ReviewWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetReviewAPI {
    String reviewPath = "{id}/reviews"+ StoreContract.API_PATH
            +StoreContract.API_KEY
            +StoreContract.API_LANGUAGE
            +"en-US";

    @GET(reviewPath)
    Call<ReviewWrapper> getReviewWrapper(@Path("id") String id);
}
