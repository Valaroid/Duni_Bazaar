package com.example.dunibazar.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dunibazar.di.myModules
import com.example.dunibazar.model.repository.TokenInMemory
import com.example.dunibazar.model.repository.user.UserRepository
import com.example.dunibazar.ui.features.cartScreen.CartScreen
import com.example.dunibazar.ui.features.categoryScreen.CategoryScreen
import com.example.dunibazar.ui.features.introScreen.IntroScreen
import com.example.dunibazar.ui.features.mainScreen.MainScreen
import com.example.dunibazar.ui.features.productScreen.ProductScreen
import com.example.dunibazar.ui.features.profileScreen.ProfileScreen
import com.example.dunibazar.ui.features.singInScreen.SingInScreen
import com.example.dunibazar.ui.features.singUpScreen.SingUpScreen
import com.example.dunibazar.ui.theme.BackgroundMain
import com.example.dunibazar.ui.theme.MainTheme
import com.example.dunibazar.utils.KEY_ARG_Category
import com.example.dunibazar.utils.KEY_ARG_Product
import com.example.dunibazar.utils.MyScreens
import dev.burnoo.cokoin.Koin

import dev.burnoo.cokoin.navigation.KoinNavHost
import dev.burnoo.cokoin.get
import org.koin.android.ext.koin.androidContext



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {

                MainTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = BackgroundMain
                    ) {

                        val userRepository : UserRepository= get()
                        userRepository.loadToken()

                        DuniBazarUi()
                    }
                }


            }




        }
    }
}

@Composable
private fun DuniBazarUi() {

    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.MainScreen.routeName) {

        composable(route = MyScreens.IntroScreen.routeName) {
            IntroScreen()
        }

        composable(route = MyScreens.MainScreen.routeName) {

            if (TokenInMemory.token!=null){
                MainScreen()
            }else{
                IntroScreen()
            }

        }

        composable(route = MyScreens.SingInScreen.routeName) {
          SingInScreen()
        }


        composable(route = MyScreens.SingUpScreen.routeName) {
            SingUpScreen()
        }


        composable(route = MyScreens.CartScreen.routeName) {
            CartScreen()
        }

        composable(route = MyScreens.ProfileScreen.routeName) {
            ProfileScreen()
        }


        composable(
            route = MyScreens.CategoryScreen.routeName + "/" + "{$KEY_ARG_Category}",
            arguments = listOf(navArgument(KEY_ARG_Category) {
                type = NavType.StringType
            })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_ARG_Category, "null"))
        }

        composable(
            route = MyScreens.ProductScreen.routeName + "/" + "{$KEY_ARG_Product}",
            arguments = listOf(navArgument(KEY_ARG_Product) {
                type = NavType.StringType
            })
        ) {

            ProductScreen( it.arguments!!.getString(KEY_ARG_Product, "null"))

        }


    }


}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundMain
        ) {
            DuniBazarUi()
        }
    }




}

