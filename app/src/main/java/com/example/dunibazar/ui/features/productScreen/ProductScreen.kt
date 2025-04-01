@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dunibazar.ui.features.productScreen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.dunibazar.R
import com.example.dunibazar.model.data.Comment
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.ui.theme.Blue
import com.example.dunibazar.ui.theme.PriceBackground
import com.example.dunibazar.ui.theme.shapes
import com.example.dunibazar.utils.MyScreens
import com.example.dunibazar.utils.NetworkChecker
import com.example.dunibazar.utils.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun ProductScreenThisScreen() {

    //ProductScreen()
}

@Composable
fun ProductScreen(productId: String) {

    val navigation = getNavController()
    val context = LocalContext.current
    val viewModel = getViewModel<ProductViewModel>()
    viewModel.loadData(productId, NetworkChecker(context).isInternetConnected)


    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, top = 18.dp, bottom = 48.dp)
                .verticalScroll(rememberScrollState())
        ) {

            ProductToolbar(
                badgeNumber = viewModel.badgeNumber.value,
                productName = viewModel.thisProduct.value.name,
                onBackClicked = {
                    navigation.popBackStack()
                },
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.routeName)
                    } else {
                        Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT)
                            .show()
                    }

                })

                val comments = if(NetworkChecker(context).isInternetConnected)  viewModel.comments.value else listOf()
            ProductItem(comments = comments,
                product = viewModel.thisProduct.value,
                onAddNewComment = {
                        viewModel.addNewComment(productId,it){ payam ->

                            Toast.makeText(context,payam , Toast.LENGTH_SHORT).show()
                        }
                },
                onCategoryClicked = {
                    navigation.navigate(MyScreens.CategoryScreen.routeName + "/" + it)
                }

            )


        }


         AddToCart(
             price = viewModel.thisProduct.value.price,
             isAddingProduct =viewModel.isAddingProduct.value
         ) {
             viewModel.addToCart(productId){
                 Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
             }


         }




    }


}


@Composable
fun ProductItem(
    comments: List<Comment>,
    product: Product,
    onCategoryClicked: (String) -> Unit,
    onAddNewComment: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ProductDesign(
            product = product,
            onCategoryClicked = { onCategoryClicked.invoke(product.category) })


        HorizontalDivider(
            thickness = 1.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp)
        )


        ProductDetail(product, comments)

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
        )

        ProductComments(comments, onAddNewComment)


    }

}

@Composable
fun CommentBody(comment: Comment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = comment.userEmail,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = comment.text,
                style = TextStyle(fontSize = 14.sp)
            )

        }

    }

}


@Composable
fun ProductComments(comments: List<Comment>, addNewComment: (String) -> Unit) {
    val context = LocalContext.current
    var showDialog = remember { mutableStateOf(false) }

    if (comments.isNotEmpty()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Comments",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            TextButton(onClick = {

                if (NetworkChecker(context).isInternetConnected) {
                    showDialog.value = true
                } else {
                    Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
                }

            }) {

                Text(
                    text = "Add New Comment",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
                )

            }

        }

        comments.forEach {
            CommentBody(comment = it)
        }


    } else {

        TextButton(onClick = {

            if (NetworkChecker(context).isInternetConnected) {
                showDialog.value = true
            } else {
                Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
            }

        }) {

            Text(
                text = "Add New Comment",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
            )

        }

    }

    if (showDialog.value) {

        AddNewCommentDialog(onDismiss = { showDialog.value = false },
            onPositive = {
                addNewComment.invoke(it)
                showDialog.value = false
            }
        )
    }


}

@Composable
fun AddNewCommentDialog(
    onDismiss: () -> Unit,
    onPositive: (String) -> Unit
) {

    var userComment = remember { mutableStateOf("") }
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {

        Card(
            shape = shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Write Your Comment",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProductTextField(
                    hint = "Write Something ...",
                    edtValue = userComment.value,
                ) {
                    userComment.value = it
                }



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(onClick = { onDismiss.invoke() }) {

                        Text(
                            text = "Cancel",
                        )

                    }

                    TextButton(onClick = {
                        if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {

                            if (NetworkChecker(context).isInternetConnected) {
                                onPositive(userComment.value)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please Connect to Internet",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {

                            Toast.makeText(
                                context,
                                "Please Write Your Comment First",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }) {

                        Text(
                            text = "OK",
                        )

                    }

                }

            }

        }


    }

}

@Composable
fun ProductTextField(hint: String, edtValue: String, onValueChange: (String) -> Unit) {

    OutlinedTextField(
        value = edtValue,
        onValueChange = onValueChange,
        placeholder = { Text(text = hint) },
        label = { Text(text = hint) },
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = shapes.large,
        maxLines = 2,
        singleLine = false
    )

}


@Composable
fun ProductDesign(
    product: Product,
    onCategoryClicked: (String) -> Unit
) {

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = shapes.medium),
        model = product.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Text(
        text = product.name,
        modifier = Modifier.padding(top = 8.dp),
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
    )


    Text(
        text = product.detailText,
        modifier = Modifier
            .padding(top = 8.dp),
        style = TextStyle(fontSize = 13.sp, textAlign = TextAlign.Justify)
    )

    TextButton(onClick = { onCategoryClicked.invoke(product.category) }) {

        Text(
            text = "# " + product.category,
            modifier = Modifier.padding(top = 8.dp),
            style = TextStyle(fontSize = 13.sp)
        )
    }
}

@Composable
fun ProductDetail(product: Product, comments: List<Comment>) {

val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource(id = R.drawable.ic_details_comment), contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                 val aa = if (NetworkChecker(context =context ).isInternetConnected) comments.size.toString() + " Comments" else "No Internet Connection"
                Text(
                    text = aa,
                    modifier = Modifier.padding(start = 6.dp),
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )

            }
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource(id = R.drawable.ic_details_material), contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = product.material,
                    modifier = Modifier.padding(start = 6.dp),
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )

            }
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = product.soldItem + " Sold",
                    modifier = Modifier.padding(start = 6.dp),
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )

            }


        }

        Surface(
            shape = shapes.large,
            color = Blue,
            modifier = Modifier.align(Alignment.Bottom)
        ) {
            Text(
                text = product.tags,
                color = Color.White,
                modifier = Modifier.padding(6.dp),
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
            )

        }


    }


}


@Composable
fun ProductToolbar(
    badgeNumber : Int,
    productName: String,
    onBackClicked: () -> Unit,
    onCartClicked: () -> Unit
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
                onClick = { onCartClicked.invoke() }
            ) {

                if (badgeNumber == 0) {
                    Icon(Icons.Default.ShoppingCart, null)
                } else {
                    BadgedBox(badge = { Badge { Text(badgeNumber.toString()) } }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }




            }


        },
        title = {
            Text(
                text = productName,
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
fun AddToCart(
    price : String,
    isAddingProduct : Boolean ,
    addToCart : ()->Unit,
) {

    val configuration= LocalConfiguration.current
    val fraction = if (configuration.orientation== Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.08f

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(shapes.medium)
                    .size(182.dp, 40.dp),
                onClick = { addToCart.invoke() }
            ) {
                if (isAddingProduct){
                    DotsTyping()

                }else{

                    Text(
                        text ="Add Product To Cart",
                        modifier = Modifier.padding(2.dp),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    )

                }


            }

            Surface(
                color = PriceBackground,
                modifier = Modifier
                    .clip(shapes.medium)
                    .padding(end = 16.dp)
            ) {

                Text(
                    text = stylePrice(price),
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )

            }




        }





    }


}

@Composable
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}


