package com.example.circulargraph;

public class Portion {
	private float percentage;
	private int color;
	private String text;
	private float startAngle;
	private float engAngle;
	private boolean isFocused;

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}

	public float getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(float startAngle) {
		this.startAngle = startAngle;
	}

	public float getEngAngle() {
		return engAngle;
	}

	public void setEngAngle(float engAngle) {
		this.engAngle = engAngle;
	}

	public Portion(float percetage, int color, String text) {
		this.percentage = percetage;
		this.color = color;
		this.text = text;
		this.isFocused = false;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
