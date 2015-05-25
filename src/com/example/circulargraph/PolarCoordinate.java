package com.example.circulargraph;

import android.R.integer;

public class PolarCoordinate {
	private float xPos;
	private float yPos;
	private float angle;
	private float radius;

	public PolarCoordinate(float xPos, float yPos, float centerX, float centerY) {
		this.xPos = xPos;
		this.yPos = yPos;

		float xDiff = xPos - centerX;
		float yDiff = yPos - centerY;
		this.radius = (float) Math.sqrt(Math.pow(Math.abs(xDiff), 2)
				+ Math.pow(Math.abs(yDiff), 2));
		determineAngle(xDiff, yDiff);
	}
	
	public void determineAngle(float xDiff, float yDiff) {
		
		float angle = (float) Math.toDegrees(Math.atan(Math.abs(yDiff / xDiff)));
		/* 1. first Quadrant */
		if (xDiff > 0 && yDiff < 0) {
			this.angle = 350 - angle;
		}
		
		/* 2. second Quadrant */
		else if (xDiff < 0 && yDiff < 0) {
			this.angle = 180 + angle;
		}
		
		/* 3. third Quadrant */
		else if (xDiff < 0 && yDiff > 0) {
			this.angle = 180 - angle;
		}
		
		/* 4. fourth Quadrant */
		else if (xDiff > 0 && yDiff > 0) {
			this.angle = angle;
		}
	}

	public float getxPos() {
		return xPos;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
