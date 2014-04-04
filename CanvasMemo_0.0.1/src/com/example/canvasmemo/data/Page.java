package com.example.canvasmemo.data;

import android.graphics.Bitmap;

public class Page {

	private Bitmap bitmap;
	private int pageIndex;
	/**
	 * @return bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	/**
	 * @return pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param bitmap セットする bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	/**
	 * @param pageIndex セットする pageIndex
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
}
