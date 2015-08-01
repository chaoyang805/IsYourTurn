package me.chaoyang805.isyourturn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * LuckyWheelSurfaceView
 */
public class LuckyWheelSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread t;
    private boolean isRunning;

    private Bitmap[] mImgBitmaps;
    private Bitmap backGround = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    private int[] mImgs = {R.drawable.danfan, R.drawable.ipad, R.drawable.iphone, R.drawable.f015, R.drawable.meizi, R.drawable.f040};
    private String[] mStrs = {"奖品一", "奖品二", "奖品三", "谢谢惠顾", "奖品四", "谢谢惠顾"};
    private int[] mColors = {0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01};

    private Paint mTextPaint,mArcPaint;

    private int mItemCount = 6;
    private int mSpeed = 0;
    private RectF mRange;
    private int mDiameter;
    private float mCenter;
    private int mPadding;
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, getResources().getDisplayMetrics());

    private float mStartAngle = 0;
    private float mSweepAngle = 360f / mItemCount;

    public LuckyWheelSurfaceView(Context context) {
        this(context, null);
    }

    public LuckyWheelSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LuckyWheelSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        mDiameter = width - mPadding * 2;
        mCenter = width / 2;
        setMeasuredDimension(width, width);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        t = new Thread(this);
        t.start();

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xFFFFFFFF);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mRange = new RectF(mPadding, mPadding, mPadding + mDiameter, mPadding + mDiameter);
        mImgBitmaps = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgBitmaps[i] = BitmapFactory.decodeResource(getResources(),mImgs[i]);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //drawsomething
                //draw background
                drawBackground();
                float tmpAngle = mStartAngle;
                for (int i = 0; i < mItemCount; i++) {
                    mArcPaint.setColor(mColors[i]);
                    mCanvas.drawArc(mRange, tmpAngle, mSweepAngle, true, mArcPaint);
                    drawTextOnPath(tmpAngle, mStrs[i]);
                    drawIcon(tmpAngle,mImgBitmaps[i]);
                    tmpAngle += mSweepAngle;
                }
            mStartAngle += mSpeed;
            }
        }catch (Exception e){

        }finally {
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawTextOnPath(float tmpAngle, String str) {
        Path path = new Path();
        path.addArc(mRange, tmpAngle, mSweepAngle);
        float textWidth = mTextPaint.measureText(str);
        float hOffset = (float) ((mDiameter * Math.PI / mItemCount - textWidth) / 2);
        float vOffset = mDiameter / 2 / 6;
        mCanvas.drawTextOnPath(str, path, hOffset, vOffset, mTextPaint);
    }

    private void drawIcon(float tmpAngle, Bitmap bitmap) {
        int imgWidth = mDiameter / 8;
        double angle = (tmpAngle + 360 / mItemCount / 2) * Math.PI / 180;
        int x = (int) (mCenter + mDiameter / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mDiameter / 2 / 2 * Math.sin(angle));
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2,
                x + imgWidth / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(bitmap, null, rect, null);
    }

    private void drawBackground() {
        mCanvas.drawColor(0xFFFFFFFF);
        mCanvas.drawBitmap(backGround, null, new RectF(mPadding / 2, mPadding / 2,
                getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), null);
    }

    public void start(int speed) {
        mSpeed = speed;
    }

    public void stop() {
        mSpeed = 0;
    }
}
