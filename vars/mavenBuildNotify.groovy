// vars/mavenBuildNotify.groovy
def call(Map config = [:]) {
    // Input parameters with defaults
    def repoUrl = config.repoUrl ?: error("Missing repoUrl")
    def branch = config.branch ?: 'main'
    def mvnCommand = config.mvnCommand ?: 'clean package'
    def notifier = config.notifier ?: 'console' // 'slack', 'telegram', 'console'

    // Pipeline steps
    pipeline {
        agent any
        stages {
            stage('Clone Repository') {
                steps {
                    echo "Cloning ${repoUrl} (branch: ${branch})"
                    git branch: branch, url: repoUrl
                }
            }
            stage('Build with Maven') {
                steps {
                    echo "Running Maven Command: mvn ${mvnCommand}"
                    sh "mvn ${mvnCommand}"
                }
            }
        }
        post {
            success {
                script {
                    sendNotification(notifier, repoUrl, branch, mvnCommand, 'SUCCESS')
                }
            }
            failure {
                script {
                    sendNotification(notifier, repoUrl, branch, mvnCommand, 'FAILURE')
                }
            }
        }
    }
}

// Internal function
def sendNotification(String type, String repo, String branch, String command, String status) {
    def msg = """
    *Maven Build Notification*
    • *Repository:* ${repo}
    • *Branch:* ${branch}
    • *Command:* mvn ${command}
    • *Status:* ${status}
    """.stripIndent().trim()

    if (type == 'slack') {
        slackSend(channel: '#builds', message: msg, color: status == 'SUCCESS' ? 'good' : 'danger')
    } else if (type == 'telegram') {
        // Simulated - requires external plugin or script
        echo "[Telegram] ${msg}"
    } else {
        echo "[Console Notification] ${msg}"
    }
}
