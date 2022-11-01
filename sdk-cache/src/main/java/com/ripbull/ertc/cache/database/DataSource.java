package com.ripbull.ertc.cache.database;

;

import com.ripbull.ertc.cache.database.dao.ChatReactionDao;
import com.ripbull.ertc.cache.database.dao.ChatThreadDao;
import com.ripbull.ertc.cache.database.dao.DomainFilterDao;
import com.ripbull.ertc.cache.database.dao.DownloadMediaDao;
import com.ripbull.ertc.cache.database.dao.EKeyDao;
import com.ripbull.ertc.cache.database.dao.GroupDao;
import com.ripbull.ertc.cache.database.dao.ProfanityFilterDao;
import com.ripbull.ertc.cache.database.dao.SingleChatDao;
import com.ripbull.ertc.cache.database.dao.TenantDao;
import com.ripbull.ertc.cache.database.dao.ThreadDao;
import com.ripbull.ertc.cache.database.dao.ThreadUserLinkDao;
import com.ripbull.ertc.cache.database.dao.UserDao;

/** Created by DK on 25/11/18. */

public interface DataSource {

  SingleChatDao singleChatDao();

  ChatThreadDao chatThreadDao();

  ThreadDao threadDao();

  ThreadUserLinkDao threadUserLinkDao();

  TenantDao tenantDao();

  UserDao userDao();

  GroupDao groupDao();

  EKeyDao ekeyDao();

  DownloadMediaDao downloadMediaDao();

  ChatReactionDao chatReactionDao();

  DomainFilterDao domainFilterDao();

  ProfanityFilterDao profanityFilterDao();
}
