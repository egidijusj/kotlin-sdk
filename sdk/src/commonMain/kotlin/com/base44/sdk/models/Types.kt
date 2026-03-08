package com.base44.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// ---- Error ----

/**
 * Represents an error returned by the Base44 API.
 */
class Base44Exception(
    val status: Int,
    override val message: String,
    val code: String? = null,
    val data: JsonObject? = null,
) : Exception(message)

// ---- Auth ----

@Serializable
data class LoginResponse(
    @SerialName("access_token") val accessToken: String,
    val user: JsonObject? = null,
)

// ---- Delete results ----

@Serializable
data class DeleteResult(
    val id: String? = null,
    val deleted: Boolean? = null,
)

@Serializable
data class DeleteManyResult(
    val deleted: Int? = null,
)

@Serializable
data class ImportResult(
    val created: Int? = null,
    val updated: Int? = null,
    val errors: List<String>? = null,
)

// ---- Agents ----

@Serializable
data class AgentConversation(
    val id: String,
    val messages: List<AgentMessage>? = null,
    @SerialName("agent_name") val agentName: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
)

@Serializable
data class AgentMessage(
    val id: String? = null,
    val role: String? = null,
    val content: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class CreateConversationParams(
    @SerialName("agent_name") val agentName: String? = null,
    val title: String? = null,
    val metadata: JsonObject? = null,
)
