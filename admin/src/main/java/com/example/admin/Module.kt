package com.example.admin

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.admin.Data.ProductData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class Model @Inject constructor(
    val database: FirebaseDatabase,

    val storage: FirebaseStorage
): ViewModel()
{


    fun addData(name: String? = null, cost: String? = null, imageUrl: String? = null, context: Context, productId: String?=null) {
//        inProcess.value = true
        val myRef: DatabaseReference = database.getReference("product")
        val productKey = myRef.push().key

        val product = ProductData(
            productId = productKey,
            name = name,
            cost = cost,
            imageUrl = imageUrl // Cập nhật đường dẫn ảnh vào đối tượng ProductData
        )
        val newProduct = ProductData(
            productId = "product_1",
            name = "Sản phẩm 1",
            cost = "100.000 VND",
            imageUrl = "file:///C:/Users/MINH/Downloads/picture/pikaso_enhance__custom_2K_Standard_r50_c30_.png"
        )
        productKey?.let { key ->
            myRef.child(key).setValue(newProduct)}
        productKey?.let { key ->
            myRef.child(key).setValue(product)
                .addOnSuccessListener {

                    Toast.makeText(
                        context, "Thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        context, "Thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun fetchProductData(callback: (List<ProductData>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("product")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductData>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(ProductData::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }
                callback(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun uploadToStorage(uri: Uri, context: Context, type: String, onSuccess: (String) -> Unit) {

        val storageRef = storage.reference
        val unique_image_name = UUID.randomUUID().toString()
        val spaceRef = storageRef.child("images/$unique_image_name.jpg")

        val byteArray: ByteArray? = context.contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }

        byteArray?.let {
            val uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Upload failed",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener { _ ->
                Toast.makeText(
                    context,
                    "Upload succeeded",
                    Toast.LENGTH_SHORT
                ).show()

                spaceRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Gọi hàm gọi lại onSuccess với đường dẫn của ảnh
                    onSuccess(imageUrl.toString())
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Failed to get download URL",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun updateProduct(productId: String, updatedName: String, updatedCost: String, updatedImageUrl: String, context: Context) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("product").child(productId)
        // Tạo một Map chứa các trường cần cập nhật

        val updates = hashMapOf<String, Any>(
            "name" to updatedName,
            "cost" to updatedCost,
            "imageUrl" to updatedImageUrl
        )
        // Thực hiện cập nhật dữ liệu
        myRef.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(
                    context, "Cập nhật dữ liệu thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context, "Cập nhật dữ liệu thất bại: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    fun deleteProduct(productId: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("product").child(productId)

        myRef.removeValue()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Xóa dữ liệu thành công!")
                // Đây là nơi bạn có thể cập nhật UI hoặc thực hiện các hành động khác sau khi xóa dữ liệu thành công.
            }
            .addOnFailureListener {
                Log.e(ContentValues.TAG, "Xóa dữ liệu thất bại!", it)
                // Đây là nơi bạn có thể xử lý lỗi nếu xóa dữ liệu không thành công.
            }
    }


}