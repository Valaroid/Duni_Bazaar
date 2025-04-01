package com.example.dunibazar.ui.features.singUpScreen

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dunibazar.R
import com.example.dunibazar.ui.theme.BackgroundMain
import com.example.dunibazar.ui.theme.Blue
import com.example.dunibazar.ui.theme.MainTheme
import com.example.dunibazar.utils.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import com.example.dunibazar.utils.NetworkChecker
import com.example.dunibazar.utils.SUCCESS_MESSAGE
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Preview(showBackground = true)
@Composable
fun PreviewThisScreen() {


    MainTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundMain
        ) {

            SingUpScreen()


        }
    }


}


@SuppressLint("SuspiciousIndentation")
@Composable
fun SingUpScreen() {
    val context = LocalContext.current
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Blue)
    }
    val navigation = getNavController()
    val singUpViewModel = getViewModel<SingUpViewModel>()

    clearInputs(singUpViewModel)

    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .background(Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            IconApp()


            MakeCardView(navigation = navigation, viewModel = singUpViewModel) {

                singUpViewModel.singUpUser {
                    if (it == SUCCESS_MESSAGE) {
                        navigation.navigate(MyScreens.MainScreen.routeName) {
                            popUpTo(MyScreens.IntroScreen.routeName) {
                                inclusive = true
                            }
                        }

                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }

                }

            }


        }


    }


}


@Composable
fun MakeTextField(value: String, icon: Int, hint: String, setState: (String) -> Unit) {

    OutlinedTextField(
        value = value,
        singleLine = true,
        leadingIcon = { Image(painter = painterResource(id = icon), contentDescription = null) },
        label = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(top = 12.dp),
        placeholder = { Text(text = hint) },
        onValueChange = setState
    )


}


@Composable
fun PassWordTextField(value: String, icon: Int, hint: String, setState: (String) -> Unit) {

    var showPassword = remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }


    OutlinedTextField(
        value = value,
        singleLine = true,
        leadingIcon = { Image(painter = painterResource(id = icon), contentDescription = null) },
        label = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(top = 12.dp),
        placeholder = { Text(text = hint) },
        onValueChange = setState,
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        },

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        trailingIcon = {
            val image = if (showPassword.value) painterResource(id = R.drawable.ic_visible)
            else painterResource(id = R.drawable.ic_invisible)

            Icon(painter = image,
                contentDescription = null,
                modifier = Modifier.clickable { showPassword.value = !showPassword.value })

        })
}


@Composable
fun MakeCardView(navigation: NavController, viewModel: SingUpViewModel, singUpEvent: () -> Unit) {
    val fullName = viewModel.fullName.observeAsState("")
    val email = viewModel.email.observeAsState("")
    val password = viewModel.passWord.observeAsState("")
    val confirmPassword = viewModel.confirmPassword.observeAsState("")
    val context = LocalContext.current

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = BackgroundMain,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "Sing Up",
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                style = TextStyle(
                    color = Blue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            MakeTextField(
                value = fullName.value,
                icon = R.drawable.ic_person,
                hint = "Your Full Name"
            ) {
                viewModel.fullName.value = it
            }

            MakeTextField(
                value = email.value,
                icon = R.drawable.ic_email,
                hint = "Your Email"
            ) {
                viewModel.email.value = it
            }

            PassWordTextField(
                value = password.value,
                icon = R.drawable.ic_password,
                hint = "Password"
            ) {
                viewModel.passWord.value = it
            }

            PassWordTextField(
                value = confirmPassword.value,
                icon = R.drawable.ic_password,
                hint = "Confirm Password"
            ) {
                viewModel.confirmPassword.value = it
            }

            Button(onClick = {


                if (fullName.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && confirmPassword.value.isNotEmpty()) {

                    if (password.value == confirmPassword.value) {

                        if (password.value.length > 8) {

                            if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                if (NetworkChecker(context).isInternetConnected) {

                                    singUpEvent.invoke()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please connect to Internet ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                Toast.makeText(
                                    context,
                                    "email format is not true! ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Password characters should be more than 8 ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } else {
                        Toast.makeText(context, "Passwords are not the same", Toast.LENGTH_SHORT)
                            .show()
                    }


                } else {
                    Toast.makeText(context, "Please Fill the Data Boxes", Toast.LENGTH_SHORT).show()
                }


            }, modifier = Modifier.padding(top = 18.dp)) {

                Text(
                    text = "Register Account",
                    style = TextStyle(
                        color = BackgroundMain,
                        background = Blue,
                        fontSize = 18.sp
                    )
                )
            }


            Row(
                modifier = Modifier.padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = "Already have an account?")

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = {
                    navigation.navigate(MyScreens.SingInScreen.routeName) {
                        popUpTo(MyScreens.SingUpScreen.routeName) {
                            inclusive = true
                        }
                    }
                }) {

                    Text(text = "Login", color = Blue)
                }

            }


        }


    }


}


@Composable
fun IconApp() {

    Surface(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)

    ) {

        Image(
            modifier = Modifier.padding(14.dp),
            painter = painterResource(id = R.drawable.ic_icon_app),
            contentDescription = null
        )

    }


}

fun clearInputs(viewModel: SingUpViewModel) {

    viewModel.email.value = ""
    viewModel.passWord.value = ""
    viewModel.confirmPassword.value = ""
    viewModel.fullName.value = ""
}












