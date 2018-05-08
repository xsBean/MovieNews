package d.manh.movienow.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    private String trailerId;
    private String trailerKey;
    private String trailerName;
    private int trailerSize;
    private String trailerType;

    public Trailer(String trailerId, String trailerKey, String trailerName, int trailerSize, String trailerType) {
        this.trailerId = trailerId;
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
        this.trailerSize = trailerSize;
        this.trailerType = trailerType;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    protected Trailer(Parcel in) {
        trailerId = in.readString();
        trailerKey = in.readString();
        trailerName = in.readString();
        trailerSize = in.readInt();
        trailerType = in.readString();
    }


    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerId);
        dest.writeString(trailerKey);
        dest.writeString(trailerName);
        dest.writeInt(trailerSize);
        dest.writeString(trailerType);
    }
}
