package org.inpertio.cucumber.glue

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.spring.CucumberContextConfiguration
import org.inpertio.server.InpertioServerApplication
import org.inpertio.test.util.TestAware
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment

@SpringBootTest(
        classes = [InpertioServerApplication::class],
        webEnvironment = WebEnvironment.RANDOM_PORT
)
@CucumberContextConfiguration
class InpertioServerStepDefinitions {

    @Autowired private lateinit var testAwareBeans: Collection<TestAware>
    @Autowired private lateinit var logger: Logger

    @Before
    fun logScenarioStart(scenario: Scenario) {
        logger.info("Starting scenario '{}'", scenario.name)
    }

    @After
    fun logScenarioEnd(scenario: Scenario) {
        logger.info("Finished scenario '{}'", scenario.name)
    }

    @After
    fun propagateTestEndToTestAware() {
        testAwareBeans.forEach(TestAware::onTestEnd)
    }
}