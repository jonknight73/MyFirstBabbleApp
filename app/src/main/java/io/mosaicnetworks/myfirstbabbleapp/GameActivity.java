package io.mosaicnetworks.myfirstbabbleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.mosaicnetworks.babble.node.BabbleNode;
import io.mosaicnetworks.babble.service.BabbleServiceBinderActivity;
import io.mosaicnetworks.babble.service.ServiceObserver;
import io.mosaicnetworks.babble.utils.DialogUtils;

public class GameActivity extends BabbleServiceBinderActivity implements ServiceObserver {


    private String mMoniker;
    private Integer mMessageIndex = 0;
    private boolean mArchiveMode;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mTextView = findViewById(R.id.textView);
        mTextView.setText("My Randoms:\n\n");


        Intent intent = getIntent();
        mMoniker = intent.getStringExtra("MONIKER");
        mArchiveMode = intent.getBooleanExtra("ARCHIVE_MODE", false);
        String group = intent.getStringExtra("GROUP");

        setTitle(group);
        doBindService();
    }


    @Override
    protected void onServiceConnected() {
        mBoundService.registerObserver(this);

        //we need to call stateUpdate() to ensure messages are pulled on configuration changes
        stateUpdated();
    }

    @Override
    protected void onServiceDisconnected() {
        //do nothing
    }

    @Override
    public void stateUpdated() {
        final List<GameTx> newTxs = ((GameState) mBoundService.getAppState()).getTxFromIndex(mMessageIndex);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (GameTx tx : newTxs ) {
                    displayNumber(tx);
                }
            }
        });

        mMessageIndex = mMessageIndex + newTxs.size();
    }

    @Override
    public void onNodeStateChanged(BabbleNode.State state) {
        if (state== BabbleNode.State.Suspended) {
            DialogUtils.displayOkAlertDialogText(this, R.string.app_name, "Node suspended. Too few participants");
        }
    }

    private void displayNumber(GameTx gameTx) {
        mTextView.append(Integer.toString(gameTx.getNum()));
    }

    public void btSendClick(View view) {
        mBoundService.submitTx(new GameTx((int) (Math.random() * 10)));
    }

    @Override
    public void onBackPressed() {
        mBoundService.leave(null);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        mBoundService.removeObserver(this);
        super.onDestroy();
    }


}
