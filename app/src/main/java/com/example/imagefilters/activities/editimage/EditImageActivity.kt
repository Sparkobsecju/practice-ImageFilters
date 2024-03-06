package com.example.imagefilters.activities.editimage

// import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.imagefilters.activities.main.MainActivity
import com.example.imagefilters.adapters.ImageFiltersAdapter
import com.example.imagefilters.data.ImageFilter
import com.example.imagefilters.databinding.ActivityEditImageBinding
import com.example.imagefilters.listeners.ImageFilterListener
import com.example.imagefilters.utilities.displayToast
import com.example.imagefilters.utilities.show
import com.example.imagefilters.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    private lateinit var binding: ActivityEditImageBinding
    // 使用 Koin 依賴注入取得 EditImageViewModel 實例
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    // Image bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()


    /**
     * 在 onCreate 方法中調用 setListeners() 和 displayImagePreview() 來設置監聽器並顯示圖片預覽。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用 View Binding 初始化布局
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 設置監聽器及顯示圖片預覽
        setListeners()
        //displayImagePreview()
        prepareImagePreview()

        // 設置觀察者監聽 ViewModel 的數據變化
        setupObservers()


    }

    // 設置觀察者，觀察 ViewModel 中 imagePreviewUiState 的變化
    private fun setupObservers() {
        // 觀察 LiveData 對象的變化
        viewModel.imagePreviewUiState.observe(this, {
            // Elvis 運算符 ?: 用於簡化 null 檢查
            val dataState = it ?: return@observe // 如果 dataState 為 null，則退出 observe
            // 更新 UI 根據數據狀態
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            // 使用 let 函數處理非空的情況
            dataState.bitmap?.let { bitmap ->
                // For the first time 'filtered image = original image'
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }
                //binding.imagePreview.setImageBitmap(bitmap)
                //binding.imagePreview.show()
                //viewModel.loadImageFilters(bitmap)

            } ?: kotlin.run {
                // Kotlin 的 run 函數，用於執行一段代碼區塊
                dataState.error?.let { error ->
                    // 如果有錯誤信息，顯示 Toast
                    displayToast(error)
                }
            }
        })
        viewModel.imageFiltersUiState.observe(this, {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
        filteredBitmap.observe(this, { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })
    }

    // 準備圖片預覽
    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    /*
    * displayImagePreview() 方法中，從 Intent 中獲取圖片 URI，
    * 然後使用 ContentResolver 打開圖片流，解碼並顯示圖片預覽。
    * */
    // 顯示接收到的圖片預覽
//    private fun displayImagePreview() {
//        // 從 Intent 中獲取圖片 URI
//        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
//            // 使用 ContentResolver 開啟圖片流
//            val inputStream = contentResolver.openInputStream(imageUri)
//
//            // 解碼流並顯示圖片預覽
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//            binding.imagePreview.setImageBitmap(bitmap)
//            binding.imagePreview.visibility = View.VISIBLE
//        }
//    }

    // 設置返回按鈕的點擊監聽器
    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        /*
        * This will show original image when we long click the ImageView util we release
        * click, so that we can see difference between original image and filtered image
        * */
        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}