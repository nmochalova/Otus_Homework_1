timeout(180) {
    node('maven-slave') {
        timestamps {
            wrap([$class: 'BuildUser']) {
                ownerInfo = """<b>Owner:</b> ${env.BUILD_USER}"""
                currentBuild.description = ownerInfo
            }
            stage('Checkout') {
                checkout([
                        $class: 'GitSCM',
                        branches: [[name: "refc/head/$REFSPEC"]],
                        doGenerateSubmoduleConfiguration: false,
                        submoduleCfg: [],
                        userRemoteConfigs: [[credentialsId: 'jenkins', url: 'https://github.com/nmochalova/Otus_Homework_JavaTests.git']]
                ])
            }
            stage('Running tests') {
                sh "mvn test -Dbase.url=${BASE_URL} -Dbrowser.name=${BROWSER_NAME} -Dbrowser.version=${BROWSER_VERSION}"
            }
            stage('Publisher allure report') {
                allure([
                        includeProperties: false,
                        jdk              : '',
                        properties       : [],
                        reportBuildPolicy: 'ALWAYS',
                        result           : [[path: 'allure-results']]
                ])
            }
            stage('Telegram notify') {
                shell(
                def bot_token = "5952779011:AAGHSuT02UfrsFarMFdsO8mKtD1SsqO2EO0"
                def chat_id = "-1001844362560"

                def message = "++++++++ UI tests report ++++++++\n"
                message += "Build URL: ${env.JOB_URL}\n"
                message += "Report: \n"

                def report = readFile "${env.WORKSPACE}/allure-results/export/influxDb.txt"
                if (report =~ /.*Failed.*?\s+[1-9]+/) {
                    status = " *FAILED*"
                }

                for (def status : ["Passed", "Failed", "Skipped"]) {
                    def matcher = report =~ /.*\$\{status}.*=[1-9]+/
                    if (matcher.find()) {
                        message += "${status.toUpperCase()}: ${matcher[0][1]}"
                    }
                }

                def url = "https://api.telegram.org/bot${bot_token}/sendMessage?chat_id=${chat_id}&text=${message}"

                if ("${HTTP_PROXY}" != "") {
                    def proxyUrl = new URL("${HTTP_PROXY}")
                    def proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(), proxyUrl.getPort()))
                    urlConnection = new URL(url).openConnection(proxy) as HttpURLConnection
                } else {
                    urlConnection = new URL(url).openConnection() as HttpURLConnection
                }

                urlConnection.setRequestMethod('GET')
                urlConnection.setDoOutput(true)

                def is = urlConnection.getInputStream() as InputStream
                echo is.getText('utf-8')
                )
            }
        }
    }
}