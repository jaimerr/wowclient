package jrr.wow.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a Plain Old Java Object (POJO) used for data
 * transport within the CharacterService app.  This POJO implements the
 * Parcelable interface to enable IPC between the Main Activity and
 * the CharacterServiceAsync.
 *  Format sent by Character Service (To be implemented)
 * { "name": "Test Subject", "class": "Fairy Queen", "level": 80, "bounty": 1000000, "rank":1}
 *
 * Parcelable defines an interface for marshaling/de-marshaling
 * https://en.wikipedia.org/wiki/Marshalling_(computer_science)
 * to/from a format that Android uses to allow data transport between
 * processes on a device.  Discussion of the details of Parcelable is
 * outside the scope of this assignment, but you can read more at
 * https://developer.android.com/reference/android/os/Parcelable.html.
 */
public class CharacterData implements Parcelable {
    /*
     * These data members are the local variables that will store the
     * WCharacterData's state
     */
    private String mName;
    private String mClass;
    private double mLevel;
    private double mBounty;
    private double mRank;

    /**
     * Constructor
     * 
     * @param name
     * @param raceclass
     * @param level
     * @param bounty
     * @param rank
     */
    public CharacterData(String name,
                       String raceclass,
                       double level,
                       double bounty, double rank) {
        mName = name;
        mClass = raceclass;
        mLevel = level;
        mBounty = bounty;
        mRank = rank;

    }

    /**
     * Provides a printable representation of this object.
     */
    @Override
    public String toString() {
        return "CharacterData [name=" + mName
            + ", class=" + mClass
            + ", level=" + mLevel
            + ", bounty=" + mBounty
            + ", rank=" + mRank
            + "]";
    }

    /*
     * BELOW THIS is related to Parcelable Interface.
     */

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this instance out to byte contiguous memory.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mClass);
        dest.writeDouble(mLevel);
        dest.writeDouble(mBounty);
        dest.writeDouble(mRank);

    }

    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an CharacterData from the Parcel of data.
     * <p>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * @param in
     */
    private CharacterData(Parcel in) {
        mName = in.readString();
        mClass = in.readString();
        mLevel = in.readDouble();
        mBounty = in.readDouble();
        mRank = in.readDouble();
    }
    public String getName() {return mName;}
    public String getRaceClass() {return mClass;}
    public double getLevel() {return mLevel;}
    public double getBounty() {return mBounty;}
    public double getRank() {return mRank;}
    /**
     * public Parcelable.Creator for WeatherData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<CharacterData> CREATOR =
            new Parcelable.Creator<CharacterData>() {
                public CharacterData createFromParcel(Parcel in) {
                    return new CharacterData(in);
                }

                public CharacterData[] newArray(int size) {
                    return new CharacterData[size];
                }
            };
}
