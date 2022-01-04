# Room Database
## Introduction

```Room``` 은 Android Jetpack에 이는 DB Library이다. 이는 SQLite DB의 abstraction layer이다. ```Room```은 DB 설정, 구성 및 interaction 등의 작업들을 간단화하고, 함수 호출을 함으로써 app과 interaction을 가능하게 한다.

![image](https://user-images.githubusercontent.com/77181865/148020872-fe4be93c-ae03-4ce4-b8d1-1bb5506f568e.png)
</br>

You must define each entity as an annotated data class, and the interactions with that entity as an annotated interface, called a data access object (DAO). ```Room``` 은 DB 내 table과 queries를 생성하기 위해 annotated class들을 사용한다.

## Data class

```Kotlin
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_sleep_quality_table")                    // @Entity(tableName = "Table 이름") => 테이블 설정        
data class SleepNight(
       @PrimaryKey(autoGenerate = true)                             // @PrimaryKey(autoGenerate = true) => PK 설정, autoGenerate => Room generates the ID for each entity. This guarantess that the ID for each entity is unique
       var nightId: Long = 0L,

       @ColumnInfo(name = "start_time_milli")                       // @ColumnInfo(name = "Column 이름") => Column 설정
       val startTimeMilli: Long = System.currentTimeMillis(),

       @ColumnInfo(name = "end_time_milli")
       var endTimeMilli: Long = startTimeMilli,

       @ColumnInfo(name = "quality_rating")
       var sleepQuality: Int = -1
)
```

## Create the DAO
### 1. interface 생성

```Kotlin
@Dao
interface SleepDatabaseDao { }
```

### 2. interface body 내 query 설정

```Kotlin
@Insert
fun insert(night: SleepNight)           //Insert

// SELECT를 할 때 retrieve 하고자 하는 data의 type에 맞게 설정해줘야 한다.

@Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
fun get(key: Long): SleepNight?         // data type : SleepNight

@Query("DELETE FROM daily_sleep_quality_table")
fun clear()

@Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
fun getTonight(): SleepNight?

@Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
fun getAllNights(): LiveData<List<SleepNight>>          data type : LiveData<List<SleepNight>>
```

### 3. Create a Room database

```Kotlin
//This class is to act as a database holder.
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)    //version => Whenever you change the schema, you'll have to increase the version number, exportSchema = false => history backup keep x
abstract class SleepDatabase : RoomDatabase() {             

    abstract val sleepDatabaseDao: SleepDatabaseDao                             

    companion object {      // companion object => allow to access the method for creating or getting the database without instantiating the class. 

        @Volatile           // Volatile => never be cached, R/W => main memory. Main memory에서 접근한다 => always up-to-date
        private var INSTANCE: SleepDatabase? = null     // INSTANCE => keep a reference to the db 

        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            SleepDatabase::class.java,
                            "sleep_history_database"
                    )
                            // Migration이 일어나면 old -> new schema 이동 할 때 data가 손실 될 수 있으므로 이를 막기 위함.
                            .fallbackToDestructiveMigration()       // schema가 변할 때 사용하는 migration strategy.
                            .build()
                            
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
```
