package net.zoogon.astrolog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ObservationsDAO {
	// Database fields
	private SQLiteDatabase database;
	private AstrologDBOpenHelper dbHelper;

	private String[] allColumns = { AstrologDBOpenHelper.OBSERVATION_ID,
			AstrologDBOpenHelper.OBSERVATION_SESSION_ID,
			AstrologDBOpenHelper.OBSERVATION_DATE,
			AstrologDBOpenHelper.OBSERVATION_OBJECT_ID,
			AstrologDBOpenHelper.OBSERVATION_TELESCOPE,
			AstrologDBOpenHelper.OBSERVATION_EYEPIECE,
			AstrologDBOpenHelper.OBSERVATION_BARLOW,
			AstrologDBOpenHelper.OBSERVATION_SEEING,
			AstrologDBOpenHelper.OBSERVATION_RATE,
			AstrologDBOpenHelper.OBSERVATION_NOTES };

	public ObservationsDAO(Context context) {
		dbHelper = new AstrologDBOpenHelper(context,
				AstrologDBOpenHelper.DATABASE_NAME, null,
				AstrologDBOpenHelper.DATABASE_VERSION);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Observation createObservation(long session_id, Date date,
			String object_id, String telescope, String eyepiece, String barlow,
			float seeing, float rate, String notes) {
		ContentValues newValues = new ContentValues();

		String st_date = AstrologDBOpenHelper.formatDateToString(date);

		// preparing row to be inserted
		newValues.put(AstrologDBOpenHelper.OBSERVATION_SESSION_ID, session_id);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_DATE, st_date);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_OBJECT_ID, object_id);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_TELESCOPE, telescope);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_EYEPIECE, eyepiece);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_BARLOW, barlow);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_SEEING, seeing);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_RATE, rate);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_NOTES, notes);

		long insertId = database.insert(
				AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE, null,
				newValues);
		Cursor cursor = database.query(
				AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE, allColumns,
				AstrologDBOpenHelper.OBSERVATION_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Observation newObservation = cursorToObservation(cursor);
		cursor.close();
		return newObservation;
	}

	public int updateObservation(long observation_id, long session_id,
			Date date, String object_id, String telescope, String eyepiece,
			String barlow, float seeing, float rate, String notes) {

		ContentValues newValues = new ContentValues();

		String st_date = AstrologDBOpenHelper.formatDateToString(date);

		// preparing row to be inserted
		newValues.put(AstrologDBOpenHelper.OBSERVATION_SESSION_ID, session_id);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_DATE, st_date);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_OBJECT_ID, object_id);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_TELESCOPE, telescope);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_EYEPIECE, eyepiece);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_BARLOW, barlow);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_SEEING, seeing);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_RATE, rate);
		newValues.put(AstrologDBOpenHelper.OBSERVATION_NOTES, notes);

		String where = AstrologDBOpenHelper.OBSERVATION_ID + "="
				+ observation_id;

		int n_rows = database.update(
				AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE, newValues,
				where, null);
		return n_rows;
	}

	public void deleteObservation(Observation observation) {
		long id = observation.getId();
		Log.w("ObservationDAO", "Observation deleted with id: " + id);
		database.delete(AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE,
				AstrologDBOpenHelper.OBSERVATION_ID + " = " + id, null);
	}

	public Observation getObservation(long id) {
		Observation observation = new Observation();

		// configuring query
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = allColumns;
		String where = AstrologDBOpenHelper.OBSERVATION_ID + "=" + id;
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = null;

		Cursor cursor = database.query(
				AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE,
				result_columns, where, whereArgs, groupBy, having, order);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			observation = cursorToObservation(cursor);
		}
		// Make sure to close the cursor
		cursor.close();
		return observation;
	}

	public List<Observation> getAllObservations() {
		List<Observation> observations = new ArrayList<Observation>();

		// configuring query
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = allColumns;
		String where = null;
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = AstrologDBOpenHelper.OBSERVATION_DATE + " DESC";

		Cursor cursor = database.query(
				AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE,
				result_columns, where, whereArgs, groupBy, having, order);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Observation observation = cursorToObservation(cursor);
			observations.add(observation);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return observations;
	}

	public List<Observation> getObservationsForSession(long session_id) {
		List<Observation> observations = new ArrayList<Observation>();

		// configuring query
		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] result_columns = allColumns;
		String where = AstrologDBOpenHelper.OBSERVATION_SESSION_ID + "="
				+ session_id;
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = AstrologDBOpenHelper.OBSERVATION_DATE + " DESC";

		Cursor cursor = database.query(
				AstrologDBOpenHelper.DATABASE_OBSERVATIONS_TABLE,
				result_columns, where, whereArgs, groupBy, having, order);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Observation observation = cursorToObservation(cursor);
			observations.add(observation);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return observations;
	}

	private Observation cursorToObservation(Cursor cursor) {
		Observation observation = new Observation();

		int indexId = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_ID);
		int indexSessionId = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_SESSION_ID);
		int indexDate = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_DATE);
		int indexObjectId = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_OBJECT_ID);
		int indexTelescope = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_TELESCOPE);
		int indexEyepiece = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_EYEPIECE);
		int indexBarlow = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_BARLOW);
		int indexSeeing = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_SEEING);
		int indexRate = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_RATE);
		int indexNotes = cursor
				.getColumnIndexOrThrow(AstrologDBOpenHelper.OBSERVATION_NOTES);

		observation.setId(cursor.getLong(indexId));
		observation.setSessionId(cursor.getLong(indexSessionId));
		observation.setDate(AstrologDBOpenHelper.formatStringToDate(cursor
				.getString(indexDate)));
		observation.setObjectId(cursor.getString(indexObjectId));
		observation.setTelescope(cursor.getString(indexTelescope));
		observation.setEyepiece(cursor.getString(indexEyepiece));
		observation.setBarlow(cursor.getString(indexBarlow));
		observation.setSeeing(cursor.getFloat(indexSeeing));
		observation.setRate(cursor.getFloat(indexRate));
		observation.setNotes(cursor.getString(indexNotes));
		return observation;
	}

}
