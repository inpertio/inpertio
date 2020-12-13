package org.inpertio

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
        plugin = [
            "pretty",
            "html:build/reports/cucumber",
            "json:build/reports/cucumber.json",
            "junit:build/reports/cucumber.xml"
        ],
        features = ["classpath:features"],
        glue = ["org.inpertio.cucumber.glue"]
)
class CucumberRunner