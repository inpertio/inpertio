package org.inpertio.cucumber.glue

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.inpertio.test.http.HttpTestContext
import org.inpertio.test.util.TestUtil.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import java.net.HttpURLConnection
import java.net.URL

class HttpStepDefinitions {

    @Autowired private lateinit var context: HttpTestContext

    @LocalServerPort private var port: Int = 0

    @When("^GET request to ([^\\s]+) is made$")
    fun makeGetRequest(path: String) {
        val url = "http://127.0.0.1:$port$path"
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
        }
        val response = connection.inputStream.readBytes()
        connection.disconnect()

        context.onResponse(url, HttpMethod.GET, response)
    }

    @Then("^the last ([^\\s]+) request returns the following:$")
    fun verifyLastSuccessfulResponse(rawMethod: String, expected: String) {
        val method = HttpMethod.valueOf(rawMethod)
        val actual = context.getLastResponse(method) ?: fail("No $method response is found")
        assertThat(String(actual)).isEqualTo(expected)
    }
}