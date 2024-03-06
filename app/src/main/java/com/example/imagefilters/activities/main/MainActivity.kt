package com.example.imagefilters.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.imagefilters.activities.editimage.EditImageActivity
import com.example.imagefilters.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 靜態成員和常數:
    // companion object 允許在類中聲明靜態成員。在這裡，REQUEST_CODE_PICK_IMAGE 和 KEY_IMAGE_URI 是靜態成員。
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
    }

    // ViewBinding:
    // ActivityMainBinding 是由 ViewBinding 自動生成的，基於相應 XML 佈局文件的名稱。這提供了對 XML 元素的類型安全引用。
    // binding.getRoot() 用於獲取根視圖，並使用它來設置 Activity 的內容視圖。
    private lateinit var binding: ActivityMainBinding

    // Activity 的生命週期:
    // onCreate 是當 Activity 第一次被創建時調用的方法。onActivityResult 在另一個 Activity 完成操作後返回時被調用。
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Kotlin Extension Functions:
        // binding.getRoot() 利用 Kotlin 的擴展函數，這允許像 binding 這樣的對象調用與之無關的函數。
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.buttonEditNewImage.setOnClickListener {
            Intent(
                // Intent 是用於執行不同的操作（例如打開相機、選擇圖片等）的對象。在這裡，使用 ACTION_PICK 行動來選擇圖片。
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).also { pickerIntent ->
                // FLAG_GRANT_READ_URI_PERMISSION 被添加到 Intent 中是為了授予讀取所選圖片的 URI 的權限。
                pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                // startActivityForResult 用於啟動另一個 Activity 以選擇圖片，並在操作完成後呼叫 onActivityResult。
                startActivityForResult(pickerIntent, REQUEST_CODE_PICK_IMAGE)
            }
        }
    }

    // Nullable 及 let 的使用:
    // data 是一個 Intent?，因此它可以是 null。data?.data 使用安全調用運算符確保安全訪問，let 用於處理非空情況。
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            // Lambda 表達式:
            // binding.buttonEditNewImage.setOnClickListener { ... } 使用 Lambda 表達式定義按鈕的點擊操作，使代碼更簡潔。
            data?.data?.let { imageUri ->
                Intent(applicationContext, EditImageActivity::class.java).also { editImageIntent ->
                    editImageIntent.putExtra(KEY_IMAGE_URI, imageUri)
                    startActivity(editImageIntent)
                }
                // 建立 EditImageActivity:
                // 創建一個新的 EditImageActivity，在其中可以使用
                // intent.getParcelableExtra<Uri>(KEY_IMAGE_URI) 獲取傳遞的圖片 URI。
            }
        }
    }

}
