package org.inpertio.server.resource.web.v1

import org.inpertio.server.resource.service.ResourceService
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.AntPathMatcher
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/resource/v1")
class ResourceControllerV1(
    private val service: ResourceService,
    private val logger: Logger
) {

    @RequestMapping("/{branch}/**")
    fun getResource(
        @PathVariable branch: String,
        request: HttpServletRequest
    ): ResponseEntity<ByteArray> {
        val requestPath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
        val controllerPath = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString()
        val resourcePath = ANT_MATCHER.extractPathWithinPattern(controllerPath, requestPath)
        val result = service.getResource(branch, resourcePath)
        if (logger.isDebugEnabled) {
            logger.debug("Got the following result on attempt to get resource '{}' in branch '{}': {}",
                         branch, resourcePath, resourcePath)
        }
        return if (result.success) {
            ResponseEntity.ok(result.successValue)
        } else {
            ResponseEntity(result.failureValue.toByteArray(), HttpStatus.BAD_REQUEST)
        }
    }

    companion object {
        private val ANT_MATCHER = AntPathMatcher()
    }
}