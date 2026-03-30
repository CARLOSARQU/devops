pipeline {
    agent any

    parameters {
        choice(
            name: 'TEST_TAGS',
            choices: ['@smoke', '@regression', '@login_test', '@login_mainframe'],
            description: 'Tags de Cucumber a ejecutar'
        )
        string(
            name: 'ENV',
            defaultValue: 'cert',
            description: 'Entorno de ejecución (cert / dev)'
        )
    }

    environment {
        JAVA_HOME  = 'C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.10.7-hotspot'
        MAVEN_HOME = 'C:\\maven\\apache-maven-3.9.12'
        PATH       = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                echo "Rama: ${env.GIT_BRANCH}"
            }
        }

        stage('Verificar dispositivo') {
            steps {
                script {
                    def devices = bat(script: 'adb devices', returnStdout: true).trim()
                    echo "Dispositivos conectados:\n${devices}"

                    if (!devices.contains('device')) {
                        error('No hay dispositivos Android conectados. Conecta el dispositivo e intenta de nuevo.')
                    }
                }
            }
        }

        stage('Ejecutar tests') {
            steps {
                script {
                    bat """
                        mvn test ^
                        -Denv=${params.ENV} ^
                        -Dcucumber.filter.tags="${params.TEST_TAGS}" ^
                        -Dallure.results.directory=target/allure-results
                    """
                }
            }
        }
    }

    post {
        always {
            allure([
                includeProperties: false,
                jdk: '',
                results: [[path: 'target/allure-results']]
            ])
        }
        success {
            echo "Tests completados correctamente."
        }
        failure {
            echo "Uno o más tests fallaron. Revisa el reporte Allure."
        }
    }
}
