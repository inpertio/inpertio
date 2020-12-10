package org.inpertio.test.http

import org.inpertio.test.util.TestAware
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class HttpTestContext : TestAware {

    private val responses = ConcurrentHashMap<HttpMethod, List<ResponseEntry>>()

    fun onResponse(url: String, method: HttpMethod, data: ByteArray) {
        responses.compute(method) { _, entries ->
            if (entries == null) {
                listOf(ResponseEntry(url, data))
            } else {
                entries + ResponseEntry(url, data)
            }
        }
    }

    fun getLastResponse(method: HttpMethod): ByteArray? {
        return responses[method]?.last()?.response
    }

    override fun onTestEnd() {
        responses.clear()
    }

    private class ResponseEntry(val url: String, val response: ByteArray)
}