package org.inpertio.test.http

import org.inpertio.test.util.TestAware
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class HttpTestContext : TestAware {

    private val responses = ConcurrentHashMap<HttpMethod, List<ResponseEntry>>()

    fun onResponse(url: String, method: HttpMethod, data: ByteArray?, code: Int) {
        responses.compute(method) { _, entries ->
            if (entries == null) {
                listOf(ResponseEntry(url, data, code))
            } else {
                entries + ResponseEntry(url, data, code)
            }
        }
    }

    fun getLastResponse(method: HttpMethod): ResponseEntry? {
        return responses[method]?.last()
    }

    override fun onTestEnd() {
        responses.clear()
    }

    class ResponseEntry(val url: String, val response: ByteArray?, val code: Int)
}