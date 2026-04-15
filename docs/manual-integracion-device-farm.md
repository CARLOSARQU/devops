# Integración QA Automation — AWS Device Farm

## Contexto

El repositorio de automatización QA (`qa-automation`) contiene pruebas funcionales sobre la app móvil Android desarrolladas con **Appium + TestNG + Cucumber**.

El objetivo es integrar la ejecución de estas pruebas al final del pipeline existente, una vez que el APK ha sido compilado y publicado en S3.

---

## Prerrequisitos

### En AWS
- Proyecto creado en Device Farm (región `us-west-2`)
- Usuario IAM con política `AmazonDeviceFarmFullAccess`
- Device pool configurado con el dispositivo objetivo

### En Jenkins
- AWS CLI disponible en el agente donde corre el pipeline
- Credenciales AWS configuradas con acceso a Device Farm
- Acceso al repositorio `qa-automation` en GitHub

### Repositorio QA
- `testspec.yml` en la raíz del proyecto — define cómo Device Farm ejecuta los tests
- `mvn clean package -DskipTests` genera `target/zip-with-dependencies.zip`

---

## Flujo completo

```
Pipeline existente                    Stage QA (nuevo)
─────────────────                     ────────────────
Source                                Checkout qa-automation
  ↓                                     ↓
Sonar                                 mvn clean package -DskipTests
  ↓                                     ↓
Prepare                               Subir ZIP → Device Farm
  ↓                                     ↓
Codebuild (compila APK)               Subir testspec.yml → Device Farm
  ↓                                     ↓
Publish (APK → S3)                    Descargar APK desde S3 → Device Farm
                                        ↓
                                      schedule-run (APK + ZIP + testspec)
                                        ↓
                                      Esperar resultado
```

---

## Variables de entorno requeridas

Agregar al bloque `environment` del pipeline:

```groovy
environment {
    // ARN del proyecto Device Farm
    DF_PROJECT_ARN     = 'arn:aws:devicefarm:us-west-2:[ACCOUNT_ID]:project:[PROJECT_ID]'

    // ARN del device pool
    DF_DEVICE_POOL_ARN = 'arn:aws:devicefarm:us-west-2:[ACCOUNT_ID]:devicepool:[PROJECT_ID]/[POOL_ID]'

    // Repositorio QA — repositorio donde están las pruebas de regresión
    QA_REPO_URL        = 'https://github.com/CARLOSARQU/devops.git'
    QA_REPO_BRANCH     = 'main'

    // Región Device Farm (siempre us-west-2)
    AWS_DF_REGION      = 'us-west-2'
}
```

---

## Parámetro TEST_TAGS

Agregar al bloque `parameters` del pipeline para escoger el alcance de las pruebas:

```groovy
parameters {
    choice(
        name: 'TEST_TAGS',
        choices: ['@smoke', '@regression', '@login_test', '@login_mainframe'],
        description: 'Tags de Cucumber a ejecutar en Device Farm'
    )
}
```

---

## Stage a agregar

Insertar después del stage `Publish`:

