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

- Before you add ```ViewModel``` : When the app goes through a configuration change such as a screen rotation, the fragment is destroyed and re-created. The data is lost
- After you add ```ViewModel``` : All the data that the fragment needs to display is now the ```ViewModel```. When the app goes through a configuration change, the ```ViewModel``` survives, and the data is retained.
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

### Dependency 추가(```build.gradle(module:app)```)

    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
    
### UI Controller 내에 ViewModel의 reference를 생성해줘야 한다.(ViewModel 클래스가 아닌, UI Controller class에 member 선언)

    private lateinit var viewModel : GameViewModel          //GameViewModel class -> ViewModel Class
    
### ViewModelProvider 
화면 회전같은 configuration의 변화가 일어나면, fragment와 같은 UI controller는 재생성된다. 그러나 이런 경우에 ViewModel instance의 lifecycle은 계속 살아 있으므로 ViewModel 안에 UI controller를 넣으면 controller 마찬가지로 survive한다. ViewModel은 ```ViewModelProvider``` 를 사용하여 생성해야 한다. 

ViewModel을 초기화하기 위해선 ```ViewModelProvider.get()``` 을 사용해야 한다.  

    viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
    
### Configuration 변화가 일어나도 저장되어야 할 data는 모두 ViewModel 안으로 ! 
- ```word```, ```score```, ```wordList``` 와 같이 저장되어야 할 data는 모두 ViewModel 안으로 넣어야 한다.
- View에서는 단순히 ViewModel로부터 data를 읽어오는 것만 구현해야 한다.
- View은 ViewModel과 databinding이 되어 있는 것을 통해 값을 읽어올 수 있다.
- ViewModel 내에서는 data를 가공해야 할 operation들을 구현 해야한다.


### gameFinished() - 다음 Fragment로 이동 하기 위함

    private fun gameFinished(){
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameToScore()
        action.score = viewModel.score
        NavHostFragment.findNavController(this).navigate(action)
    }
    
```GameFragmentDirections``` 는 Safe Args가 각 작업마다 작업이 탐색을 시작하는 대상인 각 발신 대상의 클래스를 생성한다. 생성된 클래스 이름은 발신 대상 클래스 이름과 'Directions'라는 단어의 조합이다. 따라서 ```GameFragmentDirections``` 는 GameFragment가 발신 대상이므로 ```actionGameToScore()``` method 호출을 통해 Game -> Score 이동에 대한 ```action```을 초기화 한 것이다.

