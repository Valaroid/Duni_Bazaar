package com.example.dunibazar.utils

sealed class MyScreens(val routeName : String) {

    data object IntroScreen: MyScreens("IntroScreen")
    data object MainScreen: MyScreens("MainScreen")
    data object SingUpScreen: MyScreens("SingUpScreen")
    data object SingInScreen: MyScreens("SingInScreen")
    data object CartScreen: MyScreens("CartScreen")
    data object ProductScreen: MyScreens("ProductScreen")
    data object CategoryScreen: MyScreens("CategoryScreen")
    data object ProfileScreen: MyScreens("ProfileScreen")

}