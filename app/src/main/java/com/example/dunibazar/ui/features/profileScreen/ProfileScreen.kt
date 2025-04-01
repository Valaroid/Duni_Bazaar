package com.example.dunibazar.ui.features.profileScreen


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dunibazar.R
import com.example.dunibazar.ui.features.productScreen.ProductTextField
import com.example.dunibazar.ui.theme.Blue
import com.example.dunibazar.ui.theme.shapes
import com.example.dunibazar.utils.MyScreens

import com.example.dunibazar.utils.styleTime
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun ppreviewThisScreen() {


}
//bahse login time moshkel dare eshtebah neshon mide satato tarikho

@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getViewModel<ProfileViewModel>()
    viewModel.loadData()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileToolbar {
            navigation.popBackStack()
        }

        ProfileAnimation()

        ShowDataSection(subject = "Email Address", text = viewModel.email.value,null)

        ShowDataSection(subject = "Address", text = viewModel.address.value){
            viewModel.showDialog.value=true
        }

        ShowDataSection(subject = "Postal Code", text = viewModel.postalCode.value){
            viewModel.showDialog.value=true
        }

        ShowDataSection(subject = "Login Time", text = styleTime(viewModel.loginTime.value.toLong()),null)

        Button(onClick = {
            Toast.makeText(context, "Hope to see you again", Toast.LENGTH_SHORT).show()

            viewModel.signOut()


            navigation.navigate(MyScreens.MainScreen.routeName) {
                popUpTo(MyScreens.MainScreen.routeName) {
                    inclusive = true
                }
                navigation.popBackStack()
                navigation.popBackStack()
            }
        },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 48.dp)) {

            Text(text = "Sign Out")

        }


    }

    if (viewModel.showDialog.value){

        AddUserLocationDataDialog(false,
            onDismiss = {viewModel.showDialog.value=false},
            onSubmitClicked = {address,postalCode,_ ->

                viewModel.saveLocation(address,postalCode)

            })

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(
    onBackPressed: () -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp),
        navigationIcon = {

            IconButton(onClick = { onBackPressed.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },

        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 54.dp),
                text = "My Profile",
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        },
    )


}


@Composable
fun ProfileAnimation() {


    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.profile_anim)
    )

    LottieAnimation(
        modifier = Modifier
            .size(270.dp)
            .padding(top = 36.dp, bottom = 16.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )


}


@Composable
fun ShowDataSection(
    subject : String,
    text : String,
    onClickable : (()->Unit)?
) {

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clickable { onClickable?.invoke() },
       horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = subject,
            style = TextStyle(fontSize = 18.sp, color = Blue, fontWeight = FontWeight.Bold)
        )


        Text(
            color = Color.Black,
            text = text,
            style = TextStyle(fontSize = 16.sp, color = Blue, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 2.dp)
        )


        HorizontalDivider(
            thickness = 0.5.dp, color = Blue, modifier = Modifier.padding(top = 16.dp)
        )



    }

}


@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit
) {

    val context = LocalContext.current

    val checkedState = remember { mutableStateOf(true) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }



    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                ProductTextField(edtValue = userAddress.value, hint = "your address...") {
                    userAddress.value = it
                }

                ProductTextField(edtValue = userPostalCode.value, hint =  "your postal code...") {
                    userPostalCode.value = it
                }

                if (showSaveLocation) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )

                        Text(text = "Save To Profile")

                    }

                }


                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {

                        if (
                            (userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) &&
                            (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())
                        ) {
                            onSubmitClicked(
                                userAddress.value,
                                userPostalCode.value,
                                checkedState.value
                            )
                            onDismiss.invoke()
                        } else {
                            Toast.makeText(context, "please write first...", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

@Composable
fun MainTextField(value: String, s: String, content: () -> Unit) {

}
