# Base44 Kotlin SDK

A Kotlin Multiplatform SDK for [Base44](https://base44.app) — full parity with the JavaScript SDK for JVM, Android, iOS, macOS, and Linux targets.

## Requirements

- Kotlin 2.0+
- JVM 11+
- Kotlin Multiplatform targets: JVM, iOS (arm64/x64/SimulatorArm64), macOS (arm64/x64), Linux x64

## Installation

### Gradle (KMP)

Add to `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.base44:sdk:0.1.0")
}
```

Or for multiplatform:

```kotlin
commonMain.dependencies {
    implementation("com.base44:sdk:0.1.0")
}
```

> Publishing to Maven Central is in progress. For now, clone and use locally with `maven-publish`.

## Quick Start

```kotlin
import com.base44.sdk.createClient
import com.base44.sdk.CreateClientConfig
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// Create a client
val base44 = createClient(CreateClientConfig(appId = "your-app-id"))

// Authenticate
val response = base44.auth.loginViaEmailPassword("user@example.com", "password")
// Token is set automatically after login

// List entities
val products = base44.entities["Products"].list()

// Create an entity
val newProduct = base44.entities["Products"].create(buildJsonObject {
    put("name", "Widget")
    put("price", 9.99)
})

// Filter entities
val active = base44.entities["Products"].filter(
    query = buildJsonObject { put("status", "active") },
    sort = "-created_at",
    limit = 20,
)
```

## Token Storage

The SDK stores the JWT token in-memory. For persistence across app restarts, save to platform-specific secure storage:

```kotlin
// After login
val response = base44.auth.loginViaEmailPassword("...", "...")
// Save to DataStore, EncryptedSharedPreferences, Keychain, etc.
secureStorage.save("base44_token", response.accessToken)

// On app launch, restore:
secureStorage.load("base44_token")?.let { base44.setToken(it) }
```

## Modules

### `entities`

Access entity operations by name:

```kotlin
val handler = base44.entities["EntityName"]
```

| Method | Description |
|--------|-------------|
| `list(sort, limit, skip, fields)` | List with optional pagination |
| `filter(query, sort, limit, skip, fields)` | Filter by query |
| `get(id)` | Get by ID |
| `create(body)` | Create |
| `update(id, body)` | Update |
| `delete(id)` | Delete by ID |
| `deleteMany(query)` | Delete matching |
| `bulkCreate(bodies)` | Create multiple |
| `importEntities(bytes, filename, mimeType)` | Import from CSV/Excel |

### `auth`

| Method | Description |
|--------|-------------|
| `me()` | Get current user |
| `updateMe(body)` | Update current user |
| `loginViaEmailPassword(email, password)` | Email/password login |
| `loginViaOtp(email)` | Send OTP |
| `verifyOtp(email, otpCode)` | Verify OTP |
| `register(email, password)` | Create account |
| `resetPassword(email)` | Request reset |
| `resetPasswordConfirm(token, newPassword)` | Confirm reset |
| `changePassword(userId, current, new)` | Change password |
| `isAuthenticated()` | Check token validity |
| `setToken(token)` | Set auth token |
| `logout()` | Clear auth token |

### `functions`

```kotlin
val result = base44.functions.invoke("myFunction", buildJsonObject { put("key", "value") })
```

### `integrations`

```kotlin
// LLM
val result = base44.integrations.core.invokeLLM(prompt = "What is 2+2?")

// Image generation
val image = base44.integrations.core.generateImage(prompt = "A sunset")

// File upload
val uploaded = base44.integrations.core.uploadFile(bytes, "photo.jpg", "image/jpeg")

// Email
base44.integrations.core.sendEmail("user@example.com", "Subject", "Body")

// Generic
val response = base44.integrations.invoke("MyPackage", "MyEndpoint",
    buildJsonObject { put("param", "value") }
)
```

### `agents`

```kotlin
// Create conversation
val conv = base44.agents.createConversation(CreateConversationParams(agentName = "SupportBot"))

// Send message with automatic polling
val updated = base44.agents.sendMessage(conv, "Help me with my order")
println(updated.messages?.last()?.content)
```

### `appLogs`

```kotlin
val logs = base44.appLogs.list(limit = 100)
```

### `users`

```kotlin
val users = base44.users.list()
base44.users.inviteUser("new@example.com", "member")
```

### `asServiceRole`

Requires `serviceToken` in config:

```kotlin
val base44 = createClient(CreateClientConfig(
    appId = "my-app-id",
    serviceToken = "service-role-token"
))

val allOrders = base44.asServiceRole.entities["Orders"].list()
```

## Error Handling

All suspending functions throw `Base44Exception` on API errors:

```kotlin
try {
    val item = base44.entities["Products"].get("invalid-id")
} catch (e: Base44Exception) {
    println("Status: ${e.status}")    // 404
    println("Message: ${e.message}")  // "Not found"
    println("Code: ${e.code}")        // "NOT_FOUND"
}
```

## Realtime / WebSocket

Realtime entity subscriptions are not yet available. Use polling or refer to the JS SDK. This feature is planned for a future release.

## License

MIT
