# Typescript openapi-generator

#### openapi-generator-cli 설치
```shell
npm install @openapitools/openapi-generator-cli -g
npm init
openapi-generator-cli version
```

#### 생성 스크립트
```shell
openapi-generator-cli generate -g typescript-axios -i ./openapi-spec/pet-store.json -c ./openapi.json -o ./output
```
```text
-g : generator를 설정하는 옵션이고 typescript-axios를 선택하였다.
-i : input을 의미하며, yaml/json 파일의 위치를 지정한다
-o : output을 의미하며, 코드를 생성할 위치를 지정한다.
-c : generator 설정파일의 위치를 의미한다.
-t : 커스텀 template 설정파일의 위치를 의미한다.
```

#### 템플릿 커스텀 하기
```shell
# 스크립트를 실행하면, mustaches 폴더에 template 파일들이 생성된다.
npm run template
```
```shell
# 파일을 이용하여 생성
openapi-generator-cli generate -g typescript-axios -i openapi-spec/ricky-oas.json -o ./generate -t ./mustaches -c ./openapi.json
# url 이용하여 생성
openapi-generator-cli generate -g typescript-axios -i http://localhost:9200/v3/api-docs/1_all -o ./generate -t ./mustaches -c ./openapi.json
```