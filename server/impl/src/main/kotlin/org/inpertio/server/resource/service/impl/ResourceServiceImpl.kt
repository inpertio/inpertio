package org.inpertio.server.resource.service.impl

import org.inpertio.server.git.GitService
import org.inpertio.server.resource.service.ResourceService
import org.inpertio.server.util.ProcessingResult
import org.springframework.stereotype.Component
import java.io.File

@Component
class ResourceServiceImpl(
    private val gitService: GitService
) : ResourceService {

    override fun getResource(branch: String, resourcePath: String): ProcessingResult<ByteArray, String> {
        return gitService.withBranch(branch) { rootDir ->
            val resource = File(rootDir, resourcePath)
            if (resource.isFile) {
                ProcessingResult.success(resource.readBytes())
            } else {
                ProcessingResult.failure("no resource at path '$resourcePath' is found in branch '$branch'")
            }
        } ?: ProcessingResult.failure("unknown branch '$branch'")
    }
}