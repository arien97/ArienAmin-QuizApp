package com.example.hw3quizapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizApp()
        }
    }
}
data class Question(val text: String, val answer: String)

val questions = listOf(
    Question("What is the capital of France?", "Paris"),
    Question("What is the square root of 64?", "8"),
    Question("What planet is known as the Red Planet?", "Mars")
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizApp() {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var quizCompleted by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Persistent SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle side effects when the snackbar message changes
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
                .padding(16.dp)
        ) {
            Text(
                text = question.text,
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = userInput,
            onValueChange = onInputChange,
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSubmit) {
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

        Button(onClick = onRestart) {
            Text("Restart Quiz")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun Preview() {}