package com.smartmart.scanner;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.smartmart.scanner.camera.Overlay;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class TagBarcode implements MultiProcessor.Factory<Barcode> {
    private Overlay<ColorSetting> mOverlay;
    private ScannerActivity.BarcodeGraphicTrackerListener listener;

    TagBarcode(Overlay<ColorSetting> barcodeOverlay, ScannerActivity.BarcodeGraphicTrackerListener listener) {
        mOverlay = barcodeOverlay;
        this.listener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        ColorSetting graphic = new ColorSetting(mOverlay);
        return new ScannerActivity(mOverlay, graphic, listener);
    }

}

