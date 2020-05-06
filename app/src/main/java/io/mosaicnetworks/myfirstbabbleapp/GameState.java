package io.mosaicnetworks.myfirstbabbleapp;

import android.annotation.SuppressLint;

import com.google.gson.JsonSyntaxException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mosaicnetworks.babble.node.BabbleState;
import io.mosaicnetworks.babble.node.Block;
import io.mosaicnetworks.babble.node.InternalTransactionReceipt;

public class GameState implements BabbleState {

    private Integer mNextIndex = 0;
    private byte[] mStateHash = new byte[0];
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, GameTx> mState = new HashMap<>();


    @Override
    public Block processBlock(Block block) {
        for (byte[] rawTx:block.body.transactions) {
            String tx = new String(rawTx, StandardCharsets.UTF_8);
            GameTx msg;
            try {
                msg = GameTx.fromJson(tx);
            } catch (JsonSyntaxException ex) {
                //skip any malformed transactions
                continue;
            }

            mState.put(mNextIndex, msg);
            mNextIndex++;
        }

        // Accept all internal transactions, and populate receipts.
        InternalTransactionReceipt[] itr = new InternalTransactionReceipt[block.body.internalTransactions.length];
        for(int i=0; i< block.body.internalTransactions.length; i++){
            itr[i] = block.body.internalTransactions[i].asAccepted();
        }

        // Set block stateHash and receipts
        block.body.stateHash = mStateHash;
        block.body.internalTransactionReceipts = itr;

        return block;
    }

    @Override
    public void reset() {
        mState.clear();
        mNextIndex = 0;
    }


    public List<GameTx> getTxFromIndex(Integer index) {

        if (index<0) {
            throw new IllegalArgumentException("Index cannot be less than 0");
        }

        if (index >= mNextIndex) {
            return new ArrayList<>();
        }

        Integer numTx = mNextIndex - index;

        List<GameTx> gameTxs = new ArrayList<>(numTx);

        for (int i = 0; i < numTx; i++) {
            gameTxs.add(mState.get(index + i));
        }

        return gameTxs;
    }


}
