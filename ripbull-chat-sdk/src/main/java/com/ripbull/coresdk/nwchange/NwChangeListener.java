package com.ripbull.coresdk.nwchange;

import android.net.Network;

public interface NwChangeListener {
  void onAvailable(Network network);

  void onLost(Network network);
}
