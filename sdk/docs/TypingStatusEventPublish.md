
# TypingStatusEventPublish

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**tenantId** | **kotlin.String** | Unique identified tenant |  [optional]
**participants** | **kotlin.collections.List&lt;kotlin.String&gt;** | Array of thread participant eRTCUserId except sender |  [optional]
**threadId** | **kotlin.String** | Unique identified Thread |  [optional]
**eRTCUserId** | **kotlin.String** | User&#39;s eRTCUserId who pubblished this topic |  [optional]
**typingStatusEvent** | **kotlin.String** | Typing status event type : on/off |  [optional]
**threadType** | **kotlin.String** | Thread group : single/group |  [optional]
**msgCorrelationId** | **kotlin.String** | Client generated unique identifier used to trace message delivery till receiver. |  [optional]



