# Setup

## Getting Started

In order to display any of the UI components and access chat data, you must first initialize the SDK and log in as a chat user.

### Initialization

In your Application class, call BotStacksChat.shared.setup with your API key. You can obtain your API key from the [Botstacks Dashboard](https://dashboard.botstacks.ai/settings). If you don’t yet have one, you can [create one for FREE](https://dashboard.botstacks.ai/)!

If you don't have an Application class, [create one](https://guides.codepath.com/android/Understanding-the-Android-Application-Class).

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()
+       BotStacksChat.shared.setup(
+           context = this,
+           apiKey = "your-api-key"
+       )
    }
}
```

Note, you can optionally delay load and later call `BotStacksChat.shared.load` to load BotStacks in whatever load sequence you wish.

### Logging in

Nearly all functionality is within the context of a chat user. That said, you must first be logged in as a chat user in order to appropriately display the UI components.

To log in, call the login function prior to displaying any UI components. Below is an example of How to accomplish this.

```kotlin
val composeScope = rememberCoroutineScope()
composeScope.launch {
    BotStacksChat.shared.login(
        "user-identifier",
        "username"
    ) // optionally pass displayName and picture
    if (BotStacksChat.shared.isUserLoggedIn) {
        // handle logged in state change
    }
}
```
