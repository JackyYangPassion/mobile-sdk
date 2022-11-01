package com.ripbull.coresdk.module;

import com.ripbull.coresdk.data.DataManager;

/** Created by DK on 16/03/19. */
public class CoreModuleImpl implements CoreModule {

  private DataManager dataManager;

  public static CoreModule newInstance(DataManager dataManager) {
    return new CoreModuleImpl(dataManager);
  }

  private CoreModuleImpl(DataManager dataManager) {
    this.dataManager = dataManager;
  }
}
