package com.example.dunibazar.ui.features.cartScreen

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.ui.features.profileScreen.AddUserLocationDataDialog
import com.example.dunibazar.ui.theme.Blue
import com.example.dunibazar.ui.theme.PriceBackground
import com.example.dunibazar.ui.theme.shapes
import com.example.dunibazar.utils.MyScreens
import com.example.dunibazar.utils.NetworkChecker
import com.example.dunibazar.utils.PAYMENT_PENDING
import com.example.dunibazar.utils.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun previewThisScreen() {


}

@Composable
fun CartScreen() {
    val context = LocalContext.current
    val navigation = getNavController()
    val getDialogState = remember {
        mutableStateOf(false)
    }

    val viewModel = getViewModel<CartViewModel>()
    viewModel.loadData()




    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 74.dp)
        ) {

            CartToolbar(
                onBackClicked = { navigation.popBackStack() },
                onPersonClicked = { navigation.navigate(MyScreens.ProfileScreen.routeName) }
            )

            if (viewModel.productList.value.isNotEmpty()) {

                CartList(
                    data = viewModel.productList.value,
                    onItemClicked = { navigation.navigate(MyScreens.ProductScreen.routeName + "/" + it) },
                    onAddItemClicked = {
                        viewModel.addToCart(it)
                    },
                    onRemoveItemClicked = {
                        viewModel.removeFromCart(it)
                    },
                    isChangingNumber = viewModel.isChangingNumber.value

                )


            } else {

                NoDataAnimation()
            }



        }

        PurchaseAll(totalPrice = viewModel.totalPrice.value.toString()) {

            if (viewModel.productList.value.isNotEmpty()) {



                //agar address , postal code dorost neveshte nashode bod mikham dialog ono begiriem , seteshon konim
                if (viewModel.getUserLocation().first == "Click to Add" || viewModel.getUserLocation().second == "Click to Add") {
                    getDialogState.value = true


                } else {
                    //pardakht anjam bede

                    viewModel.submitOrder(
                        viewModel.getUserLocation().first,
                        viewModel.getUserLocation().second
                    ) { success, paymentLink ->

                        if (success) {

                            Toast.makeText(context, "Pay using zarinPal...", Toast.LENGTH_SHORT)
                                .show()

                            viewModel.saveStatusPayment(PAYMENT_PENDING)


                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink))
                            context.startActivity(intent)

                        } else {
                            Toast.makeText(context, "problem in payment...", Toast.LENGTH_SHORT)
                                .show()

                        }

                    }


                }


            } else {

                Toast.makeText(context, "Please add Some Items First...", Toast.LENGTH_SHORT)
                    .show()
            }


        }



        if (getDialogState.value){

            AddUserLocationDataDialog(
                showSaveLocation = true ,
                onDismiss = { getDialogState.value=false } ,
                onSubmitClicked = { address,postalCode,checked->

                    if(NetworkChecker(context).isInternetConnected) {

                        if(checked) {
                            viewModel.saveLocation(address , postalCode)
                        }

                        // pardakht ->
                        viewModel.submitOrder(
                            viewModel.getUserLocation().first,
                            viewModel.getUserLocation().second
                        ) { success, paymentLink ->

                            if (success) {

                                Toast.makeText(context, "Pay using zarinPal...", Toast.LENGTH_SHORT)
                                    .show()

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink))
                                context.startActivity(intent)

                            } else {
                                Toast.makeText(context, "problem in payment...", Toast.LENGTH_SHORT)
                                    .show()

                            }

                        }



                    } else {
                        Toast.makeText(context, "please connect to internet first...", Toast.LENGTH_SHORT).show()
                    }

                }
            )


        }



    }


}


@Composable
fun PurchaseAll(
    totalPrice: String,
    onPurchaseClicked: () -> Unit

) {

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.08f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction),
        color = Color.White
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp),
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        onPurchaseClicked.invoke()
                    } else {
                        Toast.makeText(
                            context,
                            "Please Connect To Internet First",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
            ) {
                Text(
                    text = "Lets Purchase",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                    color = Color.White
                )


            }

            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(shapes.medium),
                color = PriceBackground
            ) {

                Text(
                    text = "Total:" + stylePrice(totalPrice),
                    modifier = Modifier.padding(4.dp),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
                )

            }


        }


    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartToolbar(
    onBackClicked: () -> Unit,
    onPersonClicked: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp),
        navigationIcon = {
            IconButton(onClick = { onBackClicked.invoke() }) {

                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {

            IconButton(
                onClick = { onPersonClicked.invoke() }
            ) {

                Icon(Icons.Default.Person, null)

            }


        },

        title = {
            Text(
                text = "My Cart",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }
    )


}


@Composable
fun CartItem(
    product: Product,
    onItemClicked: (String) -> Unit,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    isChangingNumber: Pair<String, Boolean>
) {

    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {

        Card(
            shape = shapes.medium,
            onClick = { onItemClicked.invoke(product.productId) },
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            Column(modifier = Modifier.padding(bottom = 4.dp)) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    model = product.imgUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = product.name,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "From " + product.category + " Group",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    )

                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = "Product authenticity guarantee",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Available in stock to ship",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    )


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {

                    Surface(
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .align(Alignment.Bottom)
                            .clip(shapes.small),
                        color = PriceBackground
                    ) {

                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = stylePrice(
                                (product.price.toInt() * (product.quantity
                                    ?: "1").toInt()).toString()
                            ),
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .align(Alignment.Bottom)
                            .clip(shapes.small)

                    ) {

                        Card(
                            border = BorderStroke(2.dp, Blue),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                //mines , delete
                                if (product.quantity?.toInt() == 1) {

                                    IconButton(onClick = { onRemoveItemClicked.invoke(product.productId) }) {
                                        Icon(
                                            modifier = Modifier.padding(
                                                end = 4.dp,
                                                start = 4.dp
                                            ),
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null
                                        )
                                    }

                                } else {

                                    IconButton(onClick = { onRemoveItemClicked.invoke(product.productId) }) {
                                        Icon(
                                            modifier = Modifier.padding(
                                                end = 4.dp,
                                                start = 4.dp
                                            ),
                                            painter = painterResource(id = com.example.dunibazar.R.drawable.ic_minus),
                                            contentDescription = null
                                        )
                                    }

                                }


                                //male price
                                if (isChangingNumber.first == product.productId && isChangingNumber.second) {
                                    Text(
                                        text = "...",
                                        style = TextStyle(fontSize = 18.sp),
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                } else {

                                    Text(
                                        text = product.quantity ?: "1",
                                        style = TextStyle(fontSize = 18.sp),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )

                                }

                                //male +

                                IconButton(onClick = { onAddItemClicked.invoke(product.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )
                                }


                            }


                        }


                    }


                }
            }


        }


    }


}


@Composable
fun CartList(
    data: List<Product>,
    onItemClicked: (String) -> Unit,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    isChangingNumber: Pair<String, Boolean>
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {

        items(data.size) {

            CartItem(
                product = data[it],
                onItemClicked = onItemClicked,
                onAddItemClicked = onAddItemClicked,
                onRemoveItemClicked = onRemoveItemClicked,
                isChangingNumber = isChangingNumber
            )

        }

    }


}


@Composable
fun NoDataAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.example.dunibazar.R.raw.no_data)
    )

    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}







