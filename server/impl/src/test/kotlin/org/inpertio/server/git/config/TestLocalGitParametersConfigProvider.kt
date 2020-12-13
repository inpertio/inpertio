package org.inpertio.server.git.config

import org.inpertio.test.util.TestAware
import org.slf4j.Logger
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.nio.file.Files

@Primary
@Component
class TestLocalGitParametersConfigProvider(
    private val logger: Logger
) : LocalGitParametersConfigProvider, TestAware {

    private val parameters: LocalGitParameters

    init {
        val localRepoRoot = Files.createTempDirectory("").toFile()
        logger.info("Using the following local data root dir: {}", localRepoRoot.absolutePath)
        parameters = LocalGitParameters(localRepoRoot)
    }

    override fun getData(): LocalGitParameters {
        return parameters
    }

    override fun refresh() {
    }

    override fun probe(): LocalGitParameters {
        return data
    }

    override fun onTestEnd() {
        parameters.rootDir.deleteRecursively()
        logger.info("Cleaned local data root {}", parameters.rootDir.absolutePath)
    }
}