timeout(180) {
    node('maven-slave') {
        wrap([$class: 'BuildUser']){
            ownerInfo="""<b>Owner:</b> ${env.BUILD_USER}"""
        }
        stage('Checkout'){
            checkout scm
        }
        stage('Running tests'){
            sh "mvn test -Dbase.url=${BASE_URL} -Dbrowser.name=${BROWSER_NAME} -Dbrowser.version=${BROWSER_VERSION}"
        }
        stage('Publisher allure report') {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    result: [[path: 'allure-results']]
            ])
        }
        stage('Telegram notify'){
            shell(
                def message = "++++++++ UI tests report ++++++++\n"
                message += "Build URL: ${env.JOB_URL}"
                message += "Report: \n"

                def report = readFile "${env.WORKSPACE}/allure-results/export/influxDb.txt"
                if(report =~ /.*Failed.*?\s+[1-9]+/) {
                    status = " *FAILED*"
                }

                for(def status: ["Passed", "Failed", "Skipped"]) {
                    def matcher = report =~ /.*\$\{status}.*=[1-9]+/
                    if(matcher.find()) {
                        message += "${status.toUpperCase()}: ${matcher[0][1]}"
                    }
                }

                if("${HTTP_PROXY}" != "") {
                    def proxyUrl = new URL("${HTTP_PROXY}")
                    def proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(),proxyUrl.getPort()))
                    urlConnection = new URL(stringBuilder.toString()).openConnection(proxy) as HttpURLConnection
                } else {
                    urlConnection = new URL(stringBuilder.toString()).openConnection() as HttpURLConnection
                }

                urlConnection.setRequestMethod('GET')
                urlConnection.setDoOutput(true)

                def is = urlConnection.getInputStream() as InputStream
                echo is.getText('utf-8')
            )
        }
    }
}