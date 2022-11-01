package com.ripbull.ertc.cache.db

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.ripbull.ertc.cache.database.ErtcDatabase
import com.ripbull.ertc.cache.database.dao.TenantDao
import com.ripbull.ertc.cache.database.dao.UserDao
import com.ripbull.ertc.cache.database.entity.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by DK on 19/06/20.
 */
@RunWith(AndroidJUnit4::class)
class UserDaoTest {
  var database: ErtcDatabase? = null
  private var userDao: UserDao? = null
  private var tenantDao: TenantDao? = null
  @Before
  fun createDb() {
    val appContext = InstrumentationRegistry.getTargetContext()
    database =
      Room.inMemoryDatabaseBuilder(appContext, ErtcDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    userDao = database!!.userDao()
    tenantDao = database!!.tenantDao()
  }

  @Test
  fun `queryUserTableWithArrayOfUserAppIds`() {
    val tenant = FakeData.TENANT
    tenantDao?.save(tenant)
    val arrayList = arrayListOf(FakeData.TENANT_CONFIG, FakeData.TENANT_CONFIG)
    tenantDao?.save(arrayList)
    val arrayListOf = arrayListOf<User>()

    val user1 = FakeData.USER;
    arrayListOf.add(user1)
    userDao?.insert(user1)

    val user2 = user1.copy(id = "demo@gmail.com")
    arrayListOf.add(user2)
    userDao?.insert(user2)

    val user3 = user1.copy(id = "user@gmail.com")
    arrayListOf.add(user3)
    userDao?.insert(user3)

    val stringBuilder = StringBuilder()
    arrayListOf.forEach {
      stringBuilder.append("'${it.id}',")
    }
    val string  = arrayOf("hello@gmail.com","demo@gmail.com","user@gmail.com")

    val buddyInfo =
      userDao?.getUserChatIdByUserAppIds(string)
    Assert.assertEquals(3,buddyInfo?.size)
  }

  @After
  @Throws(Exception::class)
  fun closeDb() {
     database?.close();
  }
}