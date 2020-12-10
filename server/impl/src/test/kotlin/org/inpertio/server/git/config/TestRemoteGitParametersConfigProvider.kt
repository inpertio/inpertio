package org.inpertio.server.git.config

import org.inpertio.server.util.FileUtil
import org.inpertio.test.util.TestAware
import org.slf4j.Logger
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files

@Primary
@Component
class TestRemoteGitParametersConfigProvider(
    private val logger: Logger
) : RemoteGitParametersConfigProvider, TestAware {

    private val parameters: RemoteGitParameters

    init {
        val remoteRepoRoot = Files.createTempDirectory("").toFile()
        FileUtil.ensureDirectoryExists(remoteRepoRoot)
        logger.info("Created a remote repo root dir {}", remoteRepoRoot.absolutePath)
        parameters = RemoteGitParameters("file://${remoteRepoRoot.absolutePath}")
    }

    override fun getData(): RemoteGitParameters {
        return parameters
    }

    override fun refresh() {
    }

    override fun probe(): RemoteGitParameters {
        return data
    }

    override fun onTestEnd() {
        File(parameters.uri.substring("file://".length)).deleteRecursively()
        logger.info("Cleaned remote git repo dir {}", parameters.uri)
    }
}