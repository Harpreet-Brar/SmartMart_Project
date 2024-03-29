package com.smartmart.scanner.camera;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import androidx.annotation.RequiresPermission;

import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;

import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class Preview extends ViewGroup {
    private static final String TAG = "CameraSourcePreview";

    private Context mContext;
    private SurfaceView mSurfaceView;
    private boolean mStartRequested;
    private boolean mSurfaceAvailable;
    private Source mSource;

    private Overlay mOverlay;

    public Preview(Context context, AttributeSet attrs) {
        super (context, attrs);
        mContext = context;
        mStartRequested = false;
        mSurfaceAvailable = false;

        mSurfaceView = new SurfaceView (context);
        mSurfaceView.getHolder ().addCallback (new SurfaceCallback ());
        addView (mSurfaceView);
    }

    @RequiresPermission(CAMERA)
    public void start(Source source) throws IOException, SecurityException {
        if (source == null) {
            stop ();
        }

        mSource = source;

        if (mSource != null) {
            mStartRequested = true;
            startIfReady ();
        }
    }

    @RequiresPermission(CAMERA)
    public void start(Source source, Overlay overlay) throws IOException, SecurityException {
        mOverlay = overlay;
        start (source);
    }

    public void stop() {
        if (mSource != null) {
            mSource.stop ();
        }
    }

    public void release() {
        if (mSource != null) {
            mSource.release ();
            mSource = null;
        }
    }

    public void setFlash(boolean flag) {

    }

    @RequiresPermission(CAMERA)
    private void startIfReady() throws IOException, SecurityException {
        if (mStartRequested && mSurfaceAvailable) {
            mSource.start (mSurfaceView.getHolder ());
            if (mOverlay != null) {
                Size size = mSource.getPreviewSize ();
                int min = Math.min (size.getWidth (), size.getHeight ());
                int max = Math.max (size.getWidth (), size.getHeight ());
                if (isPortraitMode ()) {
                    // Swap width and height sizes when in portrait, since it will be rotated by
                    // 90 degrees
                    mOverlay.setCameraInfo (min, max, mSource.getCameraFacing ());
                } else {
                    mOverlay.setCameraInfo (max, min, mSource.getCameraFacing ());
                }
                mOverlay.clear ();
            }
            mStartRequested = false;
        }
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surface) {
            mSurfaceAvailable = true;
            try {
                startIfReady ();
            } catch (SecurityException se) {
                Log.e (TAG, "Do not have permission to start the camera", se);
            } catch (IOException e) {
                Log.e (TAG, "Could not start camera source.", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surface) {
            mSurfaceAvailable = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
    }

    //    Changes into this method
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int previewWidth = 320;
        int previewHeight = 240;
        if (mSource != null) {
            Size size = mSource.getPreviewSize ();
            if (size != null) {
                previewWidth = size.getWidth ();
                previewHeight = size.getHeight ();
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode ()) {
            int tmp = previewWidth;
            previewWidth = previewHeight;
            previewHeight = tmp;
        }

        final int viewWidth = right - left;
        final int viewHeight = bottom - top;

        int childWidth;
        int childHeight;
        int childXOffset = 0;
        int childYOffset = 0;
        float widthRatio = (float) viewWidth / (float) previewWidth;
        float heightRatio = (float) viewHeight / (float) previewHeight;

        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
        // it is usually necessary to slightly oversize the child and to crop off portions along one
        // of the dimensions.  We scale up based on the dimension requiring the most correction, and
        // compute a crop offset for the other dimension.
        if (widthRatio > heightRatio) {
            childWidth = viewWidth;
            childHeight = (int) ((float) previewHeight * widthRatio);
            childYOffset = (childHeight - viewHeight) / 2;
        } else {
            childWidth = (int) ((float) previewWidth * heightRatio);
            childHeight = viewHeight;
            childXOffset = (childWidth - viewWidth) / 2;
        }

        for (int i = 0; i < getChildCount (); ++i) {
            // One dimension will be cropped.  We shift child over or up by this offset and adjust
            // the size to maintain the proper aspect ratio.
            getChildAt (i).layout (
                    -1 * childXOffset, -1 * childYOffset,
                    childWidth - childXOffset, childHeight - childYOffset);
        }

        try {
            if (checkSelfPermission (CAMERA) != PackageManager.PERMISSION_GRANTED) {

                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            startIfReady ();
        } catch (IOException e) {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    private int checkSelfPermission(String camera) {
        return 0;
    }

    private boolean isPortraitMode() {
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }

        Log.d(TAG, "isPortraitMode returning false by default");
        return false;
    }
}