package com.mattmarchany.sketch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    Toast clearToast, colorToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Intents
        aboutIntent = new Intent(this, AboutActivity.class);

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
            case R.id.action_undo:
                // Undo
                drawView.undo();
                return true;
            case R.id.action_redo:
                // Redo
                drawView.redo();
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
        String savePath = saveFolder + "/sketch-" + reportDate + ".png";
        String[] paths = { savePath };
        String[] mediaType = {"image/png"};
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
            b.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, R.string.toast_save_success, 5000).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.toast_save_error, 5000).show();
        }

        // Refresh gallery
        MediaScannerConnection.scanFile(this, paths, mediaType, null);

        // Cleanup
        sketch.setDrawingCacheEnabled(false);
    }

    // Dialogs
    public void clearDialog() {
        clearToast = Toast.makeText(this, getString(R.string.toast_clear), Toast.LENGTH_SHORT);

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
        colorToast = Toast.makeText(this, getString(R.string.toast_color), Toast.LENGTH_SHORT);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_colors);
        dialog.setTitle(R.string.dialog_color_title);

        final ImageButton blackButton = (ImageButton) dialog.findViewById(R.id.dialog_color_black);
        if (drawView.getColor() != Color.BLACK) {
            blackButton.setImageResource(R.drawable.ic_plain_circles_black);
        }
        else {
            blackButton.setImageResource(R.drawable.ic_checked_circles_black);
        }
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(Color.BLACK);
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton blueButton = (ImageButton) dialog.findViewById(R.id.dialog_color_blue);
        if (drawView.getColor() != getResources().getColor(R.color.blue)) {
            blueButton.setImageResource(R.drawable.ic_plain_circles_blue);
        }
        else {
            blueButton.setImageResource(R.drawable.ic_checked_circles_blue);
        }
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.blue));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton darkGrayButton = (ImageButton) dialog.findViewById(R.id.dialog_color_darkgray);
        if (drawView.getColor() != getResources().getColor(R.color.dark_gray)) {
            darkGrayButton.setImageResource(R.drawable.ic_plain_circles_darkgray);
        }
        else {
            darkGrayButton.setImageResource(R.drawable.ic_checked_circles_darkgray);
        }
        darkGrayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.dark_gray));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton darkGreenButton = (ImageButton) dialog.findViewById(R.id.dialog_color_darkgreen);
        if (drawView.getColor() != getResources().getColor(R.color.dark_green)) {
            darkGreenButton.setImageResource(R.drawable.ic_plain_circles_darkgreen);
        }
        else {
            darkGreenButton.setImageResource(R.drawable.ic_checked_circles_darkgreen);
        }
        darkGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.dark_green));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton grayButton = (ImageButton) dialog.findViewById(R.id.dialog_color_gray);
        if (drawView.getColor() != getResources().getColor(R.color.gray)) {
            grayButton.setImageResource(R.drawable.ic_plain_circles_gray);
        }
        else {
            grayButton.setImageResource(R.drawable.ic_checked_circles_gray);
        }
        grayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.gray));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton greenButton = (ImageButton) dialog.findViewById(R.id.dialog_color_green);
        if (drawView.getColor() != getResources().getColor(R.color.green)) {
            greenButton.setImageResource(R.drawable.ic_plain_circles_green);
        }
        else {
            greenButton.setImageResource(R.drawable.ic_checked_circles_green);
        }
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.green));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton lightBlueButton = (ImageButton) dialog.findViewById(R.id.dialog_color_lightblue);
        if (drawView.getColor() != getResources().getColor(R.color.light_blue)) {
            lightBlueButton.setImageResource(R.drawable.ic_plain_circles_lightblue);
        }
        else {
            lightBlueButton.setImageResource(R.drawable.ic_checked_circles_lightblue);
        }
        lightBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.light_blue));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton orangeButton = (ImageButton) dialog.findViewById(R.id.dialog_color_orange);
        if (drawView.getColor() != getResources().getColor(R.color.orange)) {
            orangeButton.setImageResource(R.drawable.ic_plain_circles_orange);
        }
        else {
            orangeButton.setImageResource(R.drawable.ic_checked_circles_orange);
        }
        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.orange));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton pinkButton = (ImageButton) dialog.findViewById(R.id.dialog_color_pink);
        if (drawView.getColor() != getResources().getColor(R.color.pink)) {
            pinkButton.setImageResource(R.drawable.ic_plain_circles_pink);
        }
        else {
            pinkButton.setImageResource(R.drawable.ic_checked_circles_pink);
        }
        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.pink));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton purpleButton = (ImageButton) dialog.findViewById(R.id.dialog_color_purple);
        if (drawView.getColor() != getResources().getColor(R.color.purple)) {
            purpleButton.setImageResource(R.drawable.ic_plain_circles_purple);
        }
        else {
            purpleButton.setImageResource(R.drawable.ic_checked_circles_purple);
        }
        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.purple));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton redButton = (ImageButton) dialog.findViewById(R.id.dialog_color_red);
        if (drawView.getColor() != getResources().getColor(R.color.red)) {
            redButton.setImageResource(R.drawable.ic_plain_circles_red);
        }
        else {
            redButton.setImageResource(R.drawable.ic_checked_circles_red);
        }
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.red));
                dialog.dismiss();
                colorToast.show();
            }
        });

        ImageButton yellowButton = (ImageButton) dialog.findViewById(R.id.dialog_color_yellow);
        if (drawView.getColor() != getResources().getColor(R.color.yellow)) {
            yellowButton.setImageResource(R.drawable.ic_plain_circles_yellow);
        }
        else {
            yellowButton.setImageResource(R.drawable.ic_checked_circles_yellow);
        }
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setColor(getResources().getColor(R.color.yellow));
                dialog.dismiss();
                colorToast.show();
            }
        });

        dialog.show();
    }
}
