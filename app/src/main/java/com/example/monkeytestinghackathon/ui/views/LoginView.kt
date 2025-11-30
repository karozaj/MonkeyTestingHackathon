package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monkeytestinghackathon.R
import com.example.monkeytestinghackathon.viewmodels.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginView(
    onGoogleSignInClick: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    Scaffold { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .shadow(10.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.deckmeetings),
                        contentDescription = "DeckMeetings Logo",
                        modifier = Modifier
                            .size(440.dp)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Witaj w DeckMeetings",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Zapisz się i graj z innymi na różnych karcianych wydarzeniach!",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center

                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onGoogleSignInClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Zaloguj się z Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
