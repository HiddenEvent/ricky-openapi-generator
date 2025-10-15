## 실행 방법
1. [openapi.gradle.kts](gradle/openapi.gradle.kts) 파일에 swagger 문서의 api-docs url 경로를 설정한다.
2. gradle task - other - [generateOpenAPI] Task 를 실행한다.
```shell
 .\gradlew clean generateOpenAPI --stacktrace
```
3. [ricky-generator](build/ricky-generator) 디렉터리에 생성된 코드를 확인한다.
   - [1. Entity](build/ricky-generator/storage/entity) - JPA Entity
   - [2. Repository](build/ricky-generator/storage/store/repository) JpaRepository
   - [3. Sample](build/ricky-generator/spec/sample) - sample 데이터 생성
   - [4. Store](build/ricky-generator/aggregate/store) - 저장로직 인터페이스 구현
   - [5. JpaStore](build/ricky-generator/storage/store) - JPA 쿼리 로직
   - [6. Service](build/ricky-generator/aggregate/service) - 서비스 로직
   - [7. Fixture](build/ricky-generator/test/fixture) - 테스트 데이터 생성 로직
   - [8. IT](build/ricky-generator/test) - 통합테스트 코드

## custom template 수정
- 템플릿 파일 위치 : [templates](src/main/resources/templates) 

