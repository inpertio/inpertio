package org.inpertio.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class InpertioServerApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(InpertioServerApplication::class.java).run(*args)
        }
    }
}