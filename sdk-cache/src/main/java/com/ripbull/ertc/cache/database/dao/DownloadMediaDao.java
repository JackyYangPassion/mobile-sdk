package com.ripbull.ertc.cache.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.ripbull.ertc.cache.database.entity.DownloadMedia;
import java.util.List;

/** Created by Sagar on 03/03/20. */
@Dao
public interface DownloadMediaDao extends BaseDao<DownloadMedia> {

  @Query("SELECT * FROM download WHERE id = :id")
  DownloadMedia getDownloadById(int id);

  @Query("SELECT * FROM download WHERE msg_id = :msgId")
  DownloadMedia getDownloadByMsgId(String msgId);

  @Query("SELECT * FROM download WHERE last_modified_at <= :days")
  List<DownloadMedia> getUnwantedModels(long days);

  @Query("DELETE FROM download WHERE id = (:id)")
  void removeById(int id);

  @Query("DELETE FROM download WHERE msg_id = (:msgId)")
  void removeByMsgId(String msgId);

  @Query("DELETE FROM download")
  void clear();
}
