package com.ripbull.coresdk.core.type;

public enum TypingState {
  START_TYPING("on"),
  STOP_TYPING("off");

  private String state;

  TypingState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }
}
