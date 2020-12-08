package org.inpertio.server.resource.service

import org.inpertio.server.util.ProcessingResult

interface ResourceService {

    /**
     * Tries finding target resource in the target branch
     */
    fun getResource(branch: String, resourcePath: String): ProcessingResult<ByteArray, String>
}