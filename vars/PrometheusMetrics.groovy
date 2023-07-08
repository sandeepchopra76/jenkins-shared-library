// File: vars/PrometheusMetrics.groovy

def call() {
    stage('Create Prometheus Metrics') {
        script {
            def jobName = env.JOB_NAME.replace('/', '_')
            def buildId = env.BUILD_ID
            def stageName = env.STAGE_NAME
            def status = currentBuild.result == 'SUCCESS' ? 1 : 0

            def metric = """
                # TYPE build_stage_status gauge
                build_stage_status{job="${jobName}", build="${buildId}", stage="${stageName}"} ${status}
            """

            writeFile(file: "${env.WORKSPACE}/prometheus_metrics.txt", text: metric)
        }
    }
}
