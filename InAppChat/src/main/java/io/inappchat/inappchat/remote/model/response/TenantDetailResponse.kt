package io.inappchat.inappchat.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

/**
 * Created by DK on 10/12/18.
 */
class TenantDetailResponse(
  @SerializedName("config") @Expose var config: Config,
  @SerializedName("nameSpace") @Expose var nameSpace: String,
  @SerializedName("apiKey") @Expose var apiKey: String?,
  @SerializedName("name") @Expose var name: String,
  @SerializedName("tenantId") @Expose var id: String,
  @SerializedName("projectType") @Expose var projectType: String
) : ValidItem {

  //Todo : Dinesh to implement this
  override fun isValid(): Boolean {
    return true
  }

  inner class Config(
    @SerializedName("loginType") @Expose var loginType: String,
    @SerializedName("features") @Expose var features: Features,
    @SerializedName("serverDetails") @Expose var serverDetails: ServerDetails
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }
  }

  inner class ServerDetails(
    @SerializedName("chatServer") @Expose var chatServer: ServerDetail,
    @SerializedName("userServer") @Expose var userServer: ServerDetail?,
    @SerializedName("mqttServer") @Expose var mqttServer: ServerDetail
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }
  }

  inner class ServerDetail(
    @SerializedName("url") @Expose var url: String,
    @SerializedName("apiKey") @Expose var apiKey: String
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }
  }

  inner class Features(
    @SerializedName("chat") @Expose var chat: Chat?,
    @SerializedName("groupChat") @Expose var groupChat: GroupChat?,
    @SerializedName("typingStatus") @Expose var typingStatus: TypingStatus?,
    @SerializedName("readReceipt") @Expose var readReceipt: ReadReceipt?,
    @SerializedName("userProfile") @Expose var userProfile: UserProfile?,
    @SerializedName("blockUser") @Expose var blockUser: BlockUser?,
    @SerializedName("searchFilter") @Expose var searchFilter: SearchFilter?,
    @SerializedName("starredChat") @Expose var starredChat: StarredChat?,
    @SerializedName("notificationSettings") @Expose var notificationSettings: NotificationSettings?,
    @SerializedName("e2eEncryption") @Expose var e2EEncryption: E2EEncryption,
    @SerializedName("chatAttachment") @Expose var chatAttachment: ChatAttachment,
    @SerializedName("forwardChat") @Expose var forwardChat: ForwardChat?,
    @SerializedName("editChat") @Expose var editChat: EditChat?,
    @SerializedName("deleteChat") @Expose var deleteChat: DeleteChat? = null,
    @SerializedName("chatReactions") @Expose var chatReactions: ChatReactions? = null,
    @SerializedName("replyThread") @Expose var replyThread: ReplyThread? = null,
    @SerializedName("followChat") @Expose var followChat: FollowChat?,
    @SerializedName("unifiedSearch") @Expose var unifiedSearch: UnifiedSearch?,
    @SerializedName("userMentions") @Expose var userMentions: UserMentions?,
    @SerializedName("announcements") @Expose var announcements: Announcements?,
    @SerializedName("copyChat") @Expose var copyChat: CopyChat?,
    @SerializedName("localSearch") @Expose var localSearch: LocalSearch?,
    @SerializedName("moderation") @Expose var moderation: Moderation?
  ) : ValidItem {

    override fun isValid(): Boolean {
      return true
    }

    inner class Chat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class GroupChat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class TypingStatus(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class ReadReceipt(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("sentAlert") @Expose var sendAlert: SendAlert?,
      @SerializedName("deliveredAlert") @Expose var deliveredAlert: DeliveredAlert?,
      @SerializedName("readAlert") @Expose var readAlert: ReadAlert?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class SendAlert(
              @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class DeliveredAlert(
              @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class ReadAlert(
              @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }

    inner class UserProfile(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("image") @Expose var image: UserImage?,
      @SerializedName("name") @Expose var userName: UserName?,
      @SerializedName("favoriteContacts") @Expose var favoriteContacts: FavoriteContacts?,
      @SerializedName("availabilityStatus") @Expose var availabilityStatus: AvailabilityStatus?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class UserImage(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("editable") @Expose var editable: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class UserName(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("editable") @Expose var editable: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class FavoriteContacts(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class AvailabilityStatus(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("allowOverriding") @Expose var allowOverriding: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }

    inner class BlockUser(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class SearchFilter(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class StarredChat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class NotificationSettings(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("groupChat") @Expose var groupChatMuteSetting: ChatMuteSettings?,
      @SerializedName("chat") @Expose var chatMuteSetting: ChatMuteSettings?,
      @SerializedName("global") @Expose var globalMuteSetting: ChatMuteSettings?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class ChatMuteSettings(
        @SerializedName("supportedAllowFromValues") @Expose var supportedAllowFromValues: SupportedAllowFromValues?,
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class SupportedAllowFromValues(
          @SerializedName("mentions") @Expose var mention: Boolean? = false,
          @SerializedName("none") @Expose var none: Boolean? = false,
          @SerializedName("all") @Expose var all: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }
    }

    inner class E2EEncryption(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("text") @Expose var text: FeatureEnabled? = null,
      @SerializedName("location") @Expose var location: FeatureEnabled? = null,
      @SerializedName("contact") @Expose var contact: FeatureEnabled? = null,
      @SerializedName("gify") @Expose var gify: FeatureEnabled? = null,
      @SerializedName("media") @Expose var media: FeatureEnabled ? = null
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class FeatureEnabled(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }

    inner class ChatAttachment(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("image") @Expose var imageEnabled: Image?,
      @SerializedName("audio") @Expose var audioEnabled: Audio?,
      @SerializedName("video") @Expose var videoEnabled: Video?,
      @SerializedName("location") @Expose var locationEnabled: Location?,
      @SerializedName("contact") @Expose var contactEnabled: Contact?,
      @SerializedName("document") @Expose var documentEnabled: Document?,
      @SerializedName("gify") @Expose var gifyEnabled: Gify?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class Image(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatImageSharing: IndividualChatImageSharing?,
        @SerializedName("groupChat") @Expose var groupChatImageSharing: GroupChatImageSharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatImageSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatImageSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Audio(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatAudioSharing: IndividualChatAudioSharing?,
        @SerializedName("groupChat") @Expose var groupChatAudioSharing: GroupChatAudioSharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatAudioSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatAudioSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Video(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatVideoSharing: IndividualChatVideoSharing?,
        @SerializedName("groupChat") @Expose var groupChatVideoSharing: GroupChatVideoSharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatVideoSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatVideoSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Location(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatLocationSharing: IndividualChatLocationSharing?,
        @SerializedName("groupChat") @Expose var groupChatLocationSharing: GroupChatLocationSharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatLocationSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatLocationSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Contact(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatContactSharing: IndividualChatContactSharing?,
        @SerializedName("groupChat") @Expose var groupChatContactSharing: GroupChatContactSharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatContactSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatContactSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Document(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatDocumentSharing: IndividualChatDocumentSharing?,
        @SerializedName("groupChat") @Expose var groupChatDocumentSharing: GroupChatDocumentSharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatDocumentSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatDocumentSharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Gify(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false,
        @SerializedName("chat") @Expose var individualChatGifySharing: IndividualChatGifySharing?,
        @SerializedName("groupChat") @Expose var groupChatGifySharing: GroupChatGifySharing?
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatGifySharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatGifySharing(
          @SerializedName("enabled") @Expose var enabled: Boolean? = false
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }
    }

    inner class ForwardChat(
      @SerializedName("enabled") @Expose var enabled: Boolean,
      @SerializedName("text") @Expose var textEnabled: Text,
      @SerializedName("media") @Expose var mediaEnabled: Media,
      @SerializedName("location") @Expose var locationEnabled: Location,
      @SerializedName("contact") @Expose var contactEnabled: Contact,
      @SerializedName("gify") @Expose var gifyEnabled: Gify
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class Text(
        @SerializedName("enabled") @Expose var enabled: Boolean,
        @SerializedName("chat") @Expose var individualChatTextForward: IndividualChatTextForward,
        @SerializedName("groupChat") @Expose var groupChatTextForward: GroupChatTextForward
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatTextForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatTextForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Media(
        @SerializedName("enabled") @Expose var enabled: Boolean,
        @SerializedName("chat") @Expose var individualChatMediaForward: IndividualChatMediaForward,
        @SerializedName("groupChat") @Expose var groupChatMediaForward: GroupChatMediaForward
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatMediaForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatMediaForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Location(
        @SerializedName("enabled") @Expose var enabled: Boolean,
        @SerializedName("chat") @Expose var individualChatLocationForward: IndividualChatLocationForward,
        @SerializedName("groupChat") @Expose var groupChatLocationForward: GroupChatLocationForward
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatLocationForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatLocationForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Contact(
        @SerializedName("enabled") @Expose var enabled: Boolean,
        @SerializedName("chat") @Expose var individualChatContactForward: IndividualChatContactForward,
        @SerializedName("groupChat") @Expose var groupChatContactForward: GroupChatContactForward
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatContactForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatContactForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }

      inner class Gify(
        @SerializedName("enabled") @Expose var enabled: Boolean,
        @SerializedName("chat") @Expose var individualChatGifyForward: IndividualChatGifyForward,
        @SerializedName("groupChat") @Expose var groupChatGifyForward: GroupChatGifyForward
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }

        inner class IndividualChatGifyForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }

        inner class GroupChatGifyForward(
          @SerializedName("enabled") @Expose var enabled: Boolean
        ) : ValidItem {
          override fun isValid(): Boolean {
            return true
          }
        }
      }
    }

    inner class EditChat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean = true
    }

    inner class DeleteChat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("deleteForSelf") @Expose var deleteForSelf: DeleteForSelf?,
      @SerializedName("deleteForEveryone") @Expose var deleteForEveryone: DeleteForEveryone?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class DeleteForSelf(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class DeleteForEveryone(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }

    inner class ChatReactions(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean = true
    }

    inner class ReplyThread(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean = true
    }

    inner class FollowChat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class UnifiedSearch(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("supportedSearchQueryParams") @Expose var supportedSearchQueryParams: SupportedSearchQueryParams?,
      @SerializedName("supportedResultCategories") @Expose var supportedResultCategories: SupportedResultCategories?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class SupportedSearchQueryParams(
        @SerializedName("groupType") @Expose var groupType: Boolean? = false,
        @SerializedName("joined") @Expose var joined: Boolean? = false,
        @SerializedName("threadId") @Expose var threadId: Boolean? = false,
        @SerializedName("text") @Expose var text: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class SupportedResultCategories(
        @SerializedName("channels") @Expose var channels: Boolean? = false,
        @SerializedName("files") @Expose var files: Boolean? = false,
        @SerializedName("messages") @Expose var messages: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }

    inner class UserMentions(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("groupChat") @Expose var groupChat: GroupChat?,
      @SerializedName("chat") @Expose var chat: Chat?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class GroupChat(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class Chat(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }

    inner class Announcements(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class CopyChat(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class LocalSearch(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }
    }

    inner class Moderation(
      @SerializedName("enabled") @Expose var enabled: Boolean? = false,
      @SerializedName("chatReport") @Expose var chatReport: ChatReport?,
      @SerializedName("domainFilter") @Expose var domainFilter: DomainFilter?,
      @SerializedName("profanityFilter") @Expose var profanityFilter: ProfanityFilter?,
      @SerializedName("smartThrottling") @Expose var smartThrottling: SmartThrottling?,
      @SerializedName("spamFiltering") @Expose var spamFiltering: SpamFiltering?
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class ChatReport(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class DomainFilter(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class ProfanityFilter(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class SmartThrottling(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class SpamFiltering(
        @SerializedName("enabled") @Expose var enabled: Boolean? = false
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }
  }
}