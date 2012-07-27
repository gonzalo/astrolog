package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Observation {
	
	private long _id;
	private Date datetime;
	private String object_id;
	private String telescope;
	private String eyepiece;
	private String barlow;
	private float seeing;
	private float rate;
	private String notes; 
	
	/**
	 * @return the _id
	 */
	public long getId() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void seId(long _id) {
		this._id = _id;
	}
	/**
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}
	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	/**
	 * @return the object_id
	 */
	public String getObject_id() {
		return object_id;
	}
	/**
	 * @param object_id the object_id to set
	 */
	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}
	/**
	 * @return the telescope
	 */
	public String getTelescope() {
		return telescope;
	}
	/**
	 * @param telescope the telescope to set
	 */
	public void setTelescope(String telescope) {
		this.telescope = telescope;
	}
	/**
	 * @return the eyepiece
	 */
	public String getEyepiece() {
		return eyepiece;
	}
	/**
	 * @param eyepiece the eyepiece to set
	 */
	public void setEyepiece(String eyepiece) {
		this.eyepiece = eyepiece;
	}
	/**
	 * @return the barlow
	 */
	public String getBarlow() {
		return barlow;
	}
	/**
	 * @param barlow the barlow to set
	 */
	public void setBarlow(String barlow) {
		this.barlow = barlow;
	}
	/**
	 * @return the seeing
	 */
	public float getSeeing() {
		return seeing;
	}
	/**
	 * @param seeing the seeing to set
	 */
	public void setSeeing(float seeing) {
		this.seeing = seeing;
	}
	/**
	 * @return the rate
	 */
	public float getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(float rate) {
		this.rate = rate;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String toString(){
		
		//something like "11/11/12 - M82"
		String date_st = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(datetime); 
		return date_st + " - " + this.object_id;
	}

}
