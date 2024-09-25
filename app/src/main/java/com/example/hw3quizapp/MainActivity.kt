package com.example.hw3quizapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizApp()
        }
    }
}

// question object so that it's easier to get the correlated answer
data class Question(val text: String, val answer: String)

val questions = listOf(
    Question("What is the largest mammal in the world?", "Blue Whale"),
    Question("What is the largest organ of the human body?", "Skin"),
    Question("What is the chemical symbol for gold?", "Au"),
    Question("What is the largest planet in our solar system?", "Jupiter"),
    Question("What is the smallest country in the world?", "Vatican City"),
    Question("What is the largest river in the world?", "Nile"),
    Question("What is the largest country in the world by land area?", "Russia"),
    Question("What is the largest desert in the world?", "Gobi"),
    Question("What is the largest ocean in the world?", "Pacific Ocean"),
    Question("What is the largest island in the world?", "Greenland"),
    Question("What is the largest lake in the world?", "Lake Superior"),
    Question("What direction does the sun rise?", "East")

)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizApp() {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var quizCompleted by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    if (quizCompleted) {
        QuizCompleteScreen(onRestart = {
            currentQuestionIndex = 0
            quizCompleted = false
        })
    } else {
        val currentQuestion = questions[currentQuestionIndex]

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(top = 48.dp, bottom = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Quiz App",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFbf6f75),
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            QuizContent(
                question = currentQuestion,
                userInput = userInput,
                onInputChange = { userInput = it },
                onSubmit = {
                    if (userInput.trim().equals(currentQuestion.answer, ignoreCase = true)) {
                        snackbarMessage = "Correct!"
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            quizCompleted = true
                        }
                    } else {
                        snackbarMessage = "Incorrect :( Please try again."
                    }
                    userInput = ""
                }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContent(
    question: Question,
    userInput: String,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFfce3e5)
            ),
            border = BorderStroke(1.dp, Color.White),
        ) {
            Text(
                text = question.text,
                modifier = Modifier.padding(vertical = 36.dp, horizontal = 20.dp),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            label = { Text("Type your answer", color = Color(0xFFbf6f75)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color(0xFFbf6f75),
                containerColor = Color.Transparent)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSubmit, colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFfce3e5),
            contentColor = Color.Black
        )) {
            Text("Submit Answer")
        }
    }
}


@Composable
fun QuizCompleteScreen(onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRestart, colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFfce3e5),
            contentColor = Color.Black))
        {
            Text("Restart Quiz")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {}