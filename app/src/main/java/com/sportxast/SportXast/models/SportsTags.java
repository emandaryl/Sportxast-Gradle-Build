package com.sportxast.SportXast.models;

import java.util.ArrayList;

public class SportsTags {

	private int sportId;
	public static final int SPORTID_DEFAULT = 0;
	public static final int SPORTID_BASEBALL = 3;
	public static final int SPORTID_BASKETBALL = 4;
	public static final int SPORTID_FOOTBALL = 9;
	public static final int SPORTID_SKATEBOARDING = 18;
	public static final int SPORTID_SOCCER = 19;
	public static final int SPORTID_SOFTBALL = 20;
	public static final int SPORTID_TENNIS = 23;
	public static final int SPORTID_TRACK_FIELD = 24;
	public static final int SPORTID_VOLLEYBALL = 26;
	public static final int SPORTID_HOCKEY = 88;
	private ArrayList<String> listTags;
	
	public SportsTags(int sportId) {
		this.sportId = sportId;
		listTags = new ArrayList<String>();
	}
	
	public ArrayList<String> populateTags() {
		switch(this.sportId) {
		case SPORTID_BASEBALL:
			listTags = populateBaseballTags();
			break;
		case SPORTID_BASKETBALL:
			listTags = populateBasketballTags();
			break;
		case SPORTID_FOOTBALL:
			listTags = populateFootballTags();
			break;
		case SPORTID_SKATEBOARDING:
			listTags = populateSkateboardingTags();
			break;
		case SPORTID_SOCCER:
			listTags = populateSoccerTags();
			break;
		case SPORTID_SOFTBALL:
			listTags = populateBaseballTags();
			break;
		case SPORTID_TENNIS:
			listTags = populateTennisTags();
			break;
		case SPORTID_TRACK_FIELD:
			listTags = populateTrackFieldTags();
			break;
		case SPORTID_VOLLEYBALL:
			listTags = populateVolleyballTags();
			break;
		case SPORTID_HOCKEY:
			listTags = populateHockeyTags();
			break;
			default:
				listTags = populateDefaultTags();
		}
		return listTags;
	}
	
	private ArrayList<String> populateDefaultTags() {
		listTags.add("save");
		listTags.add("win");
		listTags.add("foul");
		listTags.add("goal");
		listTags.add("offsides");
		listTags.add("ouch");
		listTags.add("defense");
		listTags.add("offense");
		/*
		 * save", "win", "foul", "goal", "offsides", "ouch", "defense", "offense"};
		 */
		return listTags;
	}
	
	private ArrayList<String> populateBaseballTags() {
		/*
		 * NSArray *sportTags = @[@"single", @"double", @"triple", @"homerun", @"strike", @"ball", @"out", @"foul", @"error", @"steal", @"run"];
		 * Baseball and SoftBall have the same tags.
		 */
		listTags.add("single");
		listTags.add("double");
		listTags.add("triple");
		listTags.add("homerun");
		listTags.add("strike");
		listTags.add("ball");
		listTags.add("out");
		listTags.add("foul");
		listTags.add("error");
		listTags.add("steal");
		listTags.add("run");
		
		return listTags;
	}
	
	private ArrayList<String> populateBasketballTags() {
		/*
		 * String[]{"layup", "jumpShot", "3pt", "dunk", "freeThrow", "assist", "foul", "steal", "turnOver", "block", "rebound", "flop"};
		 */
		listTags.add("layup");
		listTags.add("jumpShot");
		listTags.add("3pt");
		listTags.add("dunk");
		listTags.add("freeThrow");
		listTags.add("assist");
		listTags.add("foul");
		listTags.add("steal");
		listTags.add("turnOver");
		listTags.add("block");
		listTags.add("rebound");
		listTags.add("flop");
		
		return listTags;
	}
	
