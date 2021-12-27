package com.example.app.modules.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.modules.onboarding.OnBoardingScreen
import com.example.app.ui.theme.FirstComposeAppTheme
import kotlin.math.exp

/*
* Main Activity
* - Set Content()
* |  - FirstComposeAppTheme()
* |  |  - MyApp()
* |  |  |  - Greeting()
* |  |  |  |
* */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstComposeAppTheme {
                MyApp()
            }
        }
    }
}

@Composable
private fun MyApp() {
    var shouldShowOnBoarding by rememberSaveable { //rememberSaveable previne da mudança do estado da aplicação
        mutableStateOf(true)
    }

    if (shouldShowOnBoarding) {
        OnBoardingScreen(
            onContinueClicked = {
                shouldShowOnBoarding = false
            }
        )
    } else {
        Greetings()
    }

}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Hello List",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.h1.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
            )
        }
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                Greeting(name)
            }
        }
    }
}

@Composable
fun Greeting(name: String) { //Toda vez que a view é criada <-> recomposta. A função é criada de novo
    var expanded by rememberSaveable { //Para proteger contra a recomposição ♻️
        mutableStateOf(false)
    }

    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello, ")
                Text(text = name, style = MaterialTheme.typography.h4.copy(
                    fontWeight =  FontWeight.ExtraBold
                ))

                if (expanded) {
                    Text(text = "isOpen", Modifier.absoluteOffset(y = 36.dp))
                }
            }
            OutlinedButton(
                onClick = { expanded = !expanded }
            ) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    showBackground = true,
    name = "Text Preview"
)
@Composable fun DefaultPreview() {
    FirstComposeAppTheme {
        Greetings()
    }
}