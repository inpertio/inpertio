package org.inpertio.server.resource.service.impl

import org.inpertio.server.git.service.GitService
import org.inpertio.server.resource.service.ResourceService
import org.inpertio.server.util.ProcessingResult
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream

@Component
class ResourceServiceImpl(
    private val gitService: GitService
) : ResourceService {

    override fun getResource(branch: String, resourcePath: String): ProcessingResult<InputStream, String> {
        return gitService.withBranch(branch) { _, rootDir ->
            val resource = File(rootDir, resourcePath)
            if (resource.isFile) {
                ProcessingResult.success(resource.inputStream())
            } else {
                ProcessingResult.failure("no resource at path '$resourcePath' is found in branch '$branch'")
            }
        } ?: ProcessingResult.failure("unknown branch '$branch'")
    }
}