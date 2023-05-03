package com.example.bequiet.model;

public enum NoiseType {
    SILENT((byte) 0),
    VIBRATE((byte) 1),
    NOISE((byte) 2);

    private byte state;

    NoiseType(byte i){
        state = i;
    }
}
