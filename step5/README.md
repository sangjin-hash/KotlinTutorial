# App Architecture

## App Architecture을 사용하는 이유?

App을 사용할 때 다음 Issue가 발생할 수 있다.

1) The device orientation changes
2) The app shuts down and restart 


위의 Issue들은 save / restore이 되지 않아, 사용자가 입력한 data가 모두 손실될 수 있다. ```onSaveInstance()``` callback을 사용하면 해결할 수 있지만, 이는 ***bundle*** 안에 state를 저장하기 위해 추가적인 code 작성을 해야하고, 해당 state를 retrieve하는 logic을 구현해야 한다. 

#### 따라서 위와 같은 issue를 app architecture를 통해 해결할 수 있다.  
  
</br>

## App Architecture를 이루는 것들

### UI controller

UI controller는 ```Activity``` 혹은 ```Fragment```와 같이 UI-based class이다. UI Controller는 UI를 다루고, 사용자와의 interaction과 같은 operating-system을 구현한 logic이 포함되어 있다. 그러나, display할 text를 결정하는 logic과 같이 의사결정하는 logic을 UI controller에 넣으면 안된다.
</br>
</br>

### ViewModel

```ViewModel``` 은 ```fragment``` 혹은 ```activity``` 안에 display될 data를 가지고 있다. ```ViewModel``` 은 모든 View와 관련된 비즈니스 로직은 이 곳에 들어가게 되며 데이터를 잘 가공해서 View에서 뿌리기 쉬운 Model로 바꾸는 역할을 한다. 
</br>
</br>

### ViewModelFactory

```ViewModelFactory``` instantiates ```ViewModel``` objects, with or without constructor parameters
</br>
</br>
![Screenshot from 2022-01-01 00-12-02](https://user-images.githubusercontent.com/77181865/147829957-5748e659-3581-4cd2-ae82-48ca39816573.png)
</br>
</br>


## Project Code Review
