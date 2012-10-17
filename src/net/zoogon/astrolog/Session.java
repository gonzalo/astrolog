package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Session {
	private long _id;
	private Date date;
	private String title;
	private String location;
	private String notes;

	public long getId() {
		return _id;
	}

	public void setId(long _id) {
		this._id = _id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String toString() {
		String date_st = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(date); 
		return date_st + " - " + this.title;
	}

}
