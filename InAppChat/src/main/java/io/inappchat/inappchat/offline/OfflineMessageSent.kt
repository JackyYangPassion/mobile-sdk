package io.inappchat.inappchat.offline

import android.text.TextUtils
import android.util.Log
import io.inappchat.inappchat.chat.mapper.ChatRecordMapper
import io.inappchat.inappchat.chat.mapper.ChatRequestMapper
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MessageStatus
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.e2e.ECDHUtils
import io.inappchat.inappchat.utils.Constants
import io.inappchat.inappchat.utils.Logger
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.inappchat.inappchat.cache.database.entity.SingleChat
import io.inappchat.inappchat.remote.model.request.ReplyThread
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.collections.ArrayList

class OfflineMessageSent {

    companion object {

        fun sendE2EMessage(
            disposable: CompositeDisposable,
            tenantId: String,
            singleChat: SingleChat,
            chatUserId: String,
            data: DataManager,
            parallelDeviceList: ArrayList<EKeyTable>?,
        ) {
            val deviceId = data.preference().deviceId
            val singleChatDao = data.db().singleChatDao()
            val chatThreadDao = data.db().chatThreadDao()
            val eKeyDao = data.db().ekeyDao()
            val thread = data.db().threadDao().getThreadByIdInSync(singleChat.threadId)
            val eKeyTable =
                eKeyDao.getMyLatestPrivateKey(thread.senderChatId, deviceId, tenantId)
            var replyThread: ReplyThread? = null
            if (eKeyTable == null) {
                return
            }

            if (singleChat.parentMsgId != null && singleChat.parentMsgId!!.isNotEmpty()) {
                val singleChatParent = data.db().singleChatDao()
                    .getChatByLocalMsgId(singleChat.parentMsgId, singleChat.threadId)
                replyThread = ReplyThread(singleChatParent.msgUniqueId, singleChat.sendToChannel)
            }

            var chatType = ChatType.SINGLE
            if (singleChat.type == ChatType.GROUP.type) {
                chatType = ChatType.GROUP
            }

            val isForwarded = (singleChat.isForwardMessage == 1)

            val message = Message(
                singleChat.message,
                singleChat.parentMsgId,
                singleChat.id,
                singleChat.sendToChannel,
                null, null, null, null, null,
                chatType,
                null, null,
                singleChat.forwardMsgUniqueId,
                isForwarded,
                singleChat.clientCreatedAt
            )

            val e2eRequest = ChatRequestMapper.requestE2E(
                thread!!,
                message,
                singleChat.id,
                replyThread,
                eKeyTable,
                eKeyDao,
                data.db().groupDao(),
                null,
                parallelDeviceList,
                singleChat.customData
            )

            disposable.add(data.network().api().sendE2EMessage(
                tenantId, e2eRequest, chatUserId
            ).flatMap { response ->

                val msgId1 = response.data?.requestId
                val chatStatus = response.chatStatus
                if (chatStatus == null || !chatStatus.retryRequired) {

                    // save key id in case of new key generation
                    if (chatStatus != null && chatStatus.keyList!!.isNotEmpty()) {
                        // my key updated with key_id
                        for (e2eKey in chatStatus.keyList!!) {
                            if (e2eKey.returnCode != null && e2eKey.returnCode.equals("receiverKeyNotValid")) {
                                //need to remove all data from local DB
                                eKeyDao.deleteInActiveDevice(e2eKey.eRTCUserId, e2eKey.deviceId)
                            } else {
                                if (e2eKey.deviceId == deviceId) {
                                    eKeyDao.setKeyId(
                                        chatUserId,
                                        e2eKey.publicKey,
                                        e2eKey.keyId,
                                        eKeyTable.time,
                                        deviceId
                                    )
                                } else {
                                    //update Remaining keys
                                    val updatedRow: Int = eKeyDao.updateKey(
                                        e2eKey.eRTCUserId,
                                        e2eKey.publicKey,
                                        e2eKey.keyId,
                                        e2eKey.deviceId,
                                        System.currentTimeMillis()
                                    )

                                    if (updatedRow == 0) {
                                        val eKeyTableUpdated = EKeyTable(
                                            keyId = e2eKey.keyId,
                                            deviceId = e2eKey.deviceId,
                                            publicKey = e2eKey.publicKey,
                                            privateKey = "",
                                            ertcUserId = e2eKey.eRTCUserId,
                                            tenantId = tenantId
                                        )
                                        eKeyDao.save(eKeyTableUpdated)
                                    }
                                }
                            }
                        }
                    }

                    if (response.replyThreadFeatureData?.parentMsgId != null && response.replyThreadFeatureData?.parentMsgId!!.isNotEmpty()) {

                        val chatThread =
                            chatThreadDao.getChatByClientId(msgId1, singleChat.threadId)
                        chatThread.msgUniqueId = response.msgUniqueId
                        chatThread.status = MessageStatus.SENT.status
                        Logger.d(
                            "thread_debug ###",
                            "API : METADATA_ID : $msgId1 ::: Thread_Id : $singleChat.threadId"
                        )
                        Logger.d("thread_debug ###", "API : chatThread : $chatThread")
                        chatThreadDao.insertWithReplace(chatThread)

                        if (response.replyThreadFeatureData?.replyMsgConfig == 1) {
                            val singleChat =
                                singleChatDao.getChatByLocalMsgId(msgId1, singleChat.threadId)
                            singleChat.msgUniqueId = response.msgUniqueId
                            singleChat.status = MessageStatus.SENT.status
                            singleChatDao.insertWithReplace(singleChat)
                            Logger.d("thread_debug ###", "API : singleChat : $singleChat")
                            // Here both main and chat thread should be given back to the app
                            Single.just(ChatRecordMapper.transform(chatThread = chatThread))
                            //}
                        } else {
                            Single.just(ChatRecordMapper.transform(chatThread = chatThread))
                        }
                        //}.doOnError { it.printStackTrace() }
                    } else {
                        singleChatDao.getChatByClientIdByRxSingle(msgId1, singleChat.threadId)
                            .flatMap { singleChat ->
                                singleChat.msgUniqueId = response.msgUniqueId
                                singleChat.status = MessageStatus.SENT.status
                                singleChatDao.insertWithReplace(singleChat)
                                Single.just(ChatRecordMapper.transform(singleChat = singleChat))
                            }
                    }
                } else {
                    // add retry using operator
                    val returnCode = chatStatus.returnCode
                    if (!TextUtils.isEmpty(returnCode)) {
                        when (returnCode) {
                            "senderKeyValidityExpired" -> {
                                Log.i("Offline", "senderKeyValidityExpired")
                                val keyPair = ECDHUtils.generateKeyPair()
                                val eKeyTableUpdated = EKeyTable(
                                    deviceId = deviceId,
                                    publicKey = keyPair[0],
                                    privateKey = keyPair[1],
                                    ertcUserId = chatUserId,
                                    tenantId = tenantId
                                )
                                eKeyDao.save(eKeyTableUpdated)

                                if (chatStatus.keyList!!.isNotEmpty()) {
                                    val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                                    Log.i("Offline", "senderKeyValidityExpired - 1")
                                    for (e2eKey in chatStatus.keyList!!) {
                                        if (e2eKey.returnCode != null && e2eKey.returnCode.equals(
                                                "receiverKeyNotValid"
                                            )
                                        ) {
                                            //need to remove inactive device from local DB
                                            eKeyDao.deleteInActiveDevice(
                                                e2eKey.eRTCUserId,
                                                e2eKey.deviceId
                                            )
                                        } else {
                                            if (e2eKey.deviceId == deviceId) {
                                                Log.i("Offline", "senderKeyValidityExpired - 2")
                                                eKeyDao.setKeyId(
                                                    chatUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    eKeyTable.time,
                                                    deviceId
                                                )
                                            } else {
                                                Log.i("Offline", "senderKeyValidityExpired - 3")
                                                val updatedRow = eKeyDao.updateKey(
                                                    e2eKey.eRTCUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    e2eKey.deviceId,
                                                    System.currentTimeMillis()
                                                )

                                                if (updatedRow == 0) {
                                                    val eKeyTableUpdated = EKeyTable(
                                                        keyId = e2eKey.keyId,
                                                        deviceId = e2eKey.deviceId,
                                                        publicKey = e2eKey.publicKey,
                                                        privateKey = "",
                                                        ertcUserId = e2eKey.eRTCUserId,
                                                        tenantId = tenantId
                                                    )
                                                    eKeyDao.save(eKeyTableUpdated)
                                                }

                                                newDeviceList.add(
                                                    EKeyTable(
                                                        keyId = e2eKey.keyId,
                                                        deviceId = e2eKey.deviceId,
                                                        publicKey = e2eKey.publicKey,
                                                        privateKey = "",
                                                        ertcUserId = e2eKey.eRTCUserId,
                                                        tenantId = tenantId
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                                // need to do retry here
                                sendE2EMessage(
                                    disposable,
                                    tenantId,
                                    singleChat,
                                    chatUserId,
                                    data,
                                    parallelDeviceList
                                )
                            }
                            "receiverKeyValidationError" -> {
                                // need to handle
                                Log.i("Offline", "receiverKeyValidationError")
                                if (chatStatus.keyList!!.isNotEmpty()) {
                                    Log.i("Offline", "receiverKeyValidationError - 1")
                                    for (e2eKey in chatStatus.keyList!!) {
                                        if (e2eKey.returnCode != null && e2eKey.returnCode.equals(
                                                "receiverKeyNotValid"
                                            )
                                        ) {
                                            //need to remove inactive device from local DB
                                            eKeyDao.deleteInActiveDevice(
                                                e2eKey.eRTCUserId,
                                                e2eKey.deviceId
                                            )
                                        } else {
                                            if (e2eKey.deviceId == deviceId) {
                                                Log.i(
                                                    "Offline",
                                                    "receiverKeyValidationError - 2"
                                                )
                                                eKeyDao.setKeyId(
                                                    chatUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    eKeyTable.time,
                                                    deviceId
                                                )
                                            } else {
                                                Log.i(
                                                    "Offline",
                                                    "receiverKeyValidationError - 3"
                                                )
                                                //receiverNewDeviceKeyAvailable is handle here. we will add new device details in eKey table
                                                val updatedRow = eKeyDao.updateKey(
                                                    e2eKey.eRTCUserId,
                                                    e2eKey.publicKey,
                                                    e2eKey.keyId,
                                                    e2eKey.deviceId,
                                                    System.currentTimeMillis()
                                                )

                                                if (updatedRow == 0) {
                                                    val eKeyTableUpdated = EKeyTable(
                                                        keyId = e2eKey.keyId,
                                                        deviceId = e2eKey.deviceId,
                                                        publicKey = e2eKey.publicKey,
                                                        privateKey = "",
                                                        ertcUserId = e2eKey.eRTCUserId,
                                                        tenantId = tenantId
                                                    )
                                                    eKeyDao.save(eKeyTableUpdated)
                                                }
                                            }
                                        }
                                    }
                                    // need to do retry here
                                    sendE2EMessage(
                                        disposable,
                                        tenantId,
                                        singleChat,
                                        chatUserId,
                                        data,
                                        parallelDeviceList
                                    )
                                }
                            }
                            "senderNewDeviceKeyAvailable" -> {
                                // need to handle
                                Log.i("Offline", "senderNewDeviceKeyAvailable")
                                if (chatStatus.keyList!!.isNotEmpty()) {
                                    val newDeviceList: ArrayList<EKeyTable> = ArrayList()
                                    Log.i("Offline", "senderNewDeviceKeyAvailable - 1")
                                    for (e2eKey in chatStatus.keyList!!) {
                                        if (e2eKey.deviceId == deviceId) {
                                            Log.i("Offline", "senderNewDeviceKeyAvailable - 2")
                                            eKeyDao.setKeyId(
                                                chatUserId,
                                                e2eKey.publicKey,
                                                e2eKey.keyId,
                                                eKeyTable.time,
                                                deviceId
                                            )
                                        } else {
                                            Log.i("Offline", "senderNewDeviceKeyAvailable - 3")
                                            val updatedRow = eKeyDao.updateKey(
                                                e2eKey.eRTCUserId,
                                                e2eKey.publicKey,
                                                e2eKey.keyId,
                                                e2eKey.deviceId,
                                                System.currentTimeMillis()
                                            )

                                            if (updatedRow == 0) {
                                                val eKeyTableUpdated = EKeyTable(
                                                    keyId = e2eKey.keyId,
                                                    deviceId = e2eKey.deviceId,
                                                    publicKey = e2eKey.publicKey,
                                                    privateKey = "",
                                                    ertcUserId = e2eKey.eRTCUserId,
                                                    tenantId = tenantId
                                                )
                                                eKeyDao.save(eKeyTableUpdated)
                                            }

                                            newDeviceList.add(
                                                EKeyTable(
                                                    keyId = e2eKey.keyId,
                                                    deviceId = e2eKey.deviceId,
                                                    publicKey = e2eKey.publicKey,
                                                    privateKey = "",
                                                    ertcUserId = e2eKey.eRTCUserId,
                                                    tenantId = tenantId
                                                )
                                            )
                                        }
                                    }
                                    // need to do retry here
                                    sendE2EMessage(
                                        disposable,
                                        tenantId,
                                        singleChat,
                                        chatUserId,
                                        data,
                                        newDeviceList
                                    )
                                }
                            }
                        }
                    }
                    Single.just(MessageRecord("ID"))
                }
            }.subscribe({}, {
                it.printStackTrace()
                it.message?.let { error ->
                    if (error.contains(Constants.ErrorMessage.ERROR_403)) {
                        singleChatDao.deleteByMsgId(singleChat.id)
                        chatThreadDao.deleteByMsgId(singleChat.id)
                    }
                }
            })
            )
        }
    }
}