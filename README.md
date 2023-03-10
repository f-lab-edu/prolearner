# prolearner

## 앱 설명
### 앱 개발의 목적
개발자 서적과 같은 전문 서적의 학습률 및 진도 관리를 할 수 있는 앱 개발
### 주요 기능
1. 책을 검색하는 기능
2. 책을 저장하고 학습률을 체크하는 기능
3. 책의 목차를 가져와서 저장 및 편집
### 앱 UI구성
피그마 ☞ [바로가기](https://www.figma.com/file/6P7HxybYPkrshwuw7uzDYN/ProLearner?node-id=0%3A1&t=F2BcA4SDd22Hv7bE-1)

### 앱 Demo 시연 영상
Youtube ☞ [바로가기](https://youtu.be/a_8SJtDCWZU)

### 추후 넣고 싶은 기능
1. 미션을 부여해서 뱃지를 받을 수 있는 기능
2. 책의 내용을 간단하게 테스트 할 수 있는 기능(링크드인의 기술 테스트와 비슷한 기능)
3. 유사한 책 큐레이션이 가능하도록 하는 기능

## 필요 사용 기술
Room, MVVM 패턴, DI(Hilt), Navigation, ViewBinding, Firebase, Gitflow

## 사용 라이브러리
1. [BookContentParser](https://github.com/ejjang2030/BookContentParser)(자체제작) : 네이버와 카카오 Search API를 사용하여 책의 목차를 파싱하는 라이브러리
