package org.inpertio.server.git.config.impl

import org.inpertio.server.git.config.RemoteGitParameters
import org.inpertio.server.git.config.RemoteGitParametersConfigProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RemoteGitParametersConfigProviderImpl(
    @Value("\${${Parameter.URI}:}") uri: String
) : RemoteGitParametersConfigProvider {

    private val parameters: RemoteGitParameters

    init {
        if (uri.isBlank()) {
            throw IllegalStateException(
                    "No remote repo uri is provided, expected for it to be defined via '${Parameter.URI}' property"
            )
        }
        parameters = RemoteGitParameters(uri)
    }

    override fun getData(): RemoteGitParameters {
        return parameters
    }

    override fun refresh() {
    }

    override fun probe(): RemoteGitParameters {
        return data
    }

    private object Parameter {
        const val URI = "remote.repo.uri"
    }
}