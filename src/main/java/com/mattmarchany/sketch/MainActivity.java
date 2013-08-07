package com.mattmarchany.sketch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    DrawView drawView;
    Intent aboutIntent;
    Toast clearToast, blackToast, blueToast, greenToast, redToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Intents
        aboutIntent = new Intent(this, AboutActivity.class);

        // Toasts
        clearToast = Toast.makeText(this, getString(R.string.toast_clear), Toast.LENGTH_SHORT);
        blackToast = Toast.makeText(this, getString(R.string.toast_black), Toast.LENGTH_SHORT);
        blueToast = Toast.makeText(this, getString(R.string.toast_blue), Toast.LENGTH_SHORT);
        greenToast = Toast.makeText(this, getString(R.string.toast_green), Toast.LENGTH_SHORT);
        redToast = Toast.makeText(this, getString(R.string.toast_red), Toast.LENGTH_SHORT);

        // Launch DrawView
        drawView = new DrawView(this);
        setContentView(drawView);
        drawView.requestFocus();
    }

    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                // Clear the screen
                clearDialog();
                return true;
            case R.id.action_toggle_colors:
                // Toggle the colors menu
                colorDialog();
                return true;
            case R.id.action_save:
                // Save the canvas to gallery
                saveToGallery();
                return true;
            case R.id.action_about:
                // Launch the About section
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }

    // Save to Gallery
    public void saveToGallery() {
        View sketch = drawView;
        sketch.setDrawingCacheEnabled(true);
        sketch.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Create timestamp for saved sketch
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        Bitmap b = sketch.getDrawingCache();
        String saveFolder = Environment.getExternalStorageDirectory() + "/Pictures/Sketch";
        String savePath = saveFolder + "/sketch-" + reportDate + ".jpg";
        String[] paths = { savePath };
        String[] mediaType = {"image/jpeg"};
        File saveDir = new File(saveFolder);

        Log.d(TAG, "savePath: " + saveFolder);

        if (!saveDir.exists()) {
            saveDir.mkdirs();
            Log.d(TAG, "Directory created in gallery.");
        }

        File savedFile = new File(savePath);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(savedFile);
            b.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(getApplicationContext(), "Image saved to gallery.", 5000).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error saving image.", 5000).show();
        }

        // Refresh gallery
        MediaScannerConnection.scanFile(this, paths, mediaType, null);

        // Cleanup
        sketch.setDrawingCacheEnabled(false);
    }

    // Dialogs
    public void clearDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_clear_title))
                .setMessage(getString(R.string.dialog_clear_message))
                .setPositiveButton(getString(R.string.dialog_yes),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        drawView.clearScreen();
                        clearToast.show();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_no),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Don't do anything
                    }
                })
                .show();
    }

    public void colorDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_color_title))
                .setItems(R.array.color_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 1) {
                            drawView.setColor(Color.BLUE);
                            blueToast.show();
                        }
                        else if (i == 2) {
                            drawView.setColor(Color.GREEN);
                            greenToast.show();
                        }
                        else if (i == 3) {
                            drawView.setColor(Color.RED);
                            redToast.show();
                        }
                        else {
                            drawView.setColor(Color.BLACK);
                            blackToast.show();
                        }
                    }
                })
                .show();
    }
}
