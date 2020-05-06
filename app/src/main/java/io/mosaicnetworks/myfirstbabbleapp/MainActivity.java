package io.mosaicnetworks.myfirstbabbleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import io.mosaicnetworks.babble.configure.BaseConfigActivity;
import io.mosaicnetworks.babble.service.BabbleService;

public class MainActivity extends BaseConfigActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BabbleService.setAppState(new GameState());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onJoined(String moniker, String group) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("MONIKER", moniker);
        intent.putExtra("ARCHIVE_MODE", false);
        intent.putExtra("GROUP", group);
        startActivity(intent);
    }

    @Override
    public void onStartedNew(String moniker, String group) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("MONIKER", moniker);
        intent.putExtra("ARCHIVE_MODE", false);
        intent.putExtra("GROUP", group);
        startActivity(intent);
    }

    public void onArchiveLoaded(String moniker, String group) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("MONIKER", moniker);
        intent.putExtra("ARCHIVE_MODE", true);
        intent.putExtra("GROUP", group);
        startActivity(intent);
    }

}
