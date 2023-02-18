import androidx.compose.ui.graphics.Color

data class Colors(
    val light: Boolean = false,
    val bubble: Color = if (light) Color(0xF0F0F0) else Color(0x2B2B2B),
    val senderText: Color = Color(0x202127), // Color(hex: 0xE3E3E3)
    val senderUsername: Color = if (light) Color(0) else Color(0xFFFFFF),
    val text: Color = if (light) Color(0x1C1C1C) else Color(0xE3E3E3),
    val bubbleText: Color = if (light) Color(0x1C1C1C) else Color(0xE3E3E3),
    val username: Color = if (light) Color(0x2D3237) else Color(0xE3E3E3),
    val timestamp: Color = if (light) Color(0x71869C) else Color(0xE3E3_E34D),
    val primary: Color = Color(0x0091ff),
    val button: Color = if (light) Color(0xF0F0F0) else Color(0x2B2B2B),
    val background: Color = if (light) Color(0xFFFFFF) else Color(0x171717),
    val destructive: Color = Color(0xC74848),
    val softBackground: Color = if (light) Color(0xD4D4D4) else Color(0x2B2B2B),
    val caption: Color = if (light) Color(0x2C2C2C50) else Color(0xE3E3E350),
    val unread: Color = Color(0xC74848),
    val _public: Color = Color(0x4B48C7),
    val _private: Color = Color(0x488AC7),
    val border: Color = if (light) Color(0x1B1B1B) else Color(0xE3E3E3)
)