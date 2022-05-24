package co.yft.posprinter;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.content.Intent;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;

import android.widget.Toast;

import java.io.IOException;

public class PrinterActivity extends Activity {
    PrinterManager mPrinterManager;

    // Printer status
    private final static int PRNSTS_OK = 0; // OK
    private final static int PRNSTS_OUT_OF_PAPER = -1; // Out of paper
    private final static int PRNSTS_OVER_HEAT = -2; // Over heat
    private final static int PRNSTS_UNDER_VOLTAGE = -3; // under voltage
    private final static int PRNSTS_BUSY = -4; // Device is busy
    private final static int PRNSTS_ERR = -256; // Common error
    private final static int PRNSTS_ERR_DRIVER = -257; // Printer Driver error

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CustomThread().start(); // Start printer worker
        Log.e("hi", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPrinterManager != null) {
            mPrinterManager.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_CODE: // After selecting a picture from the picture selector, continue printing the
                                     // picture
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    // Return by URI
                    Bitmap bitmap = null;
                    if (uri != null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Some devices may be stored directly in bundles
                        Bundle bundleExtras = data.getExtras();
                        if (bundleExtras != null) {
                            bitmap = bundleExtras.getParcelable("data");
                        }
                    }
                    if (bitmap != null) {
                        bitmap = Bitmap.createScaledBitmap(bitmap,
                                300, 300 * bitmap.getHeight() / bitmap.getWidth(), true); // Zoom picture size
                        Message msg = mPrintHandler.obtainMessage(PRINT_BITMAP);
                        msg.obj = bitmap;
                        msg.sendToTarget();
                    }
                }
                break;
        }
    }

    private final int PRINT_TEXT = 0; // Printed text
    private final int PRINT_BITMAP = 1; // print pictures
    private final int PRINT_BARCOD = 2; // Print bar code
    private final int PRINT_FORWARD = 3; // Forward (paper feed)

    private Handler mPrintHandler;

    class CustomThread extends Thread {
        @Override
        public void run() {

            // To create a message loop
            Looper.prepare(); // 1.Initialize looper
            mPrintHandler = new Handler() { // 2.Bind handler to looper object of customthread instance
                public void handleMessage(Message msg) { // 3.Define how messages are processed
                    Bundle extras = getIntent().getExtras();
                    String message;
                    String action = "0";

                    if (extras == null) {
                        message = null;
                    } else {
                        message = extras.getString("message");
                        action = extras.getString("action");
                    }

                    switch (Integer.valueOf(action)) {
                        case PRINT_TEXT:
                        case PRINT_BITMAP:
                        case PRINT_BARCOD:
                            doPrint(getPrinterManager(), Integer.valueOf(action), message); // Print
                            break;
                        case PRINT_FORWARD:
                            getPrinterManager().paperFeed(200);
                            updatePrintStatus(100);
                            break;
                    }
                }
            };
            Looper.loop(); // 4.Start message loop
        }
    }

    // Instantiate printermanager
    private PrinterManager getPrinterManager() {
        if (mPrinterManager == null) {
            mPrinterManager = new PrinterManager();
            mPrinterManager.open();
        }
        return mPrinterManager;
    }

    private static final int PHOTO_REQUEST_CODE = 200;

    // Update printer status, toast reminder in case of exception
    private void updatePrintStatus(final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private void doPrint(PrinterManager printerManager, int type, Object content) {
        Log.i("what is this", String.valueOf(content));
        int ret = printerManager.getStatus(); // Get printer status
        if (ret == PRNSTS_OK) {
            printerManager.setupPage(384, -1); // Set paper size

            switch (type) {
                case PRINT_TEXT:
                    int fontSize = 24;
                    String fontName = "arial";
                    int height = 0;
                    String[] texts = ((String) content).split("\n"); // Split print content into multiple lines
                    for (String text : texts) {
                        height += printerManager.drawText(text, 0, height, fontName, fontSize, false, false, 0); // Printed
                                                                                                                 // text
                    }
                    height = 0;
                    break;

                case PRINT_BARCOD:
                    String text = (String) content;
                    printerManager.drawBarcode(text, 50, 10, 58, 8, 120, 0); // Print bar code
                    break;

                case PRINT_BITMAP:
                    Bitmap bitmap = (Bitmap) content;
                    if (bitmap != null) {
                        printerManager.drawBitmap(bitmap, 30, 0); // print pictures
                    } else {
                        Toast.makeText(this, "Picture is null", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            ret = printerManager.printPage(0); // Execution printing
            printerManager.paperFeed(600); // paper feed
        }
        updatePrintStatus(ret);
    }
}
