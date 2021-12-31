# DataBinding & Navigation

## DataBinding 

### 1. build.gradle(Module)에 databinding 추가
    
       android{
                 ...
                 dataBinding{
                       enabled = true 
                 }
              }
              
### 2. xml 수정

    <layout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto">
            
            <data>
                <variable>
                    ex) name = "user" type = "com.example.User"
                </variable>
            </data>
            
                <LinearLayout
                     ...
                     android:text = "@{user.firstName}"         // '@{' 구문 사용
                </LinearLayout>
    </layout>
     
### 3. Data Class 정의
    
    data class User(val firstName: String, val lastName: String)
     
### 4. DataBinding 

       class MainActivity : AppCompatActivity(){
            override fun onCreate(savedInstanceState: Bundle?){
                super.onCreate(savedInstanceState)
                val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
                binding.user = User("Test", "User")
            }
            
#### - LayoutInflater를 사용하여 View를 가져올 경우
    val binding : ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater())
    

#### - Fragment, ListView 또는 RecyclerView Adapter 내에서 dataBinding 항목을 사용할 경우
    val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
    // or
    val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
    
    


## Navigation

### 1. build.gradle(Project)

    buildscript{
        ....
        navigationVersion = "2.3.0"
    }
    
### 2. build.gradle(Module)

    dependencies {
        implementation "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
        implementation "androidx.navigation:navigation-ui-ktx:$navigationVersion"
    }

### 3. Add a navigation graph to the project 
- New -> Android Resource File


![Screenshot from 2021-12-31 19-57-37](https://user-images.githubusercontent.com/77181865/147819661-332e442f-826d-4260-91ac-09f432158d20.png)


### 4. Open navigation.xml and Click 'New Destination' Button

### 5. Setting Destination

![Screenshot from 2021-12-31 20-00-36](https://user-images.githubusercontent.com/77181865/147819764-d4c31113-03cc-4509-b5f2-06bce49ebe79.png)


### 6. 예시) 버튼을 누르면 다음 activity로 이동할 때
 
    binding.playButton.setOnClickListener { view : View ->
       view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)     // view.findNavController().navigate(action) 을 통해 Activity 이동
    }


### 7. 조건에 따라 Activity 2개로 나뉠 때 
#### 다음 그림과 같이 두 Activity를 이어주면 된다!

![Screenshot from 2021-12-31 20-14-20](https://user-images.githubusercontent.com/77181865/147820381-9d9f9d81-1150-4b37-b7e6-7674606de845.png)


### 8. popUpTo, popUpInclusive 정리 (navigation.xml -> Design -> action 선택 -> popUpTo/popUpToInclusive)
- popUpTo : BackStack에서 어디까지 이동할 것인지 결정하는 속성
- popUpInclusive : popUpTo로 지정한 fragment까지 pop 시킬 것인지 정하는 속성

ex) 

    <action
        android:id = "@+id/action_fragment3_to_fragment4"
        app:destination="@id/fragment4"
        app:popUpTo = "@id/fragment2"
        app:popUpToInclusive="false"/>    //true일 때 fragment1 로 이동
        
fragment1 -> fragment2 -> fragment3 -> fragment4 일 때,
fragment3에서 fragment4로 이동할 때, fragment4에서 popBackStack() 시 fragment2로 이동함.


### 9. 뒤로가기 버튼 + 기능 활성화
- 뒤로가기 버튼(onCreate() 내부)
       
      val navController = this.findNavController(R.id.myNavHostFragment)
      NavigationUI.setupActionBarWithNavController(this, navController)
      
      
- 뒤로가기 버튼 기능 활성화

      override fun onSupportNavigateUp(): Boolean {
          val navController = this.findNavController(R.id.myNavHostFragment)
          return navController.navigateUp()
      }
      
### 10. Option을 달고 싶을 때
- New -> Android Resoure File
- Resource type : Menu 선택
- menu directory -> xml 선택 -> Design -> 메뉴 아이템 drag & drop


#### option에서 선택한 item으로 이동하기 
메뉴를 삽입한 fragment 앞쪽에다 fragment 두기 


![Screenshot from 2021-12-31 21-57-46](https://user-images.githubusercontent.com/77181865/147824634-085c2487-d934-463d-8528-24cc7ce10a1c.png)


##### 메뉴를 삽입한 fragment 내 onCreateView()
    
    setHasOptionsMenu(true)
   
    
##### onCreateOptionsMenu() & onOptionsItemSelected() 추가

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
    

### 11. 다중 메뉴 지원

#### dependency 추가
    
    implementation "com.google.android.material:material:$version"
    
#### navigation.xml에 다중 메뉴 선택할 때 보여질 fragment 추가

![Screenshot from 2021-12-31 22-03-44](https://user-images.githubusercontent.com/77181865/147824853-9e82da05-09fc-4582-96f8-3443b9b08c54.png)


#### 10번의 Option 달고 싶을 때와 동일한 과정 반복

#### Open the activity_main.xml & Add drawerlayout

    <layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <androidx.drawerlayout.widget.DrawerLayout
        android:id="@+id/drawerLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <LinearLayout
        . . . 
        </LinearLayout>
      
      <com.google.android.material.navigation.NavigationView
            android:id="@+id/navView"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_gravity="start"
            app:headerLayout="@layout/nav_header"
            app:menu="@menu/navdrawer_menu" />
            
        </androidx.drawerlayout.widget.DrawerLayout>
    </layout>


#### MainActivity.kt에 추가해야할 항목 
##### 1. onCreate 내
    
    NavigationUI.setupWithNavController(binding.navView, navController)
    
##### 2. 1번만으로 메뉴가 생기지 않으므로, drawerLayout 멤버를 생성해줘야 한다.

    private lateinit var drawerLayout : DrawerLayout
    
##### 3. 다시 onCreate() method

    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
                R.layout.activity_main)

    drawerLayout = binding.drawerLayout
    
##### 4. setupActionBarWithNavController() method에 3번째 parameter 추가 

    NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    
##### 5. Edit onSupportNavigateUp()

    override fun onSupportNavigateUp(): Boolean {
    val navController = this.findNavController(R.id.myNavHostFragment)
    return NavigationUI.navigateUp(navController, drawerLayout)
    }
    
##### 6. 최종 MainActivity.kt

![Screenshot from 2021-12-31 22-37-17](https://user-images.githubusercontent.com/77181865/147826051-a0085374-8fdf-4652-88a0-262e36b5f65a.png)



        
