// File: vars/PrometheusMetrics.groovy

def call(String stageName, long startTime, long endTime, String buildResult) {
    stage("Prometheus Metrics - ${stageName}") {
        script {
            def jobName = env.JOB_NAME.replace('/', '_')
            def buildId = env.BUILD_ID

            def status = buildResult == 'SUCCESS' ? 1 : 0

            def metric = """
                # TYPE build_stage_duration_seconds gauge
                build_stage_duration_seconds{job="${jobName}", build="${buildId}", stage="${stageName}"} ${endTime - startTime}
                
                # TYPE build_stage_status gauge
                build_stage_status{job="${jobName}", build="${buildId}", stage="${stageName}"} ${status}
            """

            writeFile(file: "${env.WORKSPACE}/prometheus_metrics.txt", text: metric)
        }
    }
}

