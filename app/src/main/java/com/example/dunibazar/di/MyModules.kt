package com.example.dunibazar.di

import android.content.Context
import androidx.room.Room
import com.example.dunibazar.model.db.AppDataBase
import com.example.dunibazar.model.net.createApi
import com.example.dunibazar.model.repository.cart.CartRepository
import com.example.dunibazar.model.repository.cart.CartRepositoryImp
import com.example.dunibazar.model.repository.comment.CommentRepository
import com.example.dunibazar.model.repository.comment.CommentRepositoryImp
import com.example.dunibazar.model.repository.product.ProductRepository
import com.example.dunibazar.model.repository.product.ProductRepositoryImp
import com.example.dunibazar.model.repository.user.UserRepository
import com.example.dunibazar.model.repository.user.UserRepositoryImp
import com.example.dunibazar.ui.features.cartScreen.CartViewModel
import com.example.dunibazar.ui.features.categoryScreen.CategoryViewModel
import com.example.dunibazar.ui.features.mainScreen.MainViewModel
import com.example.dunibazar.ui.features.productScreen.ProductViewModel
import com.example.dunibazar.ui.features.profileScreen.ProfileViewModel
import com.example.dunibazar.ui.features.singInScreen.SingInViewModel
import com.example.dunibazar.ui.features.singUpScreen.SingUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }

    single { createApi() }
    single { Room.databaseBuilder(androidContext(),AppDataBase::class.java,"App_DataBase.dp").build() }

    single<UserRepository> { UserRepositoryImp(get(), get()) }
    single<ProductRepository> { ProductRepositoryImp(get(),get<AppDataBase>().productDao) }
    single<CommentRepository> { CommentRepositoryImp(get()) }
    single<CartRepository> { CartRepositoryImp(get() ,get()) }

    viewModel { ProfileViewModel(get()) }
    viewModel { SingUpViewModel(get()) }
    viewModel { SingInViewModel(get()) }
    viewModel { (isNetConnect : Boolean)-> MainViewModel(get(),get(),isNetConnect) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel(get() , get(), get()) }
    viewModel { CartViewModel(get() , get()) }
}