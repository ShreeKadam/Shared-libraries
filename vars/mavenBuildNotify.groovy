def call(Map config = [:]) {
    def repoUrl  = config.get('repoUrl', '')
    def branch   = config.get('branch', 'main')
    def mavenCmd = config.get('mavenCmd', 'clean install')
    def slackChannel = config.get('slackChannel', '#all-arizona') // default channel
    

    pipeline {
        agent any

        environment {
            REPO_URL = repoUrl
            BRANCH   = branch
            MVN_CMD  = mavenCmd
        }

        options {
            timestamps()
        }

        stages {
            stage('Clone Repository') {
                steps {
                    git branch: BRANCH, url: REPO_URL
                }
            }

            stage("Run Maven Command") {
                steps {
                    sh "mvn ${MVN_CMD}"
                }
            }
        }

        post {
            success {
                slackNotify('SUCCESS', slackChannel)
            }
            failure {
                slackNotify('FAILURE', slackChannel)
            }
            unstable {
                slackNotify('UNSTABLE', slackChannel)
            }
        }
    }
}

def slackNotify(String status, String channel, String color) {
    slackSend(
        channel: channel,
        color: color,
        message: """
*Build ${status}*
• *Repo:* ${env.REPO_URL}
• *Branch:* ${env.BRANCH}
• *Maven Cmd:* mvn ${env.MVN_CMD}
• *Job:* ${env.JOB_NAME}
• *Build URL:* ${env.BUILD_URL}
"""
    )
}

    } else {
        echo "[Console Notification] ${msg}"
    }
}
