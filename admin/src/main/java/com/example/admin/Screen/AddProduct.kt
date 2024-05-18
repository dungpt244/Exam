package com.example.admin.Screen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.admin.DestinationScreen
import com.example.admin.Model
import com.example.admin.navigateTo

@Composable
fun AddProduct(navController: NavController, vm: Model) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFACD)) // Yellow color
    ) {
        var uri by remember {
            mutableStateOf<Uri?>(null)
        }

        val productPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                uri = it
            }
        )

        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thông tin sản phẩm",
                fontSize = 27.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF4b0082) // Royal color
            )
            val nameProductState = remember {
                mutableStateOf(TextFieldValue())
            }
            val costProductState = remember {
                mutableStateOf(TextFieldValue())
            }
            OutlinedTextField(
                value = nameProductState.value,
                onValueChange = {
                    nameProductState.value = it
                },
                label = { Text(text = "Nhập tên sản phẩm ", color = Color(0xFF4b0082)) }, // Royal color
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = costProductState.value,
                onValueChange = {
                    costProductState.value = it
                },
                label = { Text(text = "Nhập giá sản phẩm ", color = Color(0xFF4b0082)) }, // Royal color
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(200.dp))

            Button(
                onClick = {
                    productPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.size(280.dp, 50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF4b0082) // Royal color
                )
            ) {
                Text("Chọn ảnh sản phẩm ", color = Color.Yellow) // Yellow color
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    uri?.let {
                        vm.uploadToStorage(uri = it, context = context, type = "image") { imageUrl ->
                            vm.addData(
                                nameProductState.value.text,
                                costProductState.value.text,
                                imageUrl,
                                context
                            )
                        }
                    }
                },
                modifier = Modifier.size(280.dp, 50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF4b0082) // Royal color
                )
            ) {
                Text(text = "Thêm sản phẩm", color = Color.Yellow) // Yellow color
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    navigateTo(navController, DestinationScreen.Display.route)
                },
                modifier = Modifier.size(280.dp, 50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF4b0082) // Royal color
                )
            ) {
                Text(text = "Danh sách ", color = Color.Yellow) // Yellow color
            }
        }
    }
}
