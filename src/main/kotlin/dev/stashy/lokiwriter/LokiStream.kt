package dev.stashy.lokiwriter

import kotlinx.serialization.Serializable

@Serializable
data class LokiStream(
    val stream: Map<String, String> = mapOf(),
    val values: List<List<String>> = listOf()
) {

    @Serializable
    data class PushRequest(val streams: List<LokiStream> = listOf())
}
