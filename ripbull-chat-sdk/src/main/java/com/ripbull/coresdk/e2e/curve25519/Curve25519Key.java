package com.ripbull.coresdk.e2e.curve25519;

import java.security.Key;

import static com.ripbull.coresdk.e2e.curve25519.Curve25519.ALGORITHM;

class Curve25519Key implements Key {
    private byte[] mKey;

    Curve25519Key(byte[] key) {
        mKey = key;
    }

    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return mKey;
    }
}
