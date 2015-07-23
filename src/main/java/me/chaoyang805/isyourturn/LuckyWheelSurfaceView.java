package me.chaoyang805.isyourturn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private RectF mRange;
    private float mDiameter;
    private float mCenter;
    private int mPadding;
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, getResources().getDisplayMetrics());

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

        mImgBitmaps = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //drawsomething
            }
        }catch (Exception e){

        }finally {
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
