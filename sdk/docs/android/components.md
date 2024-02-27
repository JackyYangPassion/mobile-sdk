# Android Component Usage

Our SDK at BotStacks also allows for direct component usage in your Jetpack Compose applications.

Outlined below are the components currently available and examples on how to use them.

- [Avatar](#avatar)

## Avatar

Our Avatar component renders an image for a given URL in a bordered circle at the specified size. This is utilized in our larger components to render user Avatars (as the component name claims).

There is a few overload methods for this component dependent on your usage.


```kotlin
Avatar(user = message.user)
Avatar(url = chat.displayImage, size = AvatarSize.Large, chat = true)
Avatar(type = AvatarType.Channel(listOf(chat.displayImage)))
Avatar(type = AvatarType.User(user))
```
Avatar sizing resolves around two defined sizes, with the ability to define a custom size.

`AvatarSize.Small` - renders at 50 dp

`AvatarSize.Large` - renders at 100 dp

`AvatarSize.Custom(val dp: Dp)` - renders at specified size

For a more detailed API definition, check out the API docs here.