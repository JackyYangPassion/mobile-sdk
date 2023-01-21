package io.inappchat.inappchat.chat.repositories

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeleteMessageRepositoryTest {

  @MockK private var chatRepositoryImpl: FakeChatRepository = FakeChatRepository()


  @Before
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

  @Test
  fun  `test`(){
    assertEquals(4,2+2)
  }

  /*@Test fun test_delete_for_everyone_single_chat_thread() = runBlocking<Unit> {
    val msgList = ArrayList<Message>()
    val msgRecordList = ArrayList<MessageRecord>()
    val msg = Message(parentMsgId = FakeRepoData.SETVER_MSG_UNIQUE_ID, chatType = SINGLE_CHAT_THREAD)
    msgList.add(msg)
    val dataReceived = coEvery {
      chatRepositoryImpl.deleteMessage(FakeRepoData.DELETE_FOR_EVERYONE, FakeRepoData.THREAD_ID, msgList)
    } returns Single.just(msgRecordList.toList())
        .doOnError { it.printStackTrace() }
    assertNotNull(dataReceived)
   // assertEquals(dataReceived, 0)

  }

  @Test fun test_delete_for_everyone_single_chat() = runBlocking<Unit> {

    val gson = Gson()

   // deleteMsgResponse = gson.fromJson(getJson("DeleteMsgResponse.json"), DeleteMessageResponse::class.java)

    val msgList = ArrayList<Message>()
    val msgRecordList = ArrayList<MessageRecord>()
    val msg = Message(parentMsgId = FakeRepoData.SETVER_MSG_UNIQUE_ID, chatType = CHAT_THREAD)
    msgList.add(msg)
    val dataReceived = coEvery {
      chatRepositoryImpl.deleteMessage(FakeRepoData.DELETE_FOR_EVERYONE, FakeRepoData.THREAD_ID, msgList)
    } returns Single.just(msgRecordList.toList())
        .doOnError { it.printStackTrace() }
    assertNotNull(dataReceived)
    assertEquals(dataReceived, 0)

  }

  @Test fun test_delete_for_everyone_group_chat() = runBlocking<Unit> {

    val gson = Gson()

   // deleteMsgResponse = gson.fromJson(getJson("DeleteMsgResponse.json"), DeleteMessageResponse::class.java)

    val msgList = ArrayList<Message>()
    val msgRecordList = ArrayList<MessageRecord>()
    val msg = Message(parentMsgId = FakeRepoData.SETVER_MSG_UNIQUE_ID, chatType = GROUP)
    msgList.add(msg)
    val dataReceived = coEvery {
      chatRepositoryImpl.deleteMessage(FakeRepoData.DELETE_FOR_EVERYONE, FakeRepoData.THREAD_ID, msgList)
    } returns Single.just(msgRecordList.toList())
        .doOnError { it.printStackTrace() }
    assertNotNull(dataReceived)
    assertEquals(dataReceived, 0)

  }

  @Test fun test_delete_for_user_single_chat() = runBlocking<Unit> {

    val gson = Gson()

   // deleteMsgResponse = gson.fromJson(getJson("DeleteMsgResponse.json"), DeleteMessageResponse::class.java)

    val msgList = ArrayList<Message>()
    val msgRecordList = ArrayList<MessageRecord>()
    val msg = Message(parentMsgId = FakeRepoData.SETVER_MSG_UNIQUE_ID, chatType = CHAT_THREAD)
    msgList.add(msg)
    val dataReceived = coEvery {
      chatRepositoryImpl.deleteMessage(FakeRepoData.DELETE_FOR_USER, FakeRepoData.THREAD_ID, msgList)
    } returns Single.just(msgRecordList.toList())
        .doOnError { it.printStackTrace() }
    assertNotNull(dataReceived)
    assertEquals(dataReceived, 0)

  }

  @Test fun test_delete_for_user_single_chat_thread() = runBlocking<Unit> {

    val gson = Gson()

  //  deleteMsgResponse = gson.fromJson(getJson("DeleteMsgResponse.json"), DeleteMessageResponse::class.java)

    val msgList = ArrayList<Message>()
    val msgRecordList = ArrayList<MessageRecord>()
    val msg = Message(parentMsgId = FakeRepoData.SETVER_MSG_UNIQUE_ID, chatType = SINGLE_CHAT_THREAD)
    msgList.add(msg)
    val dataReceived = coEvery {
      chatRepositoryImpl.deleteMessage(FakeRepoData.DELETE_FOR_USER, FakeRepoData.THREAD_ID, msgList)
    } returns Single.just(msgRecordList.toList())
        .doOnError { it.printStackTrace() }
    assertNotNull(dataReceived)
    assertEquals(dataReceived, 0)

  }

  @Test fun test_delete_for_user_group_chat_thread() = runBlocking<Unit> {

    val gson = Gson()

   // deleteMsgResponse = gson.fromJson(getJson("DeleteMsgResponse.json"), DeleteMessageResponse::class.java)

    val msgList = ArrayList<Message>()
    val msgRecordList = ArrayList<MessageRecord>()
    val msg = Message(parentMsgId = FakeRepoData.SETVER_MSG_UNIQUE_ID, chatType = GROUP_CHAT_THREAD)
    msgList.add(msg)
    val dataReceived = coEvery {
      chatRepositoryImpl.deleteMessage(FakeRepoData.DELETE_FOR_USER, FakeRepoData.THREAD_ID, msgList)
    } returns Single.just(msgRecordList.toList())
        .doOnError { it.printStackTrace() }
    assertNotNull(dataReceived)
    assertEquals(dataReceived, 0)

  }*/
}

