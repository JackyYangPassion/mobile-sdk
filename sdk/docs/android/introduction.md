![Maven Central](https://img.shields.io/maven-central/v/io.inappchat/sdk) ![GitHub issues](https://img.shields.io/github/issues/inappchat/android-example)

![BotStacks](https://private-user-images.githubusercontent.com/106978117/287741102-b3a09579-49e9-44e3-a054-cf8c290d01b8.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MDg5NzAwMTgsIm5iZiI6MTcwODk2OTcxOCwicGF0aCI6Ii8xMDY5NzgxMTcvMjg3NzQxMTAyLWIzYTA5NTc5LTQ5ZTktNDRlMy1hMDU0LWNmOGMyOTBkMDFiOC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwMjI2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDIyNlQxNzQ4MzhaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xMDhmMzZjYzFjZTAwOTk0Yjk3YTM1MDkwMDMwMDFmNWJmZmVhMzI1NTM4M2NlYTA2OTAzNmUyYzY3ZWJlMjJmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.QMKOsYTGa70taEz9XIRls-_93iaex3C0mOFwrNlxbfA)


# BotStacks Android SDK

> Delightful chat for your Android apps

Try the demo, download [BotStacks Android](https://play.google.com/store/apps/details?id=ai.botstacks.sample)

&nbsp;

# 📃 Table of Contents

- [Overview](https://github.com/BotStacks/android-sdk#-overview)
- [Installation](https://github.com/BotStacks/android-sdk#-installation)
- [Getting Started](https://github.com/BotStacks/android-sdk#-getting-started)
- [Theming](https://github.com/BotStacks/android-sdk#-theming)

&nbsp;

# ✨ Overview

This SDK integrates a fully serviced chat experience on the [BotStacks](https://botstacks.ai) platform.

&nbsp;

# ⚙ Installation

Add `jitpack.io` to your repositories in your `settings.gradle`

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

&nbsp;

Add `io.inappchat:sdk:1.0.1` to your dependencies

```gradle

dependencies {
    implementation 'ai.botstacks:sdk:1.0.0'
}

```

&nbsp;

# 🚀 Getting Started

The minimum it takes to add BotStacks is four lines of code.
This repo is also an example app using the Android SDK.

Feel free to clone it and view the source.

&nbsp;

### Step 1: Initialize the SDK

In your Application class, call BotStacksChat.setup with your API key. Create an account at [BotStacks.ai](https://BotStacks.ai) to get your own API key.

If you don't have an Application class, [create one](https://guides.codepath.com/android/Understanding-the-Android-Application-Class).

```kotlin
BotStacksChat.shared.setup(context = context, apiKey = "your api key")
```

&nbsp;

## Step 2: Render the UI

The BotStacks UI Kit uses Jetpack Compose.
You can add it to any `NavHost` by rendering inside an `BotStacksThemeEngine` and adding the `BotStacksChatController`. Customization controls for the Theme Engine are described below.

```kotlin
BotStacksThemeEngine {
    NavHost(navController = navController, startDestination = "splash") {
        val openChat = {
            navController.navigate("chats")
        }
        composable("splash") {
            Splash(openChat = openChat, openLogin = {
                navController.navigate("login")
            })
        }
        composable("login") {
            Login(openChat)
        }

        composable("chats") {
           BotStacksChatController(onLogout = { navController.navigate("login") })
        }
    }
}
```

&nbsp;

## Step 3: Push Notifications (Firebase Cloud Messaging)

For push notifications via FCM, just pass your push token to BotStacks

```kotlin
BotStacksChat.registerFCMToken(token)
```

&nbsp;

## Step 4: Giphy Support

If you'd like Giphy support in your chats, send your Giphy API key during `setup`.

```kotlin
BotStacksChat.shared.setup(
    context = context,
    apiKey = "your_botstacks_api_key",
    giphyApiKey = "your_giphy_api_key"
)
```

&nbsp;

# 🖍 Theming

You can theme your BotStacks UI kit by modifying the defaults of the `BotStacksThemeEngine`. The theme supports fonts, colors, assets, and dimensions. Configure it like this:


```kotlin
BotStacksThemeEngine(
    // true or false to force theming one way (default follows system)
    useDarkTheme = isSystemInDarkTheme(),
    // color scheme for light mode
    lightColorScheme = lightBotStacksColors(
        primary = Purple40,
        onPrimary = Color.White,
    ),
    // color scheme for dark mode
    darkColorScheme = darkBotStacksColors(
        primary = Purple80,
        onPrimary = Color.Black
    ),
    // fonts to utilize for Text within components
    fonts = with(Typography.bodyLarge) {
        botstacksFonts(
            body1 = FontStyle(
                size = fontSize,
            )
        )
    },
    // assets for empty state and logo (in header)
    assets = Assets(
        logo = R.drawable.inappchat_icon,
        emptyChat = EmptyScreenConfig.Messages(
            caption = "No messages yet."
        )
    ),
    // shape definitions for components
    shapes = ShapeDefinitions(
        small = 4.dp,
        medium = 10.dp,
        large = 16.dp
    )
) {
    // content code here (components, Controller)
}
```

&nbsp;

# 🙋‍♂️ Help

If you don't understand something in the documentation, you are experiencing problems, or you just need a gentle nudge in the right direction, please join our [Discord server](https://discord.com/invite/5kwyQCz3zZ)

---

**All Content Copyright © 2023 BotStacks**