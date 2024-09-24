## 생성 관련 속성
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
| delegatePattern | 서버 파일을 위임 패턴을 사용하여 생성할지 여부입니다. | false |
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