package org.inpertio.server.git.config.impl

import org.inpertio.server.git.config.LocalGitParameters
import org.inpertio.server.git.config.LocalGitParametersConfigProvider
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files

@Component
class LocalGitParametersConfigProviderImpl(
    @Value("\${${Parameter.LOCAL_ROOT}:}") rootPath: String,
    logger: Logger
) : LocalGitParametersConfigProvider {

    private val parameters: LocalGitParameters

    init {
        parameters = if (rootPath.isBlank()) {
            val dir = Files.createTempDirectory("").toFile()
            logger.info("No root local dir path is defined explicitly via '{}' property, using auto-generated "
                        + "directory at {}", Parameter.LOCAL_ROOT, dir.absolutePath)
            LocalGitParameters(dir)
        } else {
            logger.info("Using pre-configured root local dir {}", rootPath)
            LocalGitParameters(File(rootPath))
        }
    }

    override fun getData(): LocalGitParameters {
        return parameters
    }

    override fun refresh() {
    }

    override fun probe(): LocalGitParameters {
        return data
    }

    private object Parameter {
        const val LOCAL_ROOT = "local.data.root.path"
    }

}