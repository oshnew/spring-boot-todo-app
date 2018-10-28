##### GET /api/v1/todo [#11](https://github.com/akageun/spring-boot-todo-app/issues/11)
> 할일을 목록으로 조회해 온다.

###### Parameter

| name | type | 필수 |description | example| 비고 | 
|:-------:|:-------:|:-------|:------:|:------:|:------:|
| pageNumber|Number|Y|페이지 번호 |1|0부터 시작됨.|

###### 실행예시
```
curl -X GET 'http://localhost:9999/api/v1/todo?pageNuber=0'
```

###### 성공응답
```json
{
    "data": {
        "pagination": {
            "pageNumber": 1,
            "numberOfElements": 4,
            "totalElements": 4,
            "pageBlockSize": 10,
            "totalPages": 1,
            "firstBlockPageNo": 1,
            "lastBlockPageNo": 2,
            "firstPageNo": 1,
            "lastPageNo": 0,
            "pageBlockNo": 0,
            "preBlockPageNo": 0,
            "nextBlockPageNo": 10
        },
        "resultList": [
            {
                "todoId": 2,
                "content": "빨래",
                "statusCd": "NOT_YET",
                "createdAt": "2018-10-27 23:25:19",
                "updatedAt": "2018-10-27 23:25:19",
                "todoRefs": [
                    {
                        "refId": 1,
                        "parentTodoId": 2,
                        "refTodoId": 1,
                        "createdAt": "2018-10-27 23:25:19",
                        "updatedAt": "2018-10-27 23:25:19",
                        "todoRefsInfo": {
                            "todoId": 1,
                            "content": "집안일",
                            "statusCd": "NOT_YET",
                            "createdAt": "2018-10-27 23:25:19",
                            "updatedAt": "2018-10-27 23:25:19"
                        }
                    }
                ]
            },
            {
                "todoId": 1,
                "content": "집안일",
                "statusCd": "NOT_YET",
                "createdAt": "2018-10-27 23:25:19",
                "updatedAt": "2018-10-27 23:25:19",
                "todoRefs": []
            }
        ]
    },
    "msg": "성공했습니다."
}
```

##### GET /api/v1/todo/{todoId} [#10](https://github.com/akageun/spring-boot-todo-app/issues/10)
> 할일을 단건으로 조회해 온다.

###### Parameter

| name | type | 필수 |description | example| 비고 | 
|:-------:|:-------:|:-------|:------:|:------:|:------:|
| todoId|Number|Y| 할일번호 |1||

###### 실행예시
```
curl -X GET 'http://localhost:9999/api/v1/todo/1'
```

###### 성공응답
```json
{
    "data": {
        "todoId": 1,
        "content": "집안일",
        "statusCd": "NOT_YET",
        "createdAt": "2018-10-27 23:25:19",
        "updatedAt": "2018-10-27 23:25:19",
        "todoRefs": []
    },
    "msg": "성공했습니다."
}
```

##### GET /api/v1/todo/search [#16](https://github.com/akageun/spring-boot-todo-app/issues/16)
> 할일을 검색해 온다.

###### Parameter

| name | type | 필수 |description | example| 비고 | 
|:-------:|:-------:|:-------|:------:|:------:|:------:|
| keyword|String|Y| 검색어 | 청소 |최소 2글자 이상 1024자 이하|

###### 실행예시
```
curl -X GET 'http://localhost:9999/api/v1/todo/search?keyword=집안일'
```

###### 성공응답
```json
{
    "data": [
        {
            "todoId": 1,
            "content": "집안일",
            "statusCd": "NOT_YET",
            "createdAt": "2018-10-27 23:25:19",
            "updatedAt": "2018-10-27 23:25:19",
            "todoRefs": []
        }
    ],
    "msg": "성공했습니다."
}
```

##### POST /api/v1/todo [#8](https://github.com/akageun/spring-boot-todo-app/issues/8)
> 신규로 할일을 등록한다.

######  Parameter

| name | type | 필수 |description | example| 비고 | 
|:-------:|:-------:|:-------|:------:|:------:|:------:|
| content | String |Y| 할일 | 화장실 청소 |최소 2글자 이상 1024자 이하|
| statusCd | String |Y| 상태값 | COMPLETE | 지정된 상태값만 사용가능(NOT_YET, COMPLETE)|
| refTodos | Number Array|Y|참조 할일값 | 1,2,3 | 참조할 todoId|

###### 실행 예시
```
curl -X POST http://localhost:9999/api/v1/todo
  -d 'content=TEST&statusCd=NOT_YET&refTodos=1%2C2%2C3%2C4'
  -H 'content-type: application/x-www-form-urlencoded'
```

##### PUT /api/v1/todo/{todoId} [#9](https://github.com/akageun/spring-boot-todo-app/issues/9)
> 등록된 글을 수정한다.

######  Parameter

| name | type | 필수 |description | example| 비고 | 
|:-------:|:-------:|:-------|:------:|:------:|:------:|
| todoId | Number |Y| 할일번호 | 1 ||
| content | String |Y| 할일 | 화장실 청소 |최소 2글자 이상 1024자 이하|
| statusCd | String |Y| 상태값 | COMPLETE | 지정된 상태값만 사용가능(NOT_YET, COMPLETE)|
| refTodos | Number Array|Y|참조 할일값 | 1,2,3 | 참조할 todoId|


###### 실행 예시
```
curl -X PUT http://localhost:9999/api/v1/todo/1
  -H 'content-type: application/json' 
  -d '{
	"todoId":1,
	"content":"집안일 수정"
}'
```

###### 성공응답
```json
{
    "data": {
        "todoId": 1,
        "content": "집안일 수정",
        "statusCd": "NOT_YET",
        "createdAt": null,
        "updatedAt": "2018-10-27 23:19:49",
        "todoRefs": null
    },
    "msg": "성공했습니다."
}
```

##### PATCH /api/v1/todo/{todoId} [#12](https://github.com/akageun/spring-boot-todo-app/issues/12)
> 등록된 할일을 완료처리한다.

###### Parameter

| name | type | 필수 |description | example| 비고 | 
|:-------:|:-------:|:-------|:------:|:------:|:------:|
| todoId | Number |Y| 할일번호 | 1 ||
| content | String |Y| 할일 | 화장실 청소 |최소 2글자 이상 1024자 이하|

###### 실행 예시
```
curl -X PATCH http://localhost:9999/api/v1/todo/1 
  -H 'content-type: application/json' 
  -d '{
	"todoId":1,
	"statusCd":"COMPLETE"
}'
```

###### 성공응답
```json
{
    "data": null,
    "msg": "성공했습니다."
}
```
