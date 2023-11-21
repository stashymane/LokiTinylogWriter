package dev.stashy.lokiwriter

import io.ktor.resources.*

@Resource("/loki/api/v1")
class LokiAPI {
    @Resource("push")
    class Push(val parent: LokiAPI = LokiAPI())
}

@Resource("/")
class LokiEndpoint {
    @Resource("ready")
    class Ready(val parent: LokiEndpoint = LokiEndpoint())
}
