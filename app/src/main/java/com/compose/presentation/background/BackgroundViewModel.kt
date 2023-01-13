package com.compose.presentation.background

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.data.RepositoryBgRem
import com.compose.data.models.BackgroundType
import com.compose.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackgroundViewModel @Inject constructor(
    private val repositoryBgRem: RepositoryBgRem
) : ViewModel() {

    val defualtBg = Background(
        id = "f1eae512-066d-42d6-a4cb-95d3c3bf7069",
        group = "color",
        color = "#FFFFFF",
        file_url = null,
        thumbnail_url = null,
        poster_url = null,
        small_thumbnail_url = null,
        small_poster_url = null,
        is_favorite = false
    )

    val bg = MutableLiveData<Background>()

    fun getBgEmpty() {
        bg.value = defualtBg
    }

    fun getCurrentBg(background: Background) {
        bg.value = background
    }

    var backgroundType: BackgroundType? = BackgroundType.COLOR

    var categoryId: String? = null

    private val _dataState = MutableLiveData<StateViewModel>()
    val dataState: LiveData<StateViewModel>
        get() = _dataState

    //    получение id фонов со страницы с лайками:
    var favoritesVideos = mutableSetOf<String>()

    var favoritesImages = mutableSetOf<String>()

    val favoriteImagesForFolders = repositoryBgRem.favoriteImagesAll

    val firstFiveFavoritesForEmpty = setOf(
        "392297e4-21ee-4c94-b289-25588aeee45e",
        "4e29a39f-3c5d-4651-b072-88fefd621d2c",
        "44e19d82-af1d-4eea-81e9-92431dd45e19",
        "2adfe6c4-dd01-4b6e-89a5-28c9adc4125e",
        "0d6df622-0e62-44a5-87d8-2fb32730906a"
    )

    val favoriteVideosForFolders = repositoryBgRem.favoriteVideosAll

    val firstFiveFavoritesVideosForEmpty = setOf(
        "6a9586b9-8af0-4f8e-bc29-22e608237cbc",
        "852a1193-f92c-42f9-999a-c99ce15dc494",
        "7333761b-c917-48db-a5ca-8ab31b60475b",
        "1536d199-96a3-48c2-a15e-0564925f541a",
        "d4c1a413-4051-424a-a1f0-687ea02d4ffa"
    )

    val favImagesList = mutableListOf<Background>()

    fun getFavoritesImagesForFolders(): List<Background> {
        viewModelScope.launch {
           val fav = repositoryBgRem.getFavoriteImagesAll(favoritesImages.toString())
            for(back in fav.results) {
                if(!favImagesList.contains(back)){
                    favImagesList.add(back)
                }
            }
        }
        return favImagesList
    }

    val favVideosList = mutableListOf<Background>()

    fun getFavoritesVideosForFolders() {
        viewModelScope.launch {
            val favoriteVideos = repositoryBgRem.getFavoriteVideosAll(favoritesVideos.toString())
            for(back in favoriteVideos.results) {
                if(!favVideosList.contains(back)){
                    favVideosList.add(back)
                }
            }
        }
    }

    fun getFavoritesImagesForFoldersIfListEmpty() {
        viewModelScope.launch {
            val fav = repositoryBgRem.getFavoriteImagesAll(firstFiveFavoritesForEmpty.toString())
            for(back in fav.results) {
                if(!favImagesList.contains(back)){
                    favImagesList.add(back)
                }
            }
        }
    }

    fun getFavoritesVideosForFoldersIfListEmpty() {
        viewModelScope.launch {
            val favVideos = repositoryBgRem.getFavoriteVideosAll(firstFiveFavoritesVideosForEmpty.toString())
            for(back in favVideos.results) {
                if(!favVideosList.contains(back)){
                    favVideosList.add(back)
                }
            }
        }
    }

    fun putDefualtIdsVideosToList() {
        favoritesVideos.addAll(firstFiveFavoritesVideosForEmpty)
    }

    fun putDefualtIdsImagesToList() {
        favoritesImages.addAll(firstFiveFavoritesForEmpty)
    }

    fun saveVideoIds() {
        viewModelScope.launch {
            repositoryBgRem.saveVideoIds(favoritesVideos)
        }
    }

    fun saveImageIds() {
        viewModelScope.launch {
            repositoryBgRem.saveImageIds(favoritesImages)
        }
    }

    fun getVideoIdsFromStore() {
        val list = repositoryBgRem.getVideoIds()
        if (list != null) {
            favoritesVideos.addAll(list)
        }
    }

    fun getImageIdsFromStore() {
        val list = repositoryBgRem.getImageIds()
        if (list != null) {
            favoritesImages.addAll(list)
        }
    }

    fun getFirstFiveFavoriteImages() {
        backgroundType = BackgroundType.IMAGE
    }

    fun getFirstFiveFavoriteVideos() {
        backgroundType = BackgroundType.VIDEO
    }

    fun getColors() {
        backgroundType = BackgroundType.COLOR
    }

    fun getYourBg() {
        backgroundType = BackgroundType.YOURBG
    }

    var imageUri: Uri? = null
    var videoUri: Uri? = null
    var yourBgType: String? = null

    val videoFromCategory = repositoryBgRem.videoFromCategory

    fun getVideoFromCategory(id: String) {
        try {
            viewModelScope.launch {
                repositoryBgRem.getVideoFromCategory(id)
                categoryId = id
            }
        } catch (e: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    val imageFromCategory = repositoryBgRem.imageFromCategory

    fun getImageFromCategory(id: String) {
        try {
            viewModelScope.launch {
                repositoryBgRem.getImageFromCategory(id)
                categoryId = id
            }
        } catch (e: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    val favoriteVideos = repositoryBgRem.favoriteVideos

    fun getFavoriteVideos() {
        try {
            viewModelScope.launch {
                repositoryBgRem.getFavoriteVideos()
            }
        } catch (e: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    val favoriteImages = repositoryBgRem.favoriteImages

    fun getFavoriteImages() {
        try {
            viewModelScope.launch {
                repositoryBgRem.getFavoriteImages()
            }
        } catch (e: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    private val _uiBackTransparentState = MutableStateFlow(ReplyBackColorState(loading = true))
    val uiBackTransparentState: StateFlow<ReplyBackColorState> = _uiBackTransparentState

    fun getTransparent() {
        backgroundType = BackgroundType.TRANSPARENT
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBgRem.getTransparent()
                .catch { ex ->
                    _uiBackTransparentState.value = ReplyBackColorState(error = ex.message)
                }
                .collect { transparent ->
                    _uiBackTransparentState.value = ReplyBackColorState(backColor = transparent)
                }
        }
    }

    private val _uiCategoryVideoState = MutableStateFlow(ReplyCategoryState(loading = true))
    val uiCategoryVideoState: StateFlow<ReplyCategoryState> = _uiCategoryVideoState

    fun getVideoCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBgRem.getVideoCategories()
                .catch { ex ->
                    _uiCategoryVideoState.value = ReplyCategoryState(error = ex.message)
                }
                .collect { categories ->
                    _uiCategoryVideoState.value = ReplyCategoryState(category = categories)
                }
        }
    }

    private val _uiCategoryImageState = MutableStateFlow(ReplyCategoryState(loading = true))
    val uiCategoryImageState: StateFlow<ReplyCategoryState> = _uiCategoryImageState

    fun getImageCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBgRem.getImageCategories()
                .catch { ex ->
                    _uiCategoryImageState.value = ReplyCategoryState(error = ex.message)
                }
                .collect { categories ->
                    _uiCategoryImageState.value = ReplyCategoryState(category = categories)
                }
        }
    }

}
