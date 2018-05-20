package d.manh.movienow.models;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import d.manh.movienow.models.Trailer;

public class TrailerWrapper {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<Trailer> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }

}