	private ArrayList<String> populateFootballTags() {
		/*
		 *  = @[@"run", @"pass", @"tackle", @"fumble", @"interception", @"td", @"kick", @"punt", @"penalty", @"safety", @"wow", @"ouch"];
		 */
		listTags.add("run");
		listTags.add("pass");
		listTags.add("tackle");
		listTags.add("fumble");
		listTags.add("interception");
		listTags.add("td");
		listTags.add("kick");
		listTags.add("punt");
		listTags.add("penalty");
		listTags.add("safety");
		listTags.add("wow");
		listTags.add("ouch");
		
		return listTags;
	}
	
	private ArrayList<String> populateSkateboardingTags() {
		/*
		 * @[@"backside", @"fail", @"fliptrick", @"frontside", @"howto", @"kickflip", @"grab", @"ollie", @"tricktip"];
		 */
		listTags.add("backside");
		listTags.add("fail");
		listTags.add("fliptrick");
		listTags.add("frontside");
		listTags.add("howto");
		listTags.add("kickflip");
		listTags.add("grab");
		listTags.add("ollie");
		listTags.add("tricktip");

		return listTags;
	}
	
	private ArrayList<String> populateSoccerTags() {
		/*
		 * []{"goal", "shot", "dribble", "pass", "offside", "freeKick", "save", "defense", "wow", "ouch"};
		 */
		listTags.add("goal");
		listTags.add("shot");
		listTags.add("dribble");
		listTags.add("pass");
		listTags.add("offside");
		listTags.add("freeKick");
		listTags.add("save");
		listTags.add("defense");
		listTags.add("wow");
		listTags.add("ouch");
		
		return listTags;
	}
	
	private ArrayList<String> populateTennisTags() {
		/*
		 * tTags = @[@"ace", @"fault", @"in", @"out", @"dropshot", @"volley", @"lob", @"slice", @"error", @"point", @"set", @"match"];
		 */
		listTags.add("ace");
		listTags.add("fault");
		listTags.add("in");
		listTags.add("out");
		listTags.add("dropshot");
		listTags.add("volley");
		listTags.add("lob");
		listTags.add("slice");
		listTags.add("error");
		listTags.add("point");
		listTags.add("set");
		listTags.add("match");
		
		return listTags;
		
	}
	
	private ArrayList<String> populateTrackFieldTags() {
		/*
		 * tTags = @[@"400relay", @"110hurdles", @"100dash", @"800relay", 
		 * @"400dash", @"300hurdles", @"800run", @"1600medley", @"200dash", 
		 * @"1600relay", @"1600run", @"3200run", @"longjump", @"highjump", 
		 * @"polevault", @"javelin", @"discus", @"shotput"];
		 */
		listTags.add("400relay");
		listTags.add("110hurdles");
		listTags.add("100dash");
		listTags.add("800relay");
		listTags.add("400dash");
		listTags.add("300hurdles");
		listTags.add("800run");
		listTags.add("1600medley");
		listTags.add("200dash");
		listTags.add("1600relay");
		listTags.add("1600run");
		listTags.add("3200run");
		listTags.add("longjump");
		listTags.add("highjump");
		listTags.add("polevault");
		listTags.add("javelin");
		listTags.add("discus");
		listTags.add("shotput");
		
		return listTags;
	}

	private ArrayList<String> populateVolleyballTags() {
		/*
		 * @"ace", @"assist", @"dig", @"kill", @"set", @"shot", @"spike", @"wow", @"ouch"];
		 */
		listTags.add("ace");
		listTags.add("assist");
		listTags.add("dig");
		listTags.add("kill");
		listTags.add("set");
		listTags.add("shot");
		listTags.add("spike");
		listTags.add("wow");
		listTags.add("ouch");
		
		return listTags;
	}

	private ArrayList<String> populateHockeyTags() {
		/*
		 * ew String[]{"goal", "shot", "assist", "icing", "save", "check", "steal", "penalty"};
		 */
		listTags.add("goal");
		listTags.add("shot");
		listTags.add("assist");
		listTags.add("icing");
		listTags.add("save");
		listTags.add("check");
		listTags.add("steal");
		listTags.add("penalty");
		
		return listTags;
	}
}