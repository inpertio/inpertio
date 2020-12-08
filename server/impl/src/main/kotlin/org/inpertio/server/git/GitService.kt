package org.inpertio.server.git

import java.io.File

interface GitService {

    /**
     * Executes given action against the target git branch's file system root.
     *
     * @return  given action's call result if target branch is found; `null` if no branch is found
     */
    fun <T> withBranch(branch: String, action: (File) -> T): T?
}