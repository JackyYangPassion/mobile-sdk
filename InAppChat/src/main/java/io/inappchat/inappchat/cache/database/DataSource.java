package io.inappchat.inappchat.cache.database;

;

import io.inappchat.inappchat.cache.database.dao.ChatReactionDao;
import io.inappchat.inappchat.cache.database.dao.ChatThreadDao;
import io.inappchat.inappchat.cache.database.dao.DomainFilterDao;
import io.inappchat.inappchat.cache.database.dao.DownloadMediaDao;
import io.inappchat.inappchat.cache.database.dao.EKeyDao;
import io.inappchat.inappchat.cache.database.dao.GroupDao;
import io.inappchat.inappchat.cache.database.dao.ProfanityFilterDao;
import io.inappchat.inappchat.cache.database.dao.SingleChatDao;
import io.inappchat.inappchat.cache.database.dao.TenantDao;
import io.inappchat.inappchat.cache.database.dao.ThreadDao;
import io.inappchat.inappchat.cache.database.dao.ThreadUserLinkDao;
import io.inappchat.inappchat.cache.database.dao.UserDao;

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
