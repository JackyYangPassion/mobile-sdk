package com.ripbull.ertc.cache.db

import androidx.room.Room
import com.ripbull.ertc.cache.database.ErtcDatabase
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ripbull.ertc.cache.database.dao.ChatReactionDao
import com.ripbull.ertc.cache.database.dao.ChatThreadDao
import com.ripbull.ertc.cache.database.dao.SingleChatDao
import com.ripbull.ertc.cache.database.dao.TenantDao
import com.ripbull.ertc.cache.database.dao.ThreadDao
import com.ripbull.ertc.cache.database.dao.UserDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by DK on 03/07/20.
 */
@RunWith(AndroidJUnit4::class)
class ChatReactionDaoTest {
  var database: ErtcDatabase? = null
  private var tenantDao: TenantDao? = null
  private var threadDao: ThreadDao? = null
  private var singleChatDao: SingleChatDao? = null
  private var chatThreadDao: ChatThreadDao? = null
  private var chatReactionDao: ChatReactionDao? = null
  private var userDao: UserDao? = null

  @Before
  fun createDb() {
    val appContext = InstrumentationRegistry.getTargetContext()
    database =
      Room.inMemoryDatabaseBuilder(appContext, ErtcDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    tenantDao = database!!.tenantDao()
    threadDao = database!!.threadDao()
    singleChatDao = database!!.messageDao()
    chatThreadDao = database!!.chatThreadDao()
    chatReactionDao = database!!.chatReactionDao()
    userDao = database!!.userDao()

    tenantDao?.insertWithReplace(FakeData.TENANT)

    val user1 = FakeData.USER;
    userDao?.insertWithReplace(user1)

    val user2 = user1.copy(id = "demo@gmail.com", userChatId = FakeData.USER_CHAT_ID1)
    userDao?.insertWithReplace(user2)

    val user3 = user1.copy(id = "user@gmail.com", userChatId = FakeData.USER_CHAT_ID2)
    userDao?.insertWithReplace(user3)


    threadDao?.insertWithReplace(FakeData.THREAD)
    singleChatDao?.insertWithReplace(FakeData.SINGLE_CHAT)
  }

  @Test
  fun `insert_and_retrieve_reaction_within_user_1_to_1_chat`() {
    chatReactionDao?.insertWithReplace(FakeData.SINGLE_CHAT_REACTION)
    singleChatDao?.getAll(FakeData.THREAD_ID)
      ?.test() // assertValue asserts that there was only one emission of the user
      ?.assertValue { singleChatEmbedded ->
        singleChatEmbedded.size == 1
      }
  }

  @Test
  fun `insert_and_retrieve_reaction_within_user_1_to_1_chat_thread`() {
    chatThreadDao?.insertWithReplace(FakeData.CHAT_THREAD)
    chatReactionDao?.insertWithReplace(FakeData.THREAD_CHAT_REACTION)

    chatThreadDao?.getAll(FakeData.LOCAL_MSG_ID)
      ?.test() // assertValue asserts that there was only one emission of the user
      ?.assertValue { threadChatEmbedded ->
        threadChatEmbedded.size == 1
      }
  }

  @Test
  fun `insert_and_retrive_user_list_from_reactions_list`(){

    chatThreadDao?.insertWithReplace(FakeData.CHAT_THREAD)
    chatReactionDao?.insertWithReplace(FakeData.THREAD_CHAT_REACTIONS)
    chatReactionDao?.getReactionedUsersOnThread(
      arrayOf(
        "U+1F6001",
        "U+1F6002",
        "U+1F6003"
      ),
      FakeData.LOCAL_CHAT_THREAD_MSG_ID,
      FakeData.THREAD_ID
    )
      ?.test() // assertValue asserts that there was only one emission of the user
      ?.assertValue { chatReactionAndUsers ->
        chatReactionAndUsers.size == 1
      }
  }
  @Test
  fun `insert_and_retrieve_reaction_within_user_group_chat_thread`() {

  }

  @Test
  fun `insert_and_retrieve_reaction_within_user_group_chat`() {

  }

  @After
  @Throws(Exception::class)
  fun closeDb() {
    database?.close()
  }
}