package com.alexlipin.myretrofitapp.ui.description

import androidx.lifecycle.*

import com.alexlipin.myretrofitapp.repository.dao.NumberDao
import com.alexlipin.myretrofitapp.model.FactItem

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(
    private val dao: NumberDao
) : ViewModel() {

    private var items: LiveData<FactItem> = MutableLiveData()
    private var isDatabaseError: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getItemById(id: Int): FactItem {
        var item: FactItem
         if (id == -1) {
             item = FactItem(number = "")
             viewModelScope.launch {
                 setDataBaseError()
             }
        }
         else {
            items = dao.getNumberById(id.toString()).asLiveData()
             item = items.value ?: FactItem(number = "")
        }
        return item
    }

    fun getItem(): LiveData<FactItem> {
        return items
    }

    fun getDataBaseErrorState(): LiveData<Boolean> {
        return isDatabaseError
    }

    private suspend fun setDataBaseError() {
        isDatabaseError.value = true
        delay(500)
        isDatabaseError.value = false
    }
}