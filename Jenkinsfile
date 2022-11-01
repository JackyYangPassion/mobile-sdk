pipeline {
  agent {
    // Run on a build agent test where we have the Android SDK installed
    label 'Android'
  }
  options {
    // Stop the build early in case of compile or test failures
    skipStagesAfterUnstable()
  }
  stages {
    stage('build') {
      steps {
        sh './gradlew clean assembleStageDebug'
        archiveArtifacts '**/*.apk'
      }
    }
    stage('unit-test') {
      when {
        expression {
          currentBuild.result == null || currentBuild.result == 'SUCCESS'
        }
      }
      steps {
        sh './gradlew clean createUnitTestReport'
        //archiveArtifacts(artifacts: '**/detekt-*.xml', fingerprint: true)
      }
    }
    stage('kotlin-code-verify') {
      steps {
        sh './gradlew detekt'
        //androidLint pattern: '**/detekt-*.xml'
      }
    }
  }
  post {
    failure {
      slackSend (color: '#FF0000', message: "Oops! Build FAILED : '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
      mail to: 'manidinesh89@gmail.com,sagar.vadaria90@gmail.com,tushar.tayal19@gmail.com,vikoo.vikoo90@gmail.com', subject: 'Oops!', body: "Build ${env.BUILD_NUMBER} failed; ${env.BUILD_URL}"
    }
    success {
      slackSend (color: '#00FF00', message: "Hooray! Build SUCCESSFUL : '${env.JOB_NAME} [${env.BUILD_NUMBER}]' ${env.BUILD_URL}")
      mail to: 'manidinesh89@gmail.com,sagar.vadaria90@gmail.com,tushar.tayal19@gmail.com,vikoo.vikoo90@gmail.com', subject: 'Hooray!', body: "Build ${env.BUILD_NUMBER} success; ${env.BUILD_URL}"
     }
  }
}
