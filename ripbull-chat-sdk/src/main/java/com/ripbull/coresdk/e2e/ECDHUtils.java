package com.ripbull.coresdk.e2e;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import com.ripbull.coresdk.e2e.curve25519.Curve25519KeyAgreement;
import com.ripbull.coresdk.e2e.curve25519.Curve25519KeyPairGenerator;
import com.ripbull.coresdk.e2e.curve25519.Curve25519PrivateKey;
import com.ripbull.coresdk.e2e.curve25519.Curve25519PublicKey;
import com.ripbull.coresdk.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.ripbull.coresdk.e2e.curve25519.Curve25519.ALGORITHM;

// https://github.com/FredericJacobs/25519 for ios
// https://github.com/balena/ecdh-curve25519-java for android
// https://ianix.com/pub/curve25519-deployment.html cross platform link
// https://stackoverflow.com/a/16854800 for encryption
public class ECDHUtils {

  private static final String TAG = "ECDHUtils";
  private static final String ALGO = "AES/CBC/PKCS7Padding";
  private static final String MESSAGE_DIGEST_ALGO = "SHA-256";

  private static byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
  private static final IvParameterSpec ivspec = new IvParameterSpec(iv);

  public static String[] generateKeyPair() {
    String[] keys = new String[2];
    final String publicKey;
    final String privateKey;
    KeyPair keyPair = ECDHUtils.generate();
    if(keyPair != null) {
      publicKey = ECDHUtils.getStringFromPublicKey(keyPair.getPublic());
      privateKey = ECDHUtils.getStringFromPrivateKey(keyPair.getPrivate());
    } else {
      publicKey = "";
      privateKey = "";
    }
    keys[0] = publicKey;
    keys[1] = privateKey;
    return keys;
  }

  private static KeyPair generate() {
    Curve25519KeyPairGenerator keyPairGenerator = new Curve25519KeyPairGenerator();
    // Ana generates a key-pair as follows:
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    keyPair.getPrivate();
    return keyPair;
  }

  public static String encrypt(final String message, final String publicKey, final String privateKey) {
    String encryptedMsg = "";
    Curve25519KeyAgreement keyAgreement = null;
    try {
      keyAgreement = new Curve25519KeyAgreement(getPrivateKeyFromString(privateKey));
      keyAgreement.doFinal(getPublicKeyFromString(publicKey));
      SecretKey sharedSecret = keyAgreement.generateSecret(ALGORITHM);

      String sharedKeyString = getStringFromSharedKey(sharedSecret);
      MessageDigest digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGO);
      digest.update(sharedKeyString.getBytes("UTF-8"));
      byte[] keyBytes = new byte[32];
      System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
      SecretKeySpec key = new SecretKeySpec(keyBytes, ALGO);

      Cipher c = Cipher.getInstance(ALGO);
      c.init(Cipher.ENCRYPT_MODE, key, ivspec);

      byte[] data = message.getBytes("UTF-8");
      byte[] encVal = c.doFinal(data);
      encryptedMsg = Base64.encodeToString(encVal, Base64.DEFAULT);

    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return encryptedMsg;
  }

  public static String decrypt(final String message, final String publicKey, final String privateKey) {
    Logger.i(TAG, "enc:" + message);
    String decryptedMsg = "";
    Curve25519KeyAgreement keyAgreement = null;
    try {
      byte[] decordedValue = Base64.decode(message.getBytes(), Base64.DEFAULT);

      keyAgreement = new Curve25519KeyAgreement(getPrivateKeyFromString(privateKey));
      keyAgreement.doFinal(getPublicKeyFromString(publicKey));
      SecretKey sharedSecret = keyAgreement.generateSecret(ALGORITHM);

      String sharedKeyString = getStringFromSharedKey(sharedSecret);
      MessageDigest digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGO);
      digest.update(sharedKeyString.getBytes("UTF-8"));
      byte[] keyBytes = new byte[32];
      System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
      SecretKeySpec key = new SecretKeySpec(keyBytes, ALGO);

      Cipher c = Cipher.getInstance(ALGO);
      c.init(Cipher.DECRYPT_MODE, key, ivspec);

      byte[] decValue = c.doFinal(decordedValue);
      decryptedMsg = new String(decValue);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return decryptedMsg;
  }

  private static Key generateCommonSecretKey(String privateKey, String publicKey) {
    long time = System.currentTimeMillis();
    Key secretKey = null;
    try {
      KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
      keyAgreement.init(getPrivateKeyFromString(privateKey));
      keyAgreement.doPhase(getPublicKeyFromString(publicKey), true);
      byte[] sharedsecret = keyAgreement.generateSecret();
      secretKey = new SecretKeySpec(sharedsecret, ALGO);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    Log.i(TAG, "generateCommonSecretKey:" + (System.currentTimeMillis() - time));
    return secretKey;
  }

  @SuppressLint("NewApi")
  private static PrivateKey getPrivateKeyFromString(String key) {
    byte[] bytes = Base64.decode(key, Base64.DEFAULT);
    return new Curve25519PrivateKey(bytes);
  }

  @SuppressLint("NewApi")
  private static PublicKey getPublicKeyFromString(String key) {
    byte[] bytes = Base64.decode(key, Base64.DEFAULT);
    return new Curve25519PublicKey(bytes);
  }

  public static String getStringFromPublicKey(PublicKey publicKey) {
    byte[] byteKey = publicKey.getEncoded();
    String key = Base64.encodeToString(byteKey, Base64.DEFAULT);
    key = key.substring(0, key.length()-1);
    Log.i(TAG, "publicKey:" + key);
    return key;
  }

  public static String getStringFromPrivateKey(PrivateKey privateKey) {
    byte[] byteKey = privateKey.getEncoded();
    String key = Base64.encodeToString(byteKey, Base64.DEFAULT);
    key = key.substring(0, key.length()-1);
    Log.i(TAG, "privateKey:" + key);
    return key;
  }

  public static String getStringFromSharedKey(SecretKey secretKey) {
    byte[] byteKey = secretKey.getEncoded();
    Log.i(TAG, "SecretKeybyte:" + byteKey.toString());
    String key = Base64.encodeToString(byteKey, Base64.DEFAULT);
    key = key.substring(0, key.length()-1);
    Log.i(TAG, "SecretKey:" + key);
    return key;
  }

}
