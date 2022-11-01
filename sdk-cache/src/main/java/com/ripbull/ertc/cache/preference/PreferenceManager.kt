package com.ripbull.ertc.cache.preference



/**
 * @author meeth
 */

interface PreferenceManager {
  var tenantId: String?
  var userId: String?
  var name: String?
  var loginType: String?
  var userServer: String?
  var chatServer: String?
  var mqttServer: String?
  var chatUserId: String? //eRTC_USER_ID
  var apiKey: String?
  var chatApiKey: String?
  var userApiKey: String?
  var mqttApiKey: String?
  var appUserId: String?
  var fcmToken: String?
  var deviceId: String?
  var packageName: String?
  var userToken: String?
  var userRefreshToken: String?
  var chatToken: String?
  var chatRefreshToken: String?
  var lastCallTimeUser: String?
  var skipRestoreFlag: Boolean
  var projectType: String?

  fun clearData()
}
