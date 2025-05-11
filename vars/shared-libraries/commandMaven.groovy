def call(Map args = [:]) {
    def commandSequence = []

    if (args.get('compile', false))          commandSequence << "mvn clean compile"
    if (args.get('test', false))             commandSequence << "mvn test"
    if (args.get('package', false))          commandSequence << "mvn package"
    if (args.get('dependencyResolve', false)) commandSequence << "mvn dependency:resolve"
    if (args.get('checkstyle', false))       commandSequence << "mvn checkstyle:checkstyle"
    if (args.get('findbugs', false))         commandSequence << "mvn findbugs:findbugs"
    if (args.get('cobertura', false))        commandSequence << "mvn cobertura:cobertura"

    commandSequence.each { cmd ->
        echo "Running: ${cmd}"
        sh "${cmd}"
    }
}