### [build.gradle.kts](build.gradle.kts) [openApiGenerate] task 속성 정리
| 키 | 데이터 타입 | 기본값 | 설명 |
| --- | --- | --- | --- |
| verbose | Boolean | false | 생성의 상세도 |
| validateSpec | Boolean | true | 생성 전에 입력 사양을 검증할지 여부. 잘못된 사양은 오류를 발생시킴. |
| generatorName | String | None | 코드 생성을 처리할 생성기의 이름. |
| outputDir | String | None | 코드가 생성될 출력 대상 디렉토리. |
| inputSpec | String | None | Open API 2.0/3.x 사양 위치. |
| inputSpecRootDirectory | String | None | 사양 파일이 있는 로컬 루트 폴더. |
| mergedFileName | String | None | 모든 병합된 사양을 포함할 파일의 이름. |
| remoteInputSpec | String | None | 원격 Open API 2.0/3.x 사양 URL 위치. |
| templateDir | String | None | 사용자 정의 템플릿을 보유한 템플릿 디렉토리. |
| templateResourcePath | String | None | 리소스 경로를 통한 머스태시 템플릿이 있는 디렉토리. 이 옵션은 templateDir에 정의된 모든 옵션을 덮어씀. |
| auth | String | None | 원격으로 OpenAPI 정의를 가져올 때 인증 헤더를 추가함. name:header의 URL 인코딩된 문자열을 전달하며, 여러 값을 쉼표로 구분함. |
| globalProperties | Map(String,String) | None | 지정된 전역 속성을 설정함. |
| configFile | String | None | JSON 구성 파일의 경로. 구조 세부 사항은 OpenAPI Generator readme를 참조. |
| skipOverwrite | Boolean | false | 생성 중 기존 파일을 덮어쓸지 여부를 지정함. |
| packageName | String | (generator specific) | 생성된 클래스의 패키지 (지원되는 경우). |
| apiPackage | String | (generator specific) | 생성된 API 클래스의 패키지. |
| modelPackage | String | (generator specific) | 생성된 모델 클래스의 패키지. |
| modelNamePrefix | String | None | 모든 모델 이름 앞에 추가될 접두사. |
| modelNameSuffix | String | None | 모든 모델 이름 뒤에 추가될 접미사. |
| apiNameSuffix | String | None | 모든 API 이름 뒤에 추가될 접미사. |
| instantiationTypes | Map(String,String) | None | 인스턴스화 유형 매핑을 설정함. |
| typeMappings | Map(String,String) | None | OpenAPI 사양 유형과 생성된 코드 유형 간의 매핑을 설정함. 형식은 OpenAPIType=generatedType,OpenAPIType=generatedType. 예: array=List,map=Map,string=String. 지정된 형식을 매핑하려면 type+format을 사용함. 예: string+password=EncryptedString은 type: string, format: password를 EncryptedString으로 매핑함. |
| schemaMappings | Map(String,String) | None | 스키마와 새 이름 간의 매핑을 지정함. 형식은 schema_a=Cat,schema_b=Bird. https://openapi-generator.tech/docs/customization/#schema-mapping |
| nameMappings | Map(String,String) | None | 속성 이름과 새 이름 간의 매핑을 지정함. 형식은 property_a=firstProperty,property_b=secondProperty. https://openapi-generator.tech/docs/customization/#name-mapping |
| modelNameMappings | Map(String,String) | None | 모델 이름과 새 이름 간의 매핑을 지정함. 형식은 model_a=FirstModel,property_b=SecondModel. https://openapi-generator.tech/docs/customization/#name-mapping |
| parameterNameMappings | Map(String,String) | None | 매개변수 이름과 새 이름 간의 매핑을 지정함. 형식은 parameter_a=firstParameter,parameter_b=secondParameter. https://openapi-generator.tech/docs/customization/#name-mapping |
| inlineSchemaNameMappings | Map(String,String) | None | 인라인 스키마 이름과 새 이름 간의 매핑을 지정함. 형식은 inline_object_2=Cat,inline_object_5=Bird. |
| inlineSchemaOptions | Map(String,String) | None | 인라인 모델 해결기에서 인라인 스키마를 처리할 때 사용되는 옵션을 지정함. |
| additionalProperties | Map(String,Any) | None | 머스태시 템플릿에서 참조할 수 있는 추가 속성을 설정함. |
| serverVariables | Map(String,String) | None | 서버 URL 템플릿 대체를 위한 서버 변수를 설정함. 형식은 name=value,name=value. 여러 번 발생할 수 있음. |
| languageSpecificPrimitives | List(String) | None | 형식은 type1,type2,type3,type3. 예: String,boolean,Boolean,Double. 추가 언어별 기본 유형을 지정함. |
| importMappings | Map(String,String) | None | 주어진 클래스와 해당 클래스에 사용될 가져오기 간의 매핑을 지정함. |
| invokerPackage | String | None | 생성된 코드의 루트 패키지. |
| groupId | String | None | 생성된 pom.xml/build.gradle 또는 기타 빌드 스크립트의 GroupId. 비-JVM 생성기에서는 언어별 변환이 발생함. |
| id | String | None | 생성된 pom.xml/build.gradle 또는 기타 빌드 스크립트의 ArtifactId. 비-JVM 생성기에서는 언어별 변환이 발생함. |
| version | String | None | 생성된 pom.xml/build.gradle 또는 기타 빌드 스크립트의 아티팩트 버전. 비-JVM 생성기에서는 언어별 변환이 발생함. |
| library | String | None | 생성기의 라이브러리 템플릿(서브 템플릿)을 참조함. |
| gitHost | String | github.com | Git 사용자 ID, 예: gitlab.com. |
| gitUserId | String | None | Git 사용자 ID, 예: openapitools. |
| gitRepoId | String | None | Git 저장소 ID, 예: openapi-generator. |
| releaseNote | String | 'Minor update' | 릴리스 노트. |
| httpUserAgent | String | None | HTTP 사용자 에이전트, 예: codegen_csharp_api_client. 생성기 기본값은 'OpenAPI-Generator/{packageVersion}/{language}'이지만 생성기별로 다를 수 있음. |
| reservedWordsMappings | Map(String,String) | None | 예약된 이름을 어떻게 이스케이프할지 지정함. 그렇지 않으면 기본 <name>이 사용됨. |
| ignoreFileOverride | String | None | .openapi-generator-ignore 파일의 재정의 위치를 지정함. 초기 생성 시 가장 유용함. |
| removeOperationIdPrefix | Boolean | false | operationId의 접두사를 제거함. 예: config_getId ⇒ getId. |
| skipOperationExample | Boolean | false | 작업에 정의된 예제를 건너뜀. |
| apiFilesConstrainedTo | List(String) | None | 생성될 API 관련 파일을 정의함. 이를 통해 생성된 파일의 하위 집합(또는 전혀 없음)을 만들 수 있음. 아래 참고. |
| modelFilesConstrainedTo | List(String) | None | 생성될 모델 관련 파일을 정의함. 이를 통해 생성된 파일의 하위 집합(또는 전혀 없음)을 만들 수 있음. 아래 참고. |
| supportingFilesConstrainedTo | List(String) | None | 생성될 지원 파일을 정의함. 이를 통해 생성된 파일의 하위 집합(또는 전혀 없음)을 만들 수 있음. 아래 참고. |
| generateModelTests | Boolean | true | 모델 관련 테스트 파일을 생성할지 여부를 정의함. |
| generateModelDocumentation | Boolean | true | 모델 관련 문서 파일을 생성할지 여부를 정의함. |
| generateApiTests | Boolean | true | API 관련 테스트 파일을 생성할지 여부를 정의함. |
| generateApiDocumentation | Boolean | true | API 관련 문서 파일을 생성할지 여부를 정의함. |
| configOptions | Map(String,String) | None | 생성기별 옵션의 맵. 생성기별 매개변수의 전체 목록을 보려면 생성기 문서를 참조. 생성기별 문서에서 중복될 수 있는 구성 옵션이 여기에 포함될 수 있으며, 일부 생성기는 configOptions의 형제 옵션을 중복할 수 있음. |
| logToStderr | Boolean | false | 모든 로그 메시지(오류만이 아님)를 STDOUT에 기록함. |
| enablePostProcessFile | Boolean | false | 파일 후처리 훅을 활성화함. 외부 후처리기(보통 린터 프로그램)를 실행할 수 있음. 이 옵션은 후처리기를 활성화함. 후처리 명령을 정의하려면 LANG_POST_PROCESS_FILE과 같은 환경 변수를 정의함 (예: GO_POST_PROCESS_FILE, SCALA_POST_PROCESS_FILE). 대상 생성기가 이 기능을 지원하지 않는 경우 이슈를 열어주세요. |
| skipValidateSpec | Boolean | false | 사양 검증을 건너뜀. true일 경우, 생성 전에 사양을 검증하는 기본 동작을 건너뜀. |
| openapiNormalizer | Map(String,String) | None | OpenAPI 정규화기에서 활성화할 규칙을 RULE_1=true,RULE_2=original 형식으로 지정함. |
| generateAliasAsModel | Boolean | false | 별칭(배열, 목록, 맵)을 모델로 생성함. false일 경우, 배열, 목록, 맵으로 정의된 최상위 객체는 최상위 항목의 배열, 목록, 맵 정의로 생성됨. true일 경우, 배열, 목록, 맵을 포함하거나 확장하는 모델 표현이 생성됨(특정 생성기 구현에 따라 다름). |
| engine | String | mustache | 템플릿 엔진: "mustache" (기본값) 또는 "handlebars" (베타). |
| cleanupOutput | Boolean | false | 출력을 생성하기 전에 출력 디렉토리를 정리할지 여부를 정의함. |
| dryRun | Boolean | false | 생성기가 드라이런 모드에서 실행될지 여부를 정의함. 드라이런 모드에서는 파일이 작성되지 않으며 파일 상태에 대한 요약이 출력됨. |


### openapi-config 속성
https://openapi-generator.tech/docs/generators/kotlin-spring 참고하여 번역

| 속성명 | 설명 | 기본값 |
|---------------------------------|-----------------------------------------------------------------------------------------------|----------------|
| additionalModelTypeAnnotations | 모델 타입에 대한 추가 주석을 지정합니다. 세미콜론(;) 또는 줄 바꿈으로 구분된 목록입니다. | null |
| annotationLibrary | 보조 문서 주석 라이브러리를 선택합니다. | swagger2 |
| apiPackage | 생성된 코드의 API 패키지입니다. | org.openapitools.api |
| apiSuffix | API 클래스의 접미사입니다. | Api |
| artifactId | 생성된 아티팩트의 ID(예: JAR 이름)입니다. | openapi-spring |
| artifactVersion | 생성된 아티팩트의 패키지 버전입니다. | 1.0.0 |
| basePackage | 생성된 코드의 기본 패키지입니다. | org.openapitools |
| beanQualifiers | @Component 및 @RestController 주석에 완전한 클래스 이름을 추가할지 여부입니다. | false |
| configPackage | 생성된 코드의 구성 패키지입니다. | org.openapitools.configuration |
| ~~delegatePattern~~ | 서버 파일을 위임 패턴을 사용하여 생성할지 여부입니다. | false |
| documentationProvider | OpenAPI 문서 제공자를 선택합니다. | springdoc |
| enumPropertyNaming | 열거형 속성의 명명 규칙입니다. | original |
| exceptionHandler | 기본 전역 예외 처리기를 생성할지 여부입니다. | true |
| gradleBuildFile | Kotlin DSL을 사용하여 Gradle 빌드 파일을 생성할지 여부입니다. | true |
| groupId | 생성된 아티팩트 패키지의 조직(예: Maven groupId)입니다. | org.openapitools |
| interfaceOnly | 서버 파일 없이 API 인터페이스 스텁만 생성할지 여부입니다. | false |
| library | 라이브러리 템플릿(서브 템플릿)입니다. | spring-boot |
| modelMutable | 변경 가능한 모델을 생성할지 여부입니다. | false |
| modelPackage | 생성된 코드의 모델 패키지입니다. | org.openapitools.model |
| packageName | 생성된 아티팩트의 패키지 이름입니다. | org.openapitools |
| parcelizeModels | 생성된 모델에 "@Parcelize"를 사용할지 여부입니다. | null |
| reactive | 반응형 동작을 위해 코루틴을 사용할지 여부입니다. | false |
| requestMappingMode | 클래스 수준의 @RequestMapping 주석을 생성할 위치입니다. | controller |
| serializableModel | 생성된 모델에 "implements Serializable"을 추가할지 여부입니다. | null |
| serverPort | 서버가 실행될 포트입니다. | 8080 |
| serviceImplementation | 서비스 인터페이스를 확장하는 스텁 서비스 구현을 생성할지 여부입니다. | false |
| serviceInterface | 컨트롤러와 함께 서비스 인터페이스를 생성할지 여부입니다. | false |
| skipDefaultInterface | 기본 구현 생성을 건너뛸지 여부입니다. | false |
| sortModelPropertiesByRequiredFlag| 모델 속성을 필수 매개변수를 먼저 배치하도록 정렬할지 여부입니다. | null |
| sortParamsByRequiredFlag | 메서드 인수를 필수 매개변수를 먼저 배치하도록 정렬할지 여부입니다. | null |
| sourceFolder | 생성된 코드의 소스 폴더입니다. | src/main/kotlin |
| title | 서버 제목 또는 클라이언트 서비스 이름입니다. | OpenAPI Kotlin Spring |
| useBeanValidation | 데이터 유형을 검증하기 위해 BeanValidation API 주석을 사용할지 여부입니다. | true |
| useFeignClientUrl | URL 매개변수를 가진 Feign 클라이언트를 생성할지 여부입니다. | true |
| useSpringBoot3 | Spring Boot 3.x와 함께 사용할 코드를 생성하고 종속성을 제공할지 여부입니다. | false |
| useSwaggerUI | swagger-ui에서 OpenApi 사양을 열지 여부입니다. | true |
| useTags | 인터페이스 및 컨트롤러 클래스 이름 생성을 위해 태그를 사용할지 여부입니다. | false |

## 템플릿 다운로드
```shell
openapi-generator author template -g kotlin-spring -o ./kotlin-spring-templates
```