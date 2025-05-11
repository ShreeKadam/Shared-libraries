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
