package jrr.wow.json;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data of
 * interest downloaded in Json from the Character Service.
 * 
 * 
 * Format sent by Character Service (To be implemented)
 * { "name": "Test Subject", "class": "Fairy Queen", "level": 80, "bounty": 1000000, "rank":1}
 *
 * Data fields in CharacterrData
 private String mName;
 private String mClass;
 private double mLevel;
 private double mBounty;
 private double mRank;
 * 
 */
public class JsonCharacter {
    /**
     * Various tags corresponding to data downloaded in Json from the
     * Weather Service.
     */
	final public static String name_JSON = "name";
    final public static String class_JSON = "class";
    final public static String level_JSON = "level";
    final public static String bounty_JSON = "bounty";
    final public static String rank_JSON = "rank";

    /**
     * Various fields corresponding to data downloaded in Json from
     * the Character Service.
     */
    private String mName;
    private String mClass;
    private double mLevel;
    private double mBounty;
    private double mRank;

    /**
     * Constructor that initializes all the fields of interest.
     */
    public JsonCharacter(String name, String raceclass, double level, double bounty, double rank) {
        mName = name;
        mClass= raceclass;
        mLevel = level;
        mBounty = bounty;
        mRank = rank;

    }
    
    /**
     * No-op constructor.
     */
    public JsonCharacter() {
    }
    // Get set methods for Name
    public String getName() {return mName;}
    public void setName(String name) {mName = name;}
    // Get set methods for Class
    public String getRaceClass() {return mClass;}
    public void setRaceClass(String raceclass) {mClass = raceclass;}
    // Get set methods for Level
    public double getLevel() {return mLevel;}
    public void setLevel(double level) {mLevel = level;}
    // Get set methods for Bounty
    public double getBounty() {return mBounty;}
    public void setBounty(double bounty) {mBounty = bounty;}
    // Get set methods for Rank
    public double getRank() {return mRank;}
    public void setRank(double rank) {mRank = rank;}


}
