package com.example.andrew.martialmayhem;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onStartGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    public void onInstructions(View view) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext());

        builder.setTitle(R.string.instructionsTitle);
        builder.setMessage(R.string.instructionsText);
        builder.setPositiveButton(android.R.string.ok, null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
