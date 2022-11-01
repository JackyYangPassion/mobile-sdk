package com.ripbull.coresdk.chat.repositories


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.coresdk.chat.FakeRepoData


data class DeleteMsgResponse(
  @SerializedName("chats") @Expose val chats: ArrayList<DeletedMessageList?>
)


data class DeletedMessageList(
  @SerializedName("msgUniqueId") @Expose val msgUniqueId: String = FakeRepoData.SETVER_MSG_UNIQUE_ID,
  @SerializedName("replyThreadFeatureData") @Expose val replyThreadFeatureData: ReplyThreadFeatureData? = null
)


data class ReplyThreadFeatureData(
  @SerializedName("replyMsgConfig") @Expose val replyMsgConfig: Int
)