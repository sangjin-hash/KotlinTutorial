# LiveData and LiveData observers
</br> 

## LiveData
- ```LiveData``` is observable -> ```LiveData``` 객체가 가지고 있는 data가 변할 때 ```observer```가 notify
- ```LiveData``` holds data -> ```LiveData``` is a wrapper that can be used with any data
- ```LiveData``` is lifecycle-aware. ```LiveData```에 ```observer```를 추가하면, ```observer```는 ```LifecycleOwner```(보통 Activity or Fragment)와 결합이 된다. LiveData는 lifecycle 상태가 ```STATED``` or ```RESUMED```와 같이 active할 때 update한다.
</br>

### LiveData 사용의 이점
- UI와 데이터 상태의 일치 보장
- 메모리 누수 x 
- 중지된 활동으로 인한 비정상 종료 x
- 수명 주기를 더 이상 수동으로 처리하지 않음
- 최신 데이터 유지 
- 적절한 구성 변경
- 리소스 공유
</br>

### LiveData 객체 사용
#### 1) LiveData 객체 만들기
특정 유형의 데이터를 보유할 ```LiveData```의 인스턴스를 생성한다. 이 작업은 일반적으로 ```ViewModel``` 클래스 내에서 이뤄진다. 

```Kotlin
class NameViewModel : ViewModel() {
    val currentName : MutableLiveData<String> by lazy {         
        MutableLiveData<String>()     
    }
}
```

#### 2) LiveData 객체 관찰
```onChanged()``` 메서드를 정의하는 ```Observer``` 객체를 만든다. 이 메서드는 ```LiveData``` 객체가 보유한 데이터 변경 시 발생하는 작업을 제어한다. 일반적으로 Activity 나 Fragment 같은 UI-Controller에 ```Observer``` 객체를 만든다
대부분의 경우 앱 구성요소의 ```onCreate()``` 메서드에서 ```LiveData``` 객체 관찰을 하기 적합하다. ```observer()``` 메서드를 사용하여 ```LiveData``` 객체에 ```Observer``` 객체를 연결한다. 
```observe()``` 메서드는 ```LifecycleOwner``` 객체를 사용하는데 이 때 ```Observer``` 객체가 ```LiveData``` 객체를 구독하여 변경사항에 대한 알림을 받는다.


```Kotlin
class NameActivity : AppCompatActivity(){
    private val model : NameViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        ...
        // Create the observer which updates the UI
        val nameObserver = Observer<String>{ newName ->
            // Update the UI, in this case, a TextView
            nameTextView.text = newName
        }
        
        model.currentName.observe(this, nameObserver)
    }
}
```

#### 3) LiveData 객체 Update
```MutableLiveData``` 클래스는 ```setValue(T)``` 및 ```postValue(T)``` 메서드를 public으로 노출하며 ```LiveData``` 객체에 저장된 값을 수정하려면 이렇나 메서드를 사용해야 한다.
일반적으로 ```MutableLiveData```는 ```ViewModel```에서 사용되며 ```ViewModel```은 변경이 불가능한 ```LiveData``` 객체만 관찰자에게 노출한다.

다음 예시는 관찰자 관계를 설정한 후에 사용자가 버튼을 탭할 때, 모든 관찰자가 trigger하는 ```LiveData``` 객체의 값을 update하는 예시이다.

```Kotlin
button.setOnClickListener{
    val anotherName = "John Doe"
    model.currentName.setValue(anotherName)    //setValue(anotherName) -> onChanged() 호출
}
```
</br>

## LiveData Observer
### Why use viewLifecycleOwner ? 
Fragment views get destroyed when a user navigates away from a fragment, even though the fragment itself is not destroyed. This essentially creates two lifecycles, 
the lifecycle of the fragment, and the lifecycle of the fragment's view. Referring to the fragment's lifecycle instead of the fragment view's lifecycle can cause subtle bugs when updating the fragment's view. 


### 본 프로젝트에서 observer 넣기 예제

```Kotlin
/** Setting up LiveData observation relationship **/
viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
   binding.scoreText.text = newScore.toString()
})

/** Setting up LiveData observation relationship **/
viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
   binding.wordText.text = newWord
})
```

### Encapsulation the LiveData

#### MutableLiveData vs LiveData
- ```MutableLiveData``` 객체에 있는 data는 변경이 가능하다.
- ```LiveData``` 객체에 있는 data는 읽기만 가능하고 변경이 불가능하다.

따라서 MutableLiveData를 private 으로 선언하면 App 내부 class에서만 접근이 가능하고, LiveData는 수정이 불가능 하기 때문에 외부에 공개를 해도 그 해당 값을 읽어오는 것만 가능하다. 그러므로
Encapsulation 하기 위해서는 다음과 같이 Kotlin의 ```backing property``` strategy를 사용해야 한다.

```Kotlin
private val _score = MutableLiveData<Int>()
val score: LiveData<Int>
    get() = _score
    
private val _word = MutableLiveData<String>()
val word: LiveData<String>
    get() = _word
```

### Observer pattern
Observer pattern은 ***observer*** 와 ***observable*** 두 객체간 communicaiton을 specify한다. 보통 ***observable***은 ```LiveData``` 객체이고, ***observer***는 UI-Controller(Activity or Fragment)이다.
State 변화는 ```LiveData```로 감싸진 데이터가 변할 때 일어나는데, ```ViewModel```에서 fragment로 communication 하는데 ```LiveData``` 클래스는 중요한 역할을 한다.
즉, ```LiveData```는 observer에게 update를 전달한다.

![Screenshot from 2022-01-02 23-35-20](https://user-images.githubusercontent.com/77181865/147879169-cb5b1c16-7603-4678-8d3f-3f105e459f8d.png)
</br>






