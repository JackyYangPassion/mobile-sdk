package io.inappchat.inappchat.nwchange;

import android.net.Network;

public interface NwChangeListener {
  void onAvailable(Network network);

  void onLost(Network network);
}
