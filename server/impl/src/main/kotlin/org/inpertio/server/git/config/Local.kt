package org.inpertio.server.git.config

import tech.harmonysoft.oss.inpertio.client.ConfigProvider
import java.io.File

interface LocalGitParametersConfigProvider : ConfigProvider<LocalGitParameters>

data class LocalGitParameters(val rootDir: File)