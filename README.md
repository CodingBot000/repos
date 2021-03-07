# TimeTableWork


- Library : 
Koin injection,
MVVM,
Retrofit, RxJava,
LiveData, 
Room DataBase

- Data Layer :
Repository,
DataSource

- DI :
Koin



[기본동작]
처음시작 -> 로컬DB를 뒤져서 가져와서 시간표에 표기합니다.
강의등록 /취소 -> 등록,취소 동작을하면 해당 화면이 자동으로 닫히면서 시간표화면으로 돌아가서 갱신합니다
메모등록/삭제 -> 메모등록은 첫 화면으로 돌아갑니다. 삭제는 리스트에서 삭제버튼을 눌러서 하는것이므로 돌아가지않고 그자리에서 삭제됩니다. 삭제하면 리스트는 순서를 재정렬합니다.

재시작 -> 앞의 동작에서 로컬DB에 저장한것이 있다면 시간표에 표기합니다.

달력 -> API정보에 날짜에대한 개념이 없기때문에 날짜는 오늘날짜 기준으로 시간표에 등록되도록 하였으며 오늘날짜가 아니면 시간표는 비어있도록 합니다. 오늘날짜의 경우 요일 날짜에 표시하도록 하였기때문에 시간표에 없는 토,일일 경우 아무 표시도 나타나지않습니다.
말씀해주신 요건대로 달력 오늘버튼과 양옆 버튼이 동작하도록 하였습니다.

기본적으로 api요청 (등록/삭제) -> 정상인경우면 -> 로컬DB 등록/삭제
를 합니다.
만약 정보가 불일치하다면 (강의가 로컬에는 있는데 원격에는 없음)
이미 삭제된 데이터란 정보를 주기때문에 에러가 나도 로컬의 데이터를 지웁니다.

- 아쉬운 미구현 부분
요건에는 없지만 로컬과 서버의 내용 불일치를 대비해서 시작시 서버의 데이터를 가져와 로컬과 동기화작업이 필요하겠으나
이러면 매번 동기화를 하는것이 적절한지 잘 모르겠어서 일단 요건에 없어서 처리하지않았습니다.



[UI디자인]
아이콘 이미지가 다소 부실합니다
과제화면의 이미지를 스크린샷을찍어서 잘라서 이용했습니다.
또한 나름 노력했습니다만, UI디자인도 좀 당혹스럽습니다
개발자인만큼 양해부탁드립니다.
강의 등록/취소/메모등록요청화면(메모등록화면 바로직전) 은 동일한 레이아웃이라서 하나의 액티비티에서 분기해서 처리했습니다.

등록강의 컬러간 지정된 정의가 없어서 임의로 매번 컬러를 임의변경되도록 하였습니다.


[오픈소스 활용]
https://github.com/tlaabs/TimetableView

커스터마이징이 많이 필요해서 소스파일을 package timetableview 에 소스를 넣어서 사용했습니다.
처음에 그리드로 할까 생각했으나 cell을 span해야하고 5분단위로 쪼개서 그리드셀을 만들어 span한다 쳐도
모두 높낮이가 별개로 나타날 것이기 때문에 제어가 녹록치않을것 같았습니다
StaggeredLayout도 생각해봐쓴데 정확한 cell 높이 재는데 많은 시행착오가 있을것 같았고,
무엇보다 실패시 다시 재시도할 시간이 없어서 오픈소스를 찾아보았습니다.


[요건불명확에 대한 임의 개발]
강의 취소 등은 api만 있고 어디에도 버튼등 설명이 없어서 이런부분들에 대해 임의로 UI를 구성해서 처리하였습니다

[화면 컴포넌트]
프래그먼트를 쓸지, 메인외에 모두 다이얼로그로 할지 고민하다가 그냥 액티비티로 하는것이 관리,분리차원에서 좋을것이라 판단하여 모두 액티비티를 이용하였습니다

[강의등록 등 이후 Main인 TimeTable로 자동으로 돌아갈때 OnActivityForResult를 사용하지않고 intent flag SINGLE_TOP | CLEAR_TOP  활용이유]
액티비티 스택이 여러 액티비티가 쌓였을때 처음으로 돌아가기위해 여러가지 방법이있으나,
OnActivirtyForResult를 사용하면 연쇄종료를 시켜야하는데 매우 유지보수에 안좋다고 판단하였습니다.

[시간표 리프레쉬 처리]
변경 이벤트가 발생하였을때 (강의등록/취소, 매모 등록/삭제 등)만 리프레쉬를 처리하기위해 이벤트를 보내서 메인에서 이벤트 수신시만 리로드시켰습니다
이벤트는 어디서나 쉽게 보내고 받을수있어야 해서 옛날에는 브로드캐스트리시버를 썼지만 좀더 심플한 RxEventBus를 활용하였습니다

[시간표 글씨크기]
화면크기상 UI에서 요청한 정보를 다 넣으려면 (메모 추가시 메모도)  글자크기를 제가 작성한 크기로 해야하는데,, 그래서 너무 작아서 보기 힘든점 양해부탁드립니다.

[메모 API의 모호함]
메모를 등록할때 메모타입도 설정해야하며 특히 메모타입과 강의코드 등 몇가지 조합이 유니크키로 동작하여 같으면 추가 메모 등록이 안됩니다. 그런데 UI상 메모타입설정(HOME, STUDY…) 화면도 없고 설명도 없으며 오직 API의 response와 실제 충돌이일어나 돌아온 에러 메시지를 통해서 알아내서 임의로 유아이를 구성하였습니다.
즉, 테스트하실때 동일한 타입으로 등록하시면 동록안됩니다. (서버에서 거부)

[메모추가화면 설명 상이 문제]
[메모추가화면이 제일처음에 전체 구조 설명한부분과 그 아래쪽에 메모화면 크게 설명한부분이 서로 UI가 상이합니다.. 하지만 애당초 위에 언급한 타입설정메뉴도 없었던 상황이므로  (설정안하면 등록조차안됨) 처음에 나온 UI를 따라갔습니다.

[강의 취소 안되는 케이스]
하나의 강의코드가 두개의 요일을 가진경우 해당 강의를 등록하면 두개의 요일에 동시에 등록되도록 했습니다.
그런데 이중 하나를 강의 취소하고 나머지 하나를 마저 취소하려고 하면 아래처럼 에러가 발생합니다
정확히 뭐가 문제인지 알려주지않아 해당부분 처리가 안되었습니다.
{"message":"강의 코드 삭제 에러 : ConditionalCheckFailedException: The conditional request failed"}%    



