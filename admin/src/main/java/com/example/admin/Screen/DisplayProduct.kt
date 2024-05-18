package com.example.admin.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.admin.Data.ProductData
import com.example.admin.DestinationScreen
import com.example.admin.Model
import com.example.admin.navigateTo
import com.google.firebase.database.FirebaseDatabase

@Composable
fun displayProduct(navController: NavController,vm:Model){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFACD)) // Full yellow color
    ) {
        Column {
            Button(
                onClick = {
                    navigateTo(navController, DestinationScreen.Add.route)
                },
                modifier = Modifier
                    .background(color = Color(0xFF4b0082)) // Royal color
                    .padding(16.dp)
            ) {
                Text(text = "Thêm sản phẩm", color = Color.Yellow) // Yellow color
            }
            Text(text = "Danh sách sản phẩm", fontSize = 20.sp,color = Color(0xFF4b0082)) // Royal color
            Spacer(modifier = Modifier.height(12.dp))

            DisplayProduct(vm)
        }
    }
}

@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayProduct(vm: Model){
    val productListState = remember {
        mutableStateOf(emptyList<ProductData>())
    }

    LaunchedEffect(Unit) {
        vm.fetchProductData { productList ->
            productListState.value = productList
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFACD)) // Full yellow color
    ) {
        items(productListState.value) { product ->
            ProductItem(
                vm = vm, product = product
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: ProductData, vm: Model) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf(product.name ?: "") }
    var cost by rememberSaveable { mutableStateOf(product.cost ?: "") }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .border(BorderStroke(1.dp, Color(0xFF4b0082)), shape = RoundedCornerShape(5.dp)) // Royal color
        .background(color = Color(0xFFFFFACD)) // Light yellow color
        .padding(10.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (imageProduct, textName, textCost, iconUpdate, iconDelete) = createRefs()

            val imagePainter = rememberImagePainter(data = product.imageUrl)
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .constrainAs(imageProduct) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .constrainAs(textName) {
                        top.linkTo(imageProduct.top, margin = 4.dp)
                        start.linkTo(imageProduct.end)
                    }
                    .width(230.dp)
            )

            TextField(
                value = cost,
                onValueChange = { cost = it },
                modifier = Modifier
                    .constrainAs(textCost) {
                        top.linkTo(textName.bottom)
                        start.linkTo(imageProduct.end)
                    }
                    .width(230.dp)
            )

            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(iconDelete) {
                        bottom.linkTo(parent.bottom,margin = 10.dp)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        product.productId?.let {
                            vm.deleteProduct(it)
                        }
                    },
                tint = Color(0xFF4b0082) // Royal color
            )

            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(iconUpdate) {
                        top.linkTo(parent.top, margin = 10.dp)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        product.productId?.let {
                            vm.updateProduct(
                                productId = it,
                                updatedCost = cost,
                                updatedName = name,
                                updatedImageUrl = product.imageUrl ?: "",
                                context = context
                            )
                        }
                    },
                tint = Color(0xFF4b0082) // Royal color
            )
        }
    }
}