```groovy
stage('QA Automation - Device Farm') {
    steps {
        script {

            // 1. Recuperar la versión del APK
            // Se invoca nuevamente porque la variable declarada en Publish es local a ese stage
            def versionNum = utilsHandler.getCodeVersionFromYml(versionYmlFile=DEVOPS_VERSION_FILE)
            def apkFileName = "cajalosandes-${versionNum}-qa-debug.apk"

            // 2. Checkout del repositorio QA y compilar tests
            dir('qa-automation') {
                git branch: QA_REPO_BRANCH, url: QA_REPO_URL
                sh 'mvn clean package -DskipTests'
            }

            // 3. Registrar y subir ZIP de tests a Device Farm
            def uploadTestOutput = sh(
                script: """
                    aws devicefarm create-upload \\
                    --project-arn ${DF_PROJECT_ARN} \\
                    --name zip-with-dependencies.zip \\
                    --type APPIUM_JAVA_TESTNG_TEST_PACKAGE \\
                    --region ${AWS_DF_REGION} \\
                    --query "upload.[arn,url]" \\
                    --output text
                """,
                returnStdout: true
            ).trim()

            def (testPackageArn, testPackageUrl) = uploadTestOutput.split('\t')
            if (!testPackageArn || !testPackageUrl) {
                error("Error creando upload del ZIP de tests en Device Farm")
            }
            sh "curl -T qa-automation/target/zip-with-dependencies.zip \"${testPackageUrl}\""

            // 4. Registrar y subir testspec.yml
            def uploadSpecOutput = sh(
                script: """
                    aws devicefarm create-upload \\
                    --project-arn ${DF_PROJECT_ARN} \\
                    --name testspec.yml \\
                    --type APPIUM_JAVA_TESTNG_TEST_SPEC \\
                    --region ${AWS_DF_REGION} \\
                    --query "upload.[arn,url]" \\
                    --output text
                """,
                returnStdout: true
            ).trim()

            def (testSpecArn, testSpecUrl) = uploadSpecOutput.split('\t')
            if (!testSpecArn || !testSpecUrl) {
                error("Error creando upload del testspec.yml en Device Farm")
            }
            sh "curl -T qa-automation/testspec.yml \"${testSpecUrl}\""

            // 5. Descargar el APK desde S3 usando el handler existente
            awsS3Handler.downloadItemLocal(
                targetFile: AWS_BUCKET_DEPLOY + "/" + apkFileName,
                sourceFile: "./app.apk"
            )

            // 6. Registrar APK en Device Farm
            def uploadApkOutput = sh(
                script: """
                    aws devicefarm create-upload \\
                    --project-arn ${DF_PROJECT_ARN} \\
                    --name app.apk \\
                    --type ANDROID_APP \\
                    --region ${AWS_DF_REGION} \\
                    --query "upload.[arn,url]" \\
                    --output text
                """,
                returnStdout: true
            ).trim()

            def (appArn, appUrl) = uploadApkOutput.split('\t')
            if (!appArn || !appUrl) {
                error("Error creando upload del APK en Device Farm")
            }
            sh "curl -T app.apk \"${appUrl}\""

            // 7. Esperar a que Device Farm procese los 3 archivos
            timeout(time: 3, unit: 'MINUTES') {
                waitUntil {
                    def statusTest = sh(script: "aws devicefarm get-upload --arn ${testPackageArn} --query upload.status --output text --region ${AWS_DF_REGION}", returnStdout: true).trim()
                    def statusSpec = sh(script: "aws devicefarm get-upload --arn ${testSpecArn} --query upload.status --output text --region ${AWS_DF_REGION}", returnStdout: true).trim()
                    def statusApk  = sh(script: "aws devicefarm get-upload --arn ${appArn} --query upload.status --output text --region ${AWS_DF_REGION}", returnStdout: true).trim()

                    if (statusTest == 'FAILED' || statusSpec == 'FAILED' || statusApk == 'FAILED') {
                        error("Uno de los uploads falló en Device Farm — Test:${statusTest} Spec:${statusSpec} APK:${statusApk}")
                    }

                    return statusTest == 'SUCCEEDED' && statusSpec == 'SUCCEEDED' && statusApk == 'SUCCEEDED'
                }
            }

            // 8. Lanzar el run en Device Farm
            def runArn = sh(
                script: """
                    aws devicefarm schedule-run \\
                    --project-arn ${DF_PROJECT_ARN} \\
                    --app-arn ${appArn} \\
                    --device-pool-arn ${DF_DEVICE_POOL_ARN} \\
                    --name "qa-run-${env.BUILD_NUMBER}" \\
                    --test '{"type":"APPIUM_JAVA_TESTNG","testPackageArn":"'${testPackageArn}'","testSpecArn":"'${testSpecArn}'"}' \\
                    --configuration '{"jobTimeoutMinutes":30,"environmentVariables":[{"key":"TEST_TAGS","value":"'${params.TEST_TAGS}'"}]}' \\
                    --query run.arn \\
                    --output text \\
                    --region ${AWS_DF_REGION}
                """,
                returnStdout: true
            ).trim()
            
            //para ver el video en caso se active la opcion de grabar pantalla
            //def runUrl = "https://us-west-2.console.aws.amazon.com/devicefarm/home?region=us-west-2#/projects/${DF_PROJECT_ARN.split('/').last()}/runs/${runArn.split('/').last()}"
            //echo "Ver resultados del test en video: ${runUrl}"

            echo "Run lanzado en AWS Device Farm: ${runArn}"

            // 9. Esperar resultado del run
            timeout(time: 20, unit: 'MINUTES') {
                waitUntil {
                    def runStatus = sh(
                        script: "aws devicefarm get-run --arn ${runArn} --query run.status --output text --region ${AWS_DF_REGION}",
                        returnStdout: true
                    ).trim()
                    echo "Estado del run: ${runStatus}"
                    return runStatus.contains('COMPLETED')
                }
            }

            // 10. Verificar resultado final
            def runResult = sh(
                script: "aws devicefarm get-run --arn ${runArn} --query run.result --output text --region ${AWS_DF_REGION}",
                returnStdout: true
            ).trim()

            echo "Resultado: ${runResult}"

            if (runResult.contains('FAILED') || runResult.contains('ERRORED')) {
                unstable("Tests fallaron en Device Farm. Resultado: ${runResult}")
            }
        }
    }
}
```

---

## testspec.yml

El archivo `testspec.yml` en la raíz del repositorio QA define la ejecución en Device Farm:

```yaml
version: 0.1

android_test_host: amazon_linux_2

phases:
  install:
    commands:
      - devicefarm-cli use node 20
      - devicefarm-cli use appium 3 || npm install -g appium@latest
      - appium driver install uiautomator2
      - devicefarm-cli use java 17

  pre_test:
    commands:
      - export CLASSPATH=$CLASSPATH:$DEVICEFARM_TEST_PACKAGE_PATH/*
      - export CLASSPATH=$CLASSPATH:$DEVICEFARM_TEST_PACKAGE_PATH/dependency-jars/*
      - appium --base-path=/wd/hub --log-timestamp --log-no-colors --relaxed-security >> $DEVICEFARM_LOG_DIR/appium.log 2>&1 &
      - appium_t=0; until curl --silent --fail "http://0.0.0.0:4723/wd/hub/status"; do if [[ $appium_t -gt 30 ]]; then exit 1; fi; appium_t=$((appium_t+1)); sleep 1; done

  test:
    commands:
      - cd $DEVICEFARM_TEST_PACKAGE_PATH
      - export TEST_TAGS=${TEST_TAGS:-"@regression"}
      - java -javaagent:$DEVICEFARM_TEST_PACKAGE_PATH/dependency-jars/aspectjweaver-1.9.21.jar -Dallure.results.directory=$DEVICEFARM_TEST_PACKAGE_PATH/target/allure-results -Dappium.screenshots.dir=$DEVICEFARM_SCREENSHOT_PATH -Dcucumber.filter.tags="$TEST_TAGS" -cp $CLASSPATH org.testng.TestNG -testjar *-tests.jar -d $DEVICEFARM_LOG_DIR/test-output -verbose 2

  post_test:
    commands:
      - echo "Tests finalizados"

artifacts:
  - $DEVICEFARM_LOG_DIR
  - $DEVICEFARM_TEST_PACKAGE_PATH/target/allure-results
  - $DEVICEFARM_TEST_PACKAGE_PATH/target/screenshots
```

---

## Notas 

- `versionNum` se recupera nuevamente con `utilsHandler.getCodeVersionFromYml` porque la variable declarada en el stage `Publish` es local a ese scope
- Se reutiliza `awsS3Handler.downloadItemLocal` para descargar el APK desde S3, respetando los handlers existentes
- Las URLs presignadas de Device Farm expiran en **24 horas** — siempre se generan nuevas en cada ejecución
- Device Farm solo opera en `us-west-2` — independiente de la región del resto de la infraestructura
- El `waitUntil` con `timeout` evita que el pipeline quede colgado si Device Farm demora o falla
- El tag por defecto es `@regression` — si no se pasa `TEST_TAGS`, el `testspec.yml` lo asume automáticamente
- Resultados posibles del run: `PASSED`, `FAILED`, `ERRORED`, `SKIPPED`, `STOPPED`
