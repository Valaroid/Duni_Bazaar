package com.example.dunibazar.ui.features.introScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

import com.example.dunibazar.R
import com.example.dunibazar.ui.theme.BackgroundMain
import com.example.dunibazar.ui.theme.Blue
import com.example.dunibazar.ui.theme.MainTheme
import com.example.dunibazar.utils.MyScreens
import dev.burnoo.cokoin.navigation.getNavController





@Composable
fun IntroScreen() {
    val navigation = getNavController()
    Image(
        painter = painterResource(id = R.drawable.img_intro),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ){
        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = { navigation.navigate(MyScreens.SingUpScreen.routeName) }) {
            Text(text = "Sing Up")
        }

        Button(
            colors = ButtonDefaults.buttonColors(BackgroundMain),
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = { navigation.navigate(MyScreens.SingInScreen.routeName) }) {
            Text(text = "Sing In" , color = Blue)
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.29f))

    }

    //rememberSystemUiController().setSystemBarsColor(Color.Transparent)


}


@Preview(showBackground = true)
@Composable
fun PreviewIntroScreen() {

    MainTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundMain
        ) {

            IntroScreen()

        }
    }
}