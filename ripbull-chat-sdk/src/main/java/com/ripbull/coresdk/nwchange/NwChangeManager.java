package com.ripbull.coresdk.nwchange;

public interface NwChangeManager {

  void enable();

  void disable();

  void setNwChangeListener(NwChangeListener listener);
}
