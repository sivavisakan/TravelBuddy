package edu.cmu.travelbuddy.facebook;

class RowData {
	protected int mId;
	protected String mTitle;
	protected String mDetail;

	RowData(int id, String title, String detail) {
		mId = id;
		mTitle = title;
		mDetail = detail;
	}

	@Override
	public String toString() {
		return mId + " " + mTitle + " " + mDetail;
	}
}
