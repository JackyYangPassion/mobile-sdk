package io.inappchat.inappchat.cache.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import io.inappchat.inappchat.cache.database.entity.EKeyTable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

/** Created by DK on 22/12/18. */
@Dao
public interface EKeyDao extends BaseDao<EKeyTable> {
  String TABLE_NAME = "EKeys";

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE keyId = :keyId")
  Single<EKeyTable> getKey(String keyId);

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE ertcUserId = :eRTCUserId AND deviceId =:deviceId AND tenant_id =:tenantId ORDER BY time DESC LIMIT 1")
  EKeyTable getMyLatestPrivateKey(String eRTCUserId, String deviceId, String tenantId);

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE ertcUserId = :eRTCUserId AND deviceId =:deviceId AND tenant_id =:tenantId AND keyId =:keyId")
  EKeyTable getPrivateKeyByKeyId(String eRTCUserId, String deviceId, String tenantId, String keyId);

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE ertcUserId = :eRTCUserId AND tenant_id =:tenantId ORDER BY time DESC LIMIT 1")
  List<EKeyTable> getKeyByErtcId(String eRTCUserId, String tenantId);

  @Transaction
  @Query("UPDATE " + TABLE_NAME + " SET publicKey =:publicKey, keyId =:keyId, time =:time WHERE ertcUserId = :eRTCUserId AND deviceId =:deviceId")
  int updateKey(String eRTCUserId, String publicKey, String keyId, String deviceId, long time);

  @Transaction
  @Query("UPDATE " + TABLE_NAME + " SET keyId =:keyId WHERE ertcUserId = :eRTCUserId AND publicKey =:publicKey AND time=:time AND deviceId=:deviceId")
  void setKeyId(String eRTCUserId, String publicKey, String keyId, long time, String deviceId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void save(List<EKeyTable> list);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void save(EKeyTable key);

  @Query("DELETE FROM " + TABLE_NAME)
  void deleteAll();

  @Query("DELETE FROM " + TABLE_NAME + " WHERE ertcUserId = :ertcUserId  AND deviceId = :deviceId")
  void deleteInActiveDevice(String ertcUserId,  String deviceId);

  @Query("DELETE FROM " + TABLE_NAME + " WHERE ertcUserId = :ertcUserId")
  void deleteUserDetails(String ertcUserId);
}
