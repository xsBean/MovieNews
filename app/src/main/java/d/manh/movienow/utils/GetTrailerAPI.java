package d.manh.movienow.utils;

import d.manh.movienow.data.StoreContract;
import d.manh.movienow.models.TrailerWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetTrailerAPI {

    String videoPath = "{id}/videos"+ StoreContract.API_PATH
        +StoreContract.API_KEY
        +StoreContract.API_LANGUAGE
        +"en-US";
    @GET(videoPath)
    Call<TrailerWrapper> getTrailerWrapper(@Path("id") String id);

}
