package io.mosaicnetworks.myfirstbabbleapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import io.mosaicnetworks.babble.node.BabbleTx;

public final class GameTx implements BabbleTx {

    private final static Gson gson = new GsonBuilder().create();

    @SerializedName("num")
    public final int num;

    public GameTx(int num) {
        this.num = num;
    }


    @Override
    public byte[] toBytes() {
        return gson.toJson(this).getBytes();
    }

    public int getNum() {
        return num;
    }

    public static GameTx fromJson(String txJson) {
        return gson.fromJson(txJson, GameTx.class);
    }

}
