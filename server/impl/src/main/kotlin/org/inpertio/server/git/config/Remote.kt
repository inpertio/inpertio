package org.inpertio.server.git.config

import tech.harmonysoft.oss.inpertio.client.ConfigProvider

interface RemoteGitParametersConfigProvider : ConfigProvider<RemoteGitParameters>

data class RemoteGitParameters(val uri: String)