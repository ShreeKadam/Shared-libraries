def call(Map config = [:]) {
    // Input parameters with defaults
    def repoUrl = config.repoUrl ?: error("Missing repoUrl")
    def branch = config.branch ?: 'main'
    def mvnCommand = config.mvnCommand ?: 'clean package'
    def notifier = config.notifier ?: 'console' // 'slack', 'telegram', 'console'
