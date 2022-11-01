package com.ripbull.coresdk.chat.mapper

/**
 * Created by Sagar on 11/02/2022.
 */
data class ChatSettingsRecord(
  val profanityFilters: List<ProfanityFilterRecord>?,
  val domainFilters: List<DomainFilterRecord>?
)

data class ProfanityFilterRecord(
  val keywords: List<String>,
  val regexes: List<String>,
  val actionType: String
)

data class DomainFilterRecord(
  val domains: List<String>,
  val actionType: String
)