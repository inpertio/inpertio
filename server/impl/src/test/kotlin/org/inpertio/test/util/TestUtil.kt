package org.inpertio.test.util

import org.slf4j.LoggerFactory

object TestUtil {

    private val logger = LoggerFactory.getLogger(TestUtil::class.java)

    fun fail(error: String): Nothing {
        logger.error(error)
        throw AssertionError(error)
    }
}