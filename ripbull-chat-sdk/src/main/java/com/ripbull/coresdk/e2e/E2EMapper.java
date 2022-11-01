package com.ripbull.coresdk.e2e;

import com.ripbull.ertc.cache.database.entity.EKeyTable;
import com.ripbull.ertc.remote.model.response.E2EKey;
import com.ripbull.ertc.mqtt.model.ChatTopicResponse;

public class E2EMapper {

  public static EKeyTable getEKey(E2EKey e2EKey, String tenantId) {
    EKeyTable eKeyTable = new EKeyTable();

    eKeyTable.setDeviceId(e2EKey.getDeviceId()) ;
    eKeyTable.setKeyId(e2EKey.getKeyId());
    eKeyTable.setPublicKey(e2EKey.getPublicKey());
    eKeyTable.setErtcUserId(e2EKey.getERTCUserId());
    eKeyTable.setTenantId(tenantId);
    return eKeyTable;
  }

  public static EKeyTable getEKeyFromChatTopicResponse(ChatTopicResponse response) {
      EKeyTable eKeyTable = new EKeyTable();
      eKeyTable.setDeviceId(response.getSenderKeyDetails().getDeviceId());
      eKeyTable.setErtcUserId(response.getSender().getERTCUserId());
      eKeyTable.setKeyId(response.getSenderKeyDetails().getKeyId());
      eKeyTable.setPublicKey(response.getSenderKeyDetails().getPublicKey());
      eKeyTable.setTenantId(response.getTenantId());
      return eKeyTable;
  }
}
