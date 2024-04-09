folder('Tools') { // On crée un dossier Tools
    description('Folder for miscellaneous tools.')
}

job('Tools/clone-repository') { // Tache pour cloner un repository
    description('Job to clone a repository')
    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }
    wrappers {
        preBuildCleanup { 
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('git clone $GIT_REPOSITORY_URL')
    }
}

job('Tools/SEED') { // Tache pour créer d'autres taches (job) à partir d'un repository qui contient un Makefile
    description('Seed job to create other jobs')
    parameters {
        stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "EpitechIT31000/chocolatine")')
        stringParam('DISPLAY_NAME', '', 'Display name for the job')
    }
    steps {
        dsl {
            scriptText = '''
                        job(DISPLAY_NAME) {
                            scm {
                                git {
                                    remote {
                                        github(GITHUB_NAME)
                                    }
                                }
                            }
                            wrappers {
                                preBuildCleanup {
                                    includePattern('**/target/**')
                                    deleteDirectories()
                                    cleanupParameter('CLEANUP')
                                }
                            }
                            steps {
                                shell('make fclean')
                                shell('make')
                                shell('make tests_run')
                                shell('make clean')
                            }
                            triggers {
                                scm('* * * * *')
                            }
                        }
                        '''
        }
    }
}