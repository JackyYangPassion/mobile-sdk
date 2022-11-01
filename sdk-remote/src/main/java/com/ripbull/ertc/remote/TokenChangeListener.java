package com.ripbull.ertc.remote;

public interface TokenChangeListener {

  void onChatTokenChanged(String token, String refreshToken);
  void onUserTokenChanged(String token, String refreshToken);
}
