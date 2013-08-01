package com.mattmarchany.sketch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
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
            case R.id.action_about:
                // Launch the About section
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
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
