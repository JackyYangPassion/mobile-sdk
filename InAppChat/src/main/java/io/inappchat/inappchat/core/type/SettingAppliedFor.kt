package io.inappchat.inappchat.core.type

enum class SettingAppliedFor(val duration:String) {
  ALWAYS("always"),
  HOURS_24("1 Day"),
  HOURS_72("3 Days"),
  WEEK_1("1 Week"),
  WEEKS_2("2 Weeks"),
  MONTH_1("1 Month")
}