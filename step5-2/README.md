# Databinding with ViewModel and LiveData
## why databinding? 

### Without Databinding
- View : XML layout으로 정의되어 있다.
- UI Controller : ```ViewModel```로부터 받은 data를 가공한다
- ViewModel : ```ViewModel``` 객체는 ```LiveData```를 가지고 있다.

![Screenshot from 2022-01-03 00-54-54](https://user-images.githubusercontent.com/77181865/147881367-849e2ce8-e05c-4ba9-8e07-adc75b31feff.png)
</br>

### With Databinding
UI Controller 없이, ViewModel 객체가 가지고 있는 데이터를 가공하여 직접 View에 전달할 수 있다. 즉 UI Controller가 필요 없다.

![Screenshot from 2022-01-03 00-57-40](https://user-images.githubusercontent.com/77181865/147881421-ddceaac8-5ab4-49ef-b5ca-34f6c1ccf179.png)
</br>

## DataBinding

### 1. build.gradle(Module)에 databinding 추가

```Kotlin    
    android{
              ...
              dataBinding{
                   enabled = true 
              }
           }
```

### 2. xml 수정

```Kotlin
    <layout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto">
            
            <data>
                <variable
                    name = "gameViewModel"
                    type = "com.example.android.guesstheword.screens.game.GameViewModel"/>       //ViewModel 
            </data>
            
                <LinearLayout
                     ...
                </LinearLayout>
    </layout>
```

### 3. Fragment or Activity 내 onCreateView() 에서 binding 하기
```Kotlin
// DataBindingUtil을 통해 fragment와 View 결합
binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        
viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

// Set the viewmodel for databinding - this allows the bound layout access
// to all the data in the ViewModel
binding.gameViewModel = viewModel
```

### 4. xml 내에서 event handling
xml 내에서 @(annotation)을 이용하여 Event handling이 가능하다. 즉 UI Controller(Activity or Fragment) 내부 Event 에 대한 listener는 생략이 가능하다.

```Kotlin
<Button
   android:id="@+id/skip_button"
   ...
   android:onClick="@{() -> gameViewModel.onSkip()}"         //Annotation을 이용하여 ViewModel 내 method 호출
   ... />

```


### 5. xml 내에서 data 

```Kotlin
<TextView
   android:id="@+id/word_text"
   ...
   android:text="@{gameViewModel.word}"
   ... />
```

### 6. lifecycleOwner
Activity or Fragment 내 ```onCreateView()``` 에서 binding variable의 lifecycle owner를 설정해줘야 한다.

```Kotlin
binding.gameViewModel = ...
// Specify the fragment view as the lifecycle owner of the binding.
// This is used so that the binding can observe LiveData updates
binding.lifecycleOwner = viewLifecycleOwner
```

LiveData의 observer 코드를 제거해줘도 된다 -> databinding을 통해 실제 ```LiveData``` 객체를 이용해서 View에 display가 가능하기 때문이다.





