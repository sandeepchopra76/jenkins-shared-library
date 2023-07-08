// File: vars/PrometheusMetrics.groovy

def call(String stageName, long startTime, long endTime, String buildResult) {
    def jobName = env.JOB_NAME.replace('/', '_')
    def buildId = env.BUILD_ID
    def filePath = "${env.WORKSPACE}/prometheus_metrics.txt"

    def metric = """
        # TYPE build_stage_duration_seconds gauge
        build_stage_duration_seconds{job="${jobName}", build="${buildId}", stage="${stageName}"} ${endTime - startTime}
        
        # TYPE build_stage_status gauge
        build_stage_status{job="${jobName}", build="${buildId}", stage="${stageName}"} ${buildResult}
    """
    
    // Check if the file exists
    def fileExists = fileExists(filePath)

    if (fileExists) {
        // Append the metric to the existing file
        appendFile(file: filePath, text: metric)
    } else {
        // Create a new file and write the metric content
        writeFile(file: filePath, text: metric)
    }
}


