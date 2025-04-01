@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dunibazar.ui.features.mainScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.dunibazar.R
import com.example.dunibazar.model.data.Ads
import com.example.dunibazar.model.data.CheckOut
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.ui.theme.BackgroundMain
import com.example.dunibazar.ui.theme.CardViewBackground
import com.example.dunibazar.ui.theme.MainTheme
import com.example.dunibazar.ui.theme.shapes
import com.example.dunibazar.utils.CATEGORY
import com.example.dunibazar.utils.MyScreens
import com.example.dunibazar.utils.NO_PAYMENT
import com.example.dunibazar.utils.NetworkChecker
import com.example.dunibazar.utils.PAYMENT_PENDING
import com.example.dunibazar.utils.PAYMENT_SUCCESS
import com.example.dunibazar.utils.TAGS
import com.example.dunibazar.utils.stylePrice
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import org.koin.core.parameter.parametersOf


@Preview(showBackground = true)
@Composable
fun previewThisScreen() {

    MainTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundMain
        ) {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    val context = LocalContext.current
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(BackgroundMain) }

    val viewModel =
        getViewModel<MainViewModel>(parameters = { parametersOf(NetworkChecker(context).isInternetConnected) })
    val dataProducts = viewModel.dataProducts.value
    val dataAds = viewModel.dataAds.value

    val navigation = getNavController()

    if (NetworkChecker(context).isInternetConnected) {
        viewModel.getCartSize()
    }

    if (viewModel.getStatusPayment()== PAYMENT_PENDING){
        if (NetworkChecker(context).isInternetConnected){
            viewModel.getCheckOut()
        }
    }


    Box {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (viewModel.showProgressBarState.value)
                LinearProgressIndicator()


            ToolBar(
                badgeNumber = viewModel.badgeNumber.value,
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.routeName)
                    } else {
                        Toast.makeText(
                            context,
                            "Please Connect to Internet First",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                },
                onProfileClicked = { navigation.navigate(MyScreens.ProfileScreen.routeName) })

            CategoryBar(CATEGORY) {
                navigation.navigate(MyScreens.CategoryScreen.routeName + "/" + it)
            }

            ProductSubjectList(TAGS, dataProducts, dataAds) {
                navigation.navigate(MyScreens.ProductScreen.routeName + "/" + it)
            }


        }


        if (viewModel.dialogPaymentResult.value) {

            PaymentResultDialog(
                checkoutResult = viewModel.checkOut.value,
                onDismiss = {

                    viewModel.dialogPaymentResult.value = false
                    viewModel.setStatusPayment(NO_PAYMENT)

                }
            )

        }

    }


}


@Composable
private fun PaymentResultDialog(
    checkoutResult: CheckOut,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Payment Result",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Data
                if (checkoutResult.order?.status?.toInt() == PAYMENT_SUCCESS) {

                    AsyncImage(
                        model = R.drawable.success_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(110.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order!!.amount).length - 1
                            )
                        )
                    )

                } else {

                    AsyncImage(
                        model =R.drawable.fail_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was not successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        )
                    )

                }

                // Ok Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "ok")
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}


//-----------------------------------

@Composable
fun ProductSubjectList(
    tags: List<String>,
    products: List<Product>,
    ads: List<Ads>,
    onProductClicked: (String) -> Unit
) {


    if (products.isNotEmpty()) {

        Column {

            tags.forEachIndexed { it, _ ->

                val dataWithTags = products.filter { product ->
                    product.tags == tags[it]
                }

                ProductSubject(tags[it], dataWithTags.shuffled(), onProductClicked)

                if (ads.size > 0) {
                    if (it == 1 || it == 2) {
                        BigPictureTablighat(ads[it - 1], onProductClicked)
                    }
                }

                if (it == 3) {
                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                }

            }


        }
    }

}


//-------------------------------------------


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(
    badgeNumber: Int,
    onCartClicked: () -> Unit,
    onProfileClicked: () -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = { Text(text = "Duni Bazaar") },
        actions = {
            IconButton(onClick = { onProfileClicked.invoke() }) {
                Icon(Icons.Default.Person, contentDescription = null)

            }
            IconButton(
                onClick = { onCartClicked.invoke() }
            ) {
                if (badgeNumber == 0) {
                    Icon(Icons.Default.ShoppingCart, null)
                } else {
                    BadgedBox(badge = { Badge() { Text(badgeNumber.toString()) } }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }

            }


        }


    )


}


//-------------------------------------------


@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {

        items(categoryList.size) {

            CategoryItem(categoryList[it], onCategoryClicked)

        }

    }


}

@Composable
fun CategoryItem(subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onCategoryClicked.invoke(subject.first) }
    ) {

        Surface(
            color = CardViewBackground,
            shape = shapes.medium
        ) {

            Image(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = subject.second),
                contentDescription = null
            )

        }

        Text(
            text = subject.first,
            style = TextStyle(color = Color.Gray),
            modifier = Modifier.padding(4.dp)
        )

    }

}


//-------------------------------------------


@Composable
fun BigPictureTablighat(ads: Ads, onProductClicked: (String) -> Unit) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .clip(shapes.medium)
    ) {
        AsyncImage(
            model = ads.imageURL,
            contentDescription = null,
            modifier = Modifier
                .clickable { onProductClicked.invoke(ads.productId) },
            contentScale = ContentScale.Crop
        )
    }


}


//-------------------------------------------


@Composable
fun ProductSubject(subject: String, data: List<Product>, onProductClicked: (String) -> Unit) {


    Column(
        modifier = Modifier.padding(top = 32.dp)
    ) {

        Text(
            modifier = Modifier.padding(start = 16.dp), text = subject,
            style = MaterialTheme.typography.titleLarge
        )


        LazyRow(
            modifier = Modifier.padding(top = 16.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {

            items(data.size) {

                ProductItem(data[it], onProductClicked)

            }

        }

    }


}

@Composable
fun ProductItem(product: Product, onProductClicked: (String) -> Unit) {

    Card(
        shape = shapes.medium,
        onClick = { onProductClicked.invoke(product.productId) },
        modifier = Modifier.padding(start = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(

            modifier = Modifier.padding(bottom = 4.dp)

        ) {

            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )

            Column(modifier = Modifier.padding(start = 8.dp)) {

                Text(
                    text = product.name,
                    style = TextStyle(
                        fontSize = 15.sp, color = Color.Black,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = stylePrice(product.price),
                    style = TextStyle(
                        fontSize = 14.sp, color = Color.Black,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = product.soldItem + " Solds",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )


            }

        }

    }


}





