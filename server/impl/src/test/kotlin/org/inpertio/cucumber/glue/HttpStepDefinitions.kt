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
        val code = connection.responseCode
        val response = try {
            connection.inputStream.readBytes()
        } catch (e: Exception) {
            null
        }
        connection.disconnect()

        context.onResponse(url, HttpMethod.GET, response, code)
    }

    @Then("^the last ([^\\s]+) request returns the following:$")
    fun verifyLastResponseContent(rawMethod: String, expectedContent: String) {
        val method = HttpMethod.valueOf(rawMethod)
        val actual = context.getLastResponse(method) ?: fail("No $method response is found")
        val content = actual.response ?: fail("Last $rawMethod request to ${actual.url} has no response")
        assertThat(String(content)).isEqualTo(expectedContent)
    }

    @Then("^the last ([^\\s]+) request has code (\\d+)$")
    fun verifyLastResponseCode(rawMethod: String, expectedCode: Int) {
        val method = HttpMethod.valueOf(rawMethod)
        val actual = context.getLastResponse(method) ?: fail("No $method response is found")
        assertThat(expectedCode).isEqualTo(actual.code)
    }
}