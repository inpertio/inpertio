package org.inpertio.cucumber.glue

import io.cucumber.java.Before
import io.cucumber.java.en.Given
import org.eclipse.jgit.api.Git
import org.inpertio.server.git.config.RemoteGitParametersConfigProvider
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

class GitStepDefinitions {

    @Autowired private lateinit var remoteGitParametersConfigProvider: RemoteGitParametersConfigProvider

    private lateinit var remoteRepo: Git

    @Before
    fun createRemoteRepo() {
        val repoDir = File(remoteGitParametersConfigProvider.data.uri.substring("file://".length))
        remoteRepo = Git.init().setDirectory(repoDir).call()

        // Init HEAD
        val tmpFile = File(repoDir, "initial.txt")
        tmpFile.writeText("initial")
        remoteRepo.add().addFilepattern(tmpFile.name).call()
        remoteRepo.commit().setMessage("initial").call()
    }

    @Given("^remote repo has file ([^\\s]+) in branch ([^\\s]+) with the following content:$")
    fun addFileToRemoteRepo(path: String, branch: String, content: String) {
        ensureBranchExists(branch)
        remoteRepo.checkout().setName(branch).call()

        val file = File(remoteRepo.repository.directory.parentFile, path)
        file.parentFile.mkdirs()
        file.writeText(content)
        remoteRepo.add().addFilepattern(path).call()
        remoteRepo.commit().setMessage(path).call()
    }

    private fun ensureBranchExists(branch: String) {
        val branches = remoteRepo.branchList().call()
        val branchExists = branches.any {
            it.name == "refs/heads/$branch"
        }

        if (!branchExists) {
            remoteRepo.branchCreate().setName(branch).call()
        }
    }
}