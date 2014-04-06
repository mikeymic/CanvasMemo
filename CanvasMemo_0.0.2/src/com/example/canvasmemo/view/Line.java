package com.example.canvasmemo.view;

import java.util.ArrayList;

import android.graphics.Point;

public class Line {
	private int color;
	private int width;
	private ArrayList<Point> points;

	public Line() {
		points = new ArrayList<Point>();
	}

	public Line(int color, int width) {
		points = new ArrayList<Point>();
		this.color = color;
		this.width = width;
	}

	public int getColor() {
		return color;
	}

	public int getWidth() {
		return width;
	}

	public void addPoint(Point p) {
		points.add(p);
	}

	public ArrayList<Point> getPoints() {
		return points;
	}
}
