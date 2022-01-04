# LiveData transformation
## Transformations.map()
다음 method는 ```LiveData``` source에 데이터 조작을 수행하고 ```LiveData``` 객체의 결과값을 return한다. 이러한 Transformation은 return된 ```LiveData``` 객체가 관찰되지 않으면 계산되지 않는다.

```Kotlin
val newResult = Transformations.map(someLiveData) { input ->
    // Do some transformation on the input live data
    // and return the new value
}
```

```in layout```

```Kotlin
<data>
    <variable
        name = "MyViewModel"
        type = "com.example.android.something.MyViewModel"/>
</data>


android:text = "@{SomeViewModel.newResult}"
```