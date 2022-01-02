package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {
    //The final Score
    var score = finalScore
    init{
        Log.i("ScoreModel", "Final score is $finalScore")
    }

}