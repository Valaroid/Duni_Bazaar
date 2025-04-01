package com.example.dunibazar.ui.features.categoryScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.ui.theme.Blue
import com.example.dunibazar.ui.theme.shapes
import com.example.dunibazar.utils.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Preview(showBackground = true)
@Composable
fun previewThisScreen() {

    // CategoryToolBar("categoryName")
}

@Composable
fun CategoryScreen(categoryName: String) {

    val viewModel = getViewModel<CategoryViewModel>()
    viewModel.loadDataByCategory(categoryName)

    val navigation = getNavController()

    Column {

        CategoryToolBar(categoryName)

        CategoryList(viewModel.dataProducts.value){
            navigation.navigate(MyScreens.ProductScreen.routeName + "/" + it)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryToolBar(categoryName: String) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = categoryName,
                textAlign = TextAlign.Center
            )
        },


        )


}


@Composable
fun CategoryList(data: List<Product>,onProductClicked : (String)->Unit) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {

        items(data.size) {
            CategoryItem(data[it], onProductClicked)
        }

    }

}

@Composable
fun CategoryItem(product: Product , onProductClicked : (String)->Unit) {

    Column(modifier = Modifier.padding(bottom = 8.dp)) {

        Card (
            shape = shapes.medium,
            onClick = {onProductClicked.invoke(product.productId) },
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ){

            Column(modifier = Modifier.padding(bottom = 4.dp)) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    model = product.imgUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {

                    Column {

                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = product.name,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        )

                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = product.price+ " Tomans",
                            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        )


                    }

                    Surface(
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .align(Alignment.Bottom)
                            .clip(shapes.small),
                        color = Blue
                    ) {

                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = product.soldItem + " Sold",
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        )

                    }


                }

            }


        }




    }


}


