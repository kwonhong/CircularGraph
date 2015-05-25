package com.example.circulargraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CircularGraph extends View {

	private int colors[] = { Color.parseColor("#AAF2C249"),
			Color.parseColor("#AAE6772E"), Color.parseColor("#AA4DB3B3"),
			Color.parseColor("#AAE64A45"), Color.parseColor("#AA3D4C53"),
			Color.parseColor("#AAFFFFFF") };

	private int mFilterColors[] = { Color.parseColor("#55ffffff"),
			Color.parseColor("#55ffffff"), Color.parseColor("#55ffffff"),
			Color.parseColor("#55ffffff"), Color.parseColor("#55ffffff"),
			Color.parseColor("#55ffffff") };

	/* Default Colors */
	private static int DEFAULT_STROKE_COLOR = Color.parseColor("#F2C249");
	private static int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	private static int DEFAULT_PORTION_COLOR = Color.CYAN;
	private static int DEFAULT_FILL_COLOR = Color.parseColor("#FFFFFF");
	private static int DEFAULT_PORTION_GAP = 0;
	private static int DEFAULT_INNER_TO_VOID_GAP = 20;
	private static int INNER_TO_OUTER_RATIO = 6;;
	private static int DEFAULT_GRAPH_TO_TEXT_GAP = 40;
	private static int DEFAULT_TEXT_HEIGHT_GAP = 50;
	private static int DEFAULT_TEXT_SIZE = 20;
	private static int UNFOCUSED_RECT_SIZE = 10;
	private static int FOCUSED_GAP = 2;
	private static int DEFAULT_GAP = 10;
	private int PERCENTAGE_TEXT_GAP = 250;
	private static int DEFAULT_PERCENTAGE_TEXT_SIZE = 150;
	private static int DEFAULT_PERCENTAGE_SUBTEXT_SIZE = 40;
	private static float GRAPH_TO_TEXT_WIDTH_RATIO = 2 / 3f;
	private static float PERCENTAGE_BOUNDARY = 0.03f;
	private static String DEFAULT_PORTION_TEXT = "Other";
	private static String LOG_GRAPH = "circularGraphLog";

	private ArrayList<Portion> mPortions;

	private Paint mStrokePaint;
	private Paint mBackgroundPaint;
	private Paint mFillPaint;
	private Paint mPercentageStroke;

	private int mInnerSize;
	private int canvasSize;

	private RectF mInnerRectF;
	private RectF mOuterRectF;
	private RectF mVoidRectf;

	private int mGraphWidth;

	private float mGraphCenterX;
	private float mGraphCenterY;

	private int mTextHeightGap;
	private int mGraphToTextGap;

	private boolean isSubtitleOn = true;
	private boolean isSubtitleOutside = true;
	private boolean isImageMiddleOn = true;
	private Bitmap image;

	/* Function declaration starts from here on */
	/* 1. Constructors */
	public CircularGraph(Context context) {
		super(context);
		init(null, 0);
		// TODO Auto-generated constructor stub
	}

	public CircularGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public CircularGraph(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	/* 2. Initialization - after constructor is called */
	private void init(AttributeSet attrs, int defStyle) {

		/* 1. initializing variables */
		mInnerRectF = new RectF();
		mOuterRectF = new RectF();
		mVoidRectf = new RectF();

		mBackgroundPaint = new Paint();
		mStrokePaint = new Paint();
		mFillPaint = new Paint();
		mPercentageStroke = new Paint();

		mPortions = new ArrayList<Portion>();

		mBackgroundPaint.setColor(DEFAULT_BACKGROUND_COLOR);
		mStrokePaint.setColor(DEFAULT_STROKE_COLOR);
		mFillPaint.setColor(DEFAULT_FILL_COLOR);

		/* 2. initializing text stroke */
		mTextHeightGap = DEFAULT_TEXT_HEIGHT_GAP;
		mGraphToTextGap = DEFAULT_GRAPH_TO_TEXT_GAP;
		mStrokePaint.setTextSize(DEFAULT_TEXT_SIZE);
		mPercentageStroke.setTextSize(DEFAULT_PERCENTAGE_TEXT_SIZE);

	}

	/* 3. Drawing */
	@Override
	protected void onDraw(Canvas canvas) {

		/* 1. Setting the canvasSize square (height = width) */
		setUpGraphDimension(canvas);
		setUpInnerRectFParameter();
		setUpOuterRectFParameter();
		setUpVoidRectFParameter();

		/* 3. Setting the center coordinates */
		mGraphCenterX = mInnerRectF.centerX();
		mGraphCenterY = mInnerRectF.centerY();

		/* 4. Drawing each portions */
		/* Calculating the totalPercentage available excluding the gaps */
		float totalPercentage = 360f - FOCUSED_GAP;
		int startingDegree = DEFAULT_PORTION_GAP;
		Paint arcPaint = new Paint();
		Paint filterPaint = new Paint();
		int focusedIndex = 0;

		for (int i = 0; i < mPortions.size(); i++) {
			Portion portion = mPortions.get(i);
			if (portion.isFocused()) {
				mOuterRectF.inset(-UNFOCUSED_RECT_SIZE, -UNFOCUSED_RECT_SIZE);
				startingDegree += FOCUSED_GAP;
				focusedIndex = i;
			}

			/* a. setting the color of the paint */
			arcPaint.setColor(colors[i]);
			filterPaint.setColor(mFilterColors[i]);

			/* b. drawing the arc */
			float endingDegree = totalPercentage * portion.getPercentage();
			canvas.drawArc(mOuterRectF, startingDegree, endingDegree, true,
					arcPaint);
			canvas.drawArc(mInnerRectF, startingDegree, endingDegree, true,
					filterPaint);

			/* c. updating the starting degree */
			portion.setStartAngle(startingDegree);
			portion.setEngAngle(startingDegree + endingDegree);
			startingDegree += totalPercentage * portion.getPercentage()
					+ DEFAULT_PORTION_GAP;

			if (portion.isFocused()) {
				mOuterRectF.inset(UNFOCUSED_RECT_SIZE, UNFOCUSED_RECT_SIZE);
				startingDegree += FOCUSED_GAP;

			}
		}

		/* 5. Drawing hole in the middle */
		canvas.drawArc(mVoidRectf, 0, 360, true, mFillPaint);
		drawClickedPercentage(canvas, focusedIndex);
		drawTextOnSide(canvas);

		/* 6. Drawing the image in the middle */
//		if (isImageMiddleOn) {
//			drawImageInMiddle(canvas);
//		}

	}

//	private void drawImageInMiddle(Canvas canvas) {
//		int size = mGraphWidth/2;
//		if (image != null) {
//			
//			Paint imagePaint = new Paint();
//			canvas.drawCircle(mGraphCenterX, mGraphCenterY, size/2, imagePaint);
//			
//			BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(
//					image, size, size, false),
//					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//			 imagePaint.setShader(shader);
//			 canvas.drawCircle(mGraphCenterX, mGraphCenterY, size/2, imagePaint);
//			
//			 
//			// canvas.drawCircle(circleCenter + borderWidth, circleCenter
//			// + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) - 4.0f,
//			// paint);
//		}
//
//	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	private void sortPortionArray() {
		Comparator<Portion> comparator = new Comparator<Portion>() {

			@Override
			public int compare(Portion lhs, Portion rhs) {
				// TODO Auto-generated method stub
				float result = lhs.getPercentage() - rhs.getPercentage();
				if (result == 0) {
					return 0;
				} else {
					return (result > 0) ? -1 : 1;
				}
			}
		};
		Collections.sort(mPortions, comparator);
	}

	private void setUpGraphDimension(Canvas canvas) {

		int canvasHeight = canvas.getHeight();
		int canvasWidth = canvas.getWidth();

		/* case1. graph goes to the center if text area is not needed */
		if (!isSubtitleOn) {
			mGraphWidth = (canvasHeight > canvasWidth) ? canvasWidth
					: canvasHeight;
		}

		/* case2. graph and text area share the width */
		else {
			mGraphWidth = (int) (canvasWidth * GRAPH_TO_TEXT_WIDTH_RATIO);
			if (mGraphWidth > canvasHeight) {
				mGraphWidth = canvasHeight;
			}
		}

	}

	private void setUpInnerRectFParameter() {
		/*
		 * 1. "inset" is used to distinguish "portion region" vs.
		 * "filter region"
		 */
		mInnerSize = mGraphWidth / INNER_TO_OUTER_RATIO;
		mInnerRectF.set(0, 0, mGraphWidth, mGraphWidth);
		mInnerRectF.inset(mInnerSize, mInnerSize);
	}

	private void setUpOuterRectFParameter() {
		/*
		 * 1. "inset" is used to distinguish "focused portion" vs.
		 * "normal portion"
		 */
		mOuterRectF.set(0, 0, mGraphWidth, mGraphWidth);
		mOuterRectF.inset(UNFOCUSED_RECT_SIZE, UNFOCUSED_RECT_SIZE);
	}

	private void setUpVoidRectFParameter() {
		/*
		 * 1. "inset" is used to distinguish "colored region" vs.
		 * "white region (void)"
		 */
		mVoidRectf.set(0, 0, mGraphWidth, mGraphWidth);
		mVoidRectf.inset(mInnerSize + DEFAULT_INNER_TO_VOID_GAP, mInnerSize
				+ DEFAULT_INNER_TO_VOID_GAP);
	}

	private void drawTextOnSide(Canvas canvas) {

		float xPos = mGraphWidth + mGraphToTextGap;
		float yPos = mGraphWidth - mPortions.size() * mTextHeightGap - 1;
		RectF rect = new RectF();

		if (isSubtitleOn) {
			for (int i = 0; i < mPortions.size(); i++) {
				Portion portion = mPortions.get(i);
				String text = portion.getText();

				/* 1. setting the circle drawing part */
				mStrokePaint.setColor(colors[i]);
				rect.set(xPos, yPos - mTextHeightGap, xPos + mTextHeightGap,
						yPos);
				canvas.drawArc(rect, 0, 360, true, mStrokePaint);

				/* 2. drawing the text part */
				canvas.drawText(text, xPos + mTextHeightGap + DEFAULT_GAP, yPos
						- mTextHeightGap / 3, mStrokePaint);

				/* 3. updating the y position */
				yPos += (mTextHeightGap + DEFAULT_GAP);
			}
		}
	}

	private static int DEFAULT_TEXT_TO_SUBTEXT_GAPY = 50;
	private static int DEFAULT_TEXT_TO_SUBTEXT_GAPX = 20;

	private void drawClickedPercentage(Canvas canvas, int index) {
		/* 1. setting position */
		float yPos = PERCENTAGE_TEXT_GAP;
		float xPos = mGraphWidth + mGraphToTextGap;

		/* 2. setting color & fonts */
		mPercentageStroke.setColor(colors[index]);
		mPercentageStroke.setTextSize(DEFAULT_PERCENTAGE_TEXT_SIZE);
		mPercentageStroke.setFakeBoldText(true);

		String text = (int) (mPortions.get(index).getPercentage() * 100) + "%";
		String subText = "Selected " + mPortions.get(index).getText();
		if (isSubtitleOutside) {
			canvas.drawText(text, xPos, yPos, mPercentageStroke);

			mPercentageStroke.setFakeBoldText(false);
			mPercentageStroke.setTextSize(DEFAULT_PERCENTAGE_SUBTEXT_SIZE);
			canvas.drawText(subText, xPos + DEFAULT_TEXT_TO_SUBTEXT_GAPX, yPos
					+ DEFAULT_TEXT_TO_SUBTEXT_GAPY, mPercentageStroke);
		} else {
			xPos = (int) (mGraphCenterX - DEFAULT_PERCENTAGE_TEXT_SIZE);
			yPos = (int) (mGraphCenterY + DEFAULT_PERCENTAGE_TEXT_SIZE / 3);
			canvas.drawText(text, xPos, yPos, mPercentageStroke);
		}
	}

	/* 4. Measuring the height & width of the canvas */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// The parent has determined an exact size for the child.
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			// The child can be as large as it wants up to the specified size.
			result = specSize;
		} else {
			// The parent has not imposed any constraint on the child.
			result = canvasSize;
		}

		return result;
	}

	private int measureHeight(int measureSpecHeight) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			// The child can be as large as it wants up to the specified size.
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = canvasSize;
		}

		return (result + 2);
	}

	/* 5. Set graph portions */
	public void setGraphPortions(ArrayList<Portion> array) {

		/* 1. check if the percentage adds up to 100% */
		boolean isHundred = isPercentageTotalValid(array);
		if (!isHundred) {
			addAdditionalPortion(array);
		}

		this.mPortions = array;
		sortPortionArray();
		mPortions.get(0).setFocused(true);
	}

	private void addAdditionalPortion(ArrayList<Portion> array) {
		// TODO Auto-generated method stub
		float totalPercentage = 0.0f;
		for (Portion portion : array) {
			totalPercentage += portion.getPercentage();
		}

		/* 1. Calculating the remaining percentage to add */
		float remainingPercentage = 1.0f - totalPercentage;
		array.add(new Portion(remainingPercentage, DEFAULT_PORTION_COLOR,
				DEFAULT_PORTION_TEXT));

	}

	private boolean isPercentageTotalValid(ArrayList<Portion> array) {
		// TODO Auto-generated method stub

		/* 1. adding all the percentages from the portions given */
		float totalPercentage = 0.0f;
		for (Portion portion : array) {
			totalPercentage += portion.getPercentage();
		}

		/* 2. checking if the total percentage is close to 100% */
		return ((1 - totalPercentage) < PERCENTAGE_BOUNDARY) ? true : false;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float xPos = event.getX();
		float yPos = event.getY();

		if (isWithinTheGraph(xPos, yPos)) {
			PolarCoordinate polarCoordinate = new PolarCoordinate(xPos, yPos,
					mGraphCenterX, mGraphCenterY);
			int index = determinePortionClicked(polarCoordinate.getAngle());
			setFocusedPortion(index);
			invalidate();
			Log.i(LOG_GRAPH, "index:  + " + index);

		}

		return super.onTouchEvent(event);
	}

	private void setFocusedPortion(int index) {
		for (int i = 0; i < mPortions.size(); i++) {
			if (i == index) {
				mPortions.get(i).setFocused(true);
			} else {
				mPortions.get(i).setFocused(false);
			}
		}

	}

	private int determinePortionClicked(float angle) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mPortions.size(); i++) {
			Portion portion = mPortions.get(i);
			if (angle >= portion.getStartAngle()
					&& angle <= portion.getEngAngle()) {
				return i;
			}
		}
		return -1;
	}

	private boolean isWithinTheGraph(float xPos, float yPos) {
		if (xPos > mGraphWidth) {
			Log.i(LOG_GRAPH, "something is wrong");
			return false;
		}

		float xDiff = mGraphCenterX - xPos;
		float yDiff = mGraphCenterY - yPos;
		float radius = (float) Math.sqrt(Math.pow(xDiff, 2)
				+ Math.pow(yDiff, 2));
		Log.i(LOG_GRAPH, radius + "," + mGraphWidth);
		if (radius > mGraphWidth / 2) {
			return false;
		}

		return true;
	}

}
