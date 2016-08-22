package com.seonil.scrollingimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.abs;


/**
 * Created by Seonil on 2016. 7. 6..
 */
public class ScrollingImageView extends View {
    private static final int SLIDE_LEFT_TO_RIGHT = 0;
    private static final int SLIDE_RIGHT_TO_LEFT = 1;
    private static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_DURATION = 3000;
    private static final int DEFAULT_FRAME = 20;
    private static final int STOP_POSITION = 10;

    private Bitmap bitmap;
    private float speed;
    private Rect clipBounds = new Rect();
    private float offset = 0;
    private int slideType = 0;
    private int slideStyle = 0;
    private int startPositionX;
    private int duration;

    private AtomicBoolean isStarted;

    private ScrollingImageAnimationListener animationListener;

    public ScrollingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollingImageView, 0, 0);
        int initialState = 0;
        isStarted = new AtomicBoolean(false);

        try {
            initialState = typedArray.getInt(R.styleable.ScrollingImageView_initialState, 0);
            slideType = typedArray.getInt(R.styleable.ScrollingImageView_slideType, SLIDE_LEFT_TO_RIGHT);

            int resId = typedArray.getResourceId(R.styleable.ScrollingImageView_src, 0);

            bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);

            speed = typedArray.getFloat(R.styleable.ScrollingImageView_speed, -1f);

            duration = typedArray.getInt(R.styleable.ScrollingImageView_duration, DEFAULT_DURATION);

            if (speed == -1f) {
                slideStyle = 0;
            } else {
                slideStyle = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }

        if (initialState == 0) {
            start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null || bitmap == null) {
            return;
        }
        canvas.getClipBounds(clipBounds);


        if (speed == -1f) {
            speed = (float) (bitmap.getWidth() - clipBounds.width()) / (float) (duration / DEFAULT_FRAME);
        }

        int width = bitmap.getWidth();

        if (slideType == SLIDE_LEFT_TO_RIGHT) {
            startPositionX = 0;
        } else {
            startPositionX = width - clipBounds.width();
        }

        if (width - Math.abs(offset) - clipBounds.width() < STOP_POSITION) {
            stop();
        }

        canvas.drawBitmap(bitmap, getBitmapLeft(startPositionX, offset), 0, null);


        if (isStarted.get() && speed != 0) {
            if (slideType == SLIDE_LEFT_TO_RIGHT) {
                offset -= abs(speed);
            } else {
                offset += abs(speed);
            }

            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                invalidate();
            } else {
                postInvalidateOnAnimation();
            }
        }
    }

    private float getBitmapLeft(float layerWidth, float left) {
        if (slideType == SLIDE_LEFT_TO_RIGHT) {
            return left;
        } else {
            return -(layerWidth - left);
        }
    }

    /**
     * Start the animation
     */
    public void start() {
        if (!isStarted.get()) {
            isStarted.set(true);
            if (animationListener != null) {
                animationListener.onAnimationStart();
            }
            reDrawImageView();
        }
    }

    public void clear() {
        stop();
        bitmap.recycle();
        bitmap = null;
    }

    public boolean isRunning() {
        return isStarted.get();
    }

    /**
     * Stop the animation
     */
    public void stop() {
        if (isStarted.get()) {
            isStarted.set(false);
            if (animationListener != null) {
                animationListener.onAnimationEnd();
            }
            reDrawImageView();
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        if (isStarted.get()) {
            reDrawImageView();
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
        if (isStarted.get()) {
            reDrawImageView();
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        reset();
        if (isStarted.get()) {
            reDrawImageView();
        }
    }


    public void setImageURI(Uri uri) {

        if (uri != null) {
            try {
                File imgFile = new File(uri.getPath());

                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap == null) {
                    stop();
                } else {

                    reset();

                    if (isStarted.get()) {
                        reDrawImageView();
                    }
                }
            }
        } else {
            stop();
        }
    }

    private void reDrawImageView() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            invalidate();
        } else {
            postInvalidateOnAnimation();
        }
    }

    private void reset() {
        if (slideStyle == 0) {
            speed = -1f;
        }

        offset = 0;
    }

    public void setAnimationListener(ScrollingImageAnimationListener animationListener) {
        this.animationListener = animationListener;
    }
}
