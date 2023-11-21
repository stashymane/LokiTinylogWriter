package dev.stashy.lokiwriter

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import org.tinylog.core.LogEntry
import org.tinylog.core.LogEntryValue
import org.tinylog.writers.Writer
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LokiWriter() : Writer, Runnable {
    constructor(properties: java.util.Map<java.lang.String, java.lang.String>) : this() {
        properties.forEach { t, u -> println("$t = $u") }
    }

    companion object {
        var lokiUrl: String = ""
    }

    private val scheduler = Executors.newScheduledThreadPool(1)
    private val entries: MutableList<LogEntry> = mutableListOf()

    private val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
            defaultRequest {
                url(lokiUrl)
            }
        }
    }

    init {
        scheduler.scheduleAtFixedRate(this, 5, 5, TimeUnit.SECONDS)
    }

    override fun getRequiredLogEntryValues(): MutableCollection<LogEntryValue> =
        EnumSet.of(LogEntryValue.LEVEL, LogEntryValue.MESSAGE, LogEntryValue.DATE)

    override fun write(entry: LogEntry) {
        entries.add(entry)
    }

    override fun flush() {
        if (lokiUrl.isNotEmpty() && entries.isNotEmpty()) {
            runBlocking { pushLogs() }
        }
    }

    override fun run() {
        flush()
    }

    override fun close() {
        scheduler.shutdown()
        scheduler.awaitTermination(1, TimeUnit.SECONDS)
        client.close()
    }

    private fun formatEntry(entry: LogEntry): List<String> {
        val instant = entry.timestamp.toInstant()
        return listOf(
            (instant.epochSecond * 1000000000L + instant.nano).toString(),
            entry.message
        )
    }

    private suspend fun pushLogs() {
        val values = entries.groupBy { mapOf("level" to it.level.name, "tag" to it.tag) }
        val content = LokiStream.PushRequest(values.map { LokiStream(it.key, it.value.map(::formatEntry)) })
        entries.clear()

        try {
            val result = client.post(LokiAPI.Push()) {
                setBody(content)
                contentType(ContentType.Application.Json)
            }

            if (!result.status.isSuccess()) {
                val resultBody = result.bodyAsText()
                println("Failed to push logs to Loki - HTTP ${result.status.value}")
                println(resultBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
