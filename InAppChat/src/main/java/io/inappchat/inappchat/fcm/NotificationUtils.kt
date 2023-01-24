package io.inappchat.inappchat.fcm

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import io.inappchat.inappchat.R
import io.inappchat.inappchat.InAppChat
import org.apache.commons.text.StringEscapeUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationUtils {

  companion object {
    private val TAG = NotificationUtils::class.java.simpleName
    private val CHANNEL_ID = "ertc_channel"

    fun showNotificationMessage(
      title: String, message: String, threadId: String,
      context: Context
    ) {
      // Check for empty push message
      if (TextUtils.isEmpty(message)) return
      val packageManager = context.packageManager
      val preferenceManager = NotificationPreferenceManager(context)
      var intent = InAppChat.fcmIntent
      // Log.e(TAG, "intent: " + intent);
      if (intent != null) {
        intent.putExtra("threadId", threadId)
      } else {
        intent = packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.putExtra("threadId", threadId)
      }
      val resultPendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
      val mBuilder =
        NotificationCompat.Builder(context, CHANNEL_ID)
      showSmallNotification(
        mBuilder, title, message, threadId, resultPendingIntent,
        preferenceManager, context
      )
    }

    private fun showSmallNotification(
      mBuilder: NotificationCompat.Builder, title: String,
      message: String, threadId: String, resultPendingIntent: PendingIntent,
      preferenceManager: NotificationPreferenceManager, context: Context
    ) {
      val inboxStyle =
        NotificationCompat.InboxStyle()
      var notificationId = 0
      if (preferenceManager.getNotificationId("threadId:$threadId") != 0) {
        notificationId = preferenceManager.getNotificationId("threadId:$threadId")
      } else {
        notificationId = createId()
        preferenceManager.setNotificationId("threadId:$threadId", notificationId)
      }
      preferenceManager.addNotification(threadId, message)
      // get the notifications from shared preferences
      val oldNotification = preferenceManager.getNotifications(threadId)
      val messages = oldNotification.split("\\|\\&\\#\\@\\|".toRegex()).toTypedArray()
      for (i in messages.indices) {
        inboxStyle.addLine(messages[i])
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name: CharSequence = "eRTC" // The user-visible name of the channel.
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mBuilder.setTicker(title)
        mBuilder.setAutoCancel(true)
        if (messages.size > 1) {
          mBuilder.setContentTitle(title + " (" + messages.size + " messages)")
        } else {
          mBuilder.setContentTitle(title)
        }
        mBuilder.setContentIntent(resultPendingIntent)
        mBuilder.setSmallIcon(getAppIcon(context))
        mBuilder.color = Color.parseColor("#F26339")
        mBuilder.setWhen(getTimeMilliSec())
        mBuilder.setContentText(message)
        mBuilder.setChannelId(CHANNEL_ID)
        mBuilder.setStyle(inboxStyle)
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        intent.putExtra("threadId", threadId)
        val pendingIntent =
          PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        mBuilder.setDeleteIntent(pendingIntent)
        val notificationManager =
          context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        notificationManager.notify("eRTC", notificationId, mBuilder.build())
      } else {
        mBuilder.setTicker(title)
        mBuilder.setAutoCancel(true)
        if (messages.size > 1) {
          mBuilder.setContentTitle(title + " (" + messages.size + " messages)")
        } else {
          mBuilder.setContentTitle(title)
        }
        mBuilder.setContentIntent(resultPendingIntent)
        mBuilder.setSmallIcon(getAppIcon(context))
        mBuilder.color = Color.parseColor("#F26339")
        mBuilder.setWhen(getTimeMilliSec())
        mBuilder.setContentText(message)
        mBuilder.setStyle(inboxStyle)
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        intent.putExtra("threadId", threadId)
        val pendingIntent =
          PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        mBuilder.setDeleteIntent(pendingIntent)
        val notificationManager =
          context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify("eRTC", notificationId, mBuilder.build())
      }
    }

    /** Method checks if the app is in background or not  */
    protected fun isAppIsInBackground(context: Context): Boolean {
      var isInBackground = true
      val am =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      val runningProcesses =
        am.runningAppProcesses
      for (processInfo in runningProcesses) {
        if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          for (activeProcess in processInfo.pkgList) {
            if (activeProcess == context.packageName) {
              isInBackground = false
            }
          }
        }
      }
      return isInBackground
    }
    // Clears notification tray messages

    // Clears notification tray messages
    private fun clearNotifications(context: Context) {
      val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.cancelAll()
    }

    private fun getTimeMilliSec(): Long {
      return Date().time
    }

    private fun createId(): Int {
      val now = Date()
      return SimpleDateFormat("mmssSSS", Locale.US).format(now).toInt()
    }

    protected fun uriDecoder(serverResponse: CharSequence): String? {
      val data =
        StringEscapeUtils.unescapeHtml3(serverResponse.toString())
      return StringEscapeUtils.unescapeXml(data)
    }

    private fun uriEncode(toServer: CharSequence): CharSequence? {
      return StringEscapeUtils.escapeXml11(toServer.toString())
    }

    private fun getAppIcon(context: Context): Int {
      var applicationInfo: ApplicationInfo? = null
      var appIcon: Int
      try {
        applicationInfo = context.packageManager
          .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        appIcon = applicationInfo.icon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          appIcon = R.drawable.small_notification
        }
      } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        appIcon = android.R.drawable.ic_dialog_alert
      }
      return appIcon
    }

    protected fun parseRegex(chatMessage: String): String {
      val pattern = Regex("<@+[a-zA-Z0-9._ @-]+>|<@channel>|<@here>")
      if (pattern.containsMatchIn(chatMessage)) {
        val parsedMessage = pattern.replace(chatMessage) { matchResult ->
          matchResult.value.substring(1, matchResult.value.length - 1)
        }
        return parsedMessage
      }
      return chatMessage
    }
  }

}