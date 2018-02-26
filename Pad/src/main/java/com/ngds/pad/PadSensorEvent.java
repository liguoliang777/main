package com.ngds.pad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/11/29.
 */

public class PadSensorEvent extends BaseEvent implements Parcelable {
    public static final Creator<PadSensorEvent> CREATOR = new Creator<PadSensorEvent>(){
        @Override
        public PadSensorEvent createFromParcel(Parcel source) {
            return new PadSensorEvent(source);
        }

        @Override
        public PadSensorEvent[] newArray(int size) {
            return new PadSensorEvent[size];
        }
    };

    public long timestamp;
    public final float[] vals;

    public PadSensorEvent(long eventTime, int deviceId, int valueSize){
        super(eventTime, deviceId);
        vals = new float[valueSize];
        timestamp = eventTime;
    }

    protected PadSensorEvent(Parcel in){
        super(in);
        vals = new float[in.readInt()];
        in.readFloatArray(vals);
        timestamp = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(vals.length);
        dest.writeFloatArray(vals);
        dest.writeLong(timestamp);
    }
}
