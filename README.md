![Maven Central](https://img.shields.io/maven-central/v/ai.botstacks/chat-sdk) ![GitHub issues](https://img.shields.io/github/issues/botstacks/mobile-sdk)

![BotStacks](https://github.com/BotStacks/mobile-sdk/assets/1652321/3c9b2037-0e18-4a59-9faa-d46e1e76a449)


![BotStacks](botstacks-logo.png)


# BotStacks Mobile Chat SDK

> Delightful chat for your mobile apps

&nbsp;

# 📃 Table of Contents

- [Overview](#-overview)
- [Installation](#-installation)
- [Help](#-help)

&nbsp;

# ✨ Overview

This SDK integrates a fully serviced chat experience on the [BotStacks](https://botstacks.ai) platform.

&nbsp;

# ⚙ Installation

### Compose Multiplatform

Add `ai.botstacks:chat-sdk:{version}` to your dependencies

```gradle
val commonMain by getting {
    dependencies {
        [...]
+       implementation("ai.botstacks:chat-sdk:{version}")
        [...]
    }
}
```

> [!TIP]
> To setup Compose Multiplatform click [here](docs/compose/compose-getting-started.md)

### Android Only

If you are only targeting Android the dependency is:

```gradle
dependencies {
    [...]
+   implementation("ai.botstacks:chat-sdk-android:{version}")
    [...]
}
```

> [!TIP]
>  To setup Android click [here](docs/android/android-getting-started.md)

&nbsp;

# 🙋‍♂️ Help

If you don't understand something in the documentation, you are experiencing problems, or you just need a gentle nudge in the right direction, please join our [Discord server](https://discord.com/invite/5kwyQCz3zZ)

---

**All Content Copyright © 2023 BotStacks**
