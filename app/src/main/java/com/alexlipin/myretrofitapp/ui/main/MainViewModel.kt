package com.alexlipin.myretrofitapp.ui.main

import androidx.lifecycle.*

import com.alexlipin.myretrofitapp.repository.dao.NumberDao
import com.alexlipin.myretrofitapp.repository.dao.NumberRepository
import com.alexlipin.myretrofitapp.repository.network.Repository
import com.alexlipin.myretrofitapp.model.FactItem

import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dao: NumberDao
    ) : ViewModel() {

    companion object {
        const val ERR_NO = 0
        const val ERR_SIZE = 1
        const val ERR_LESS_ZERO = 2
    }

    private val repository = Repository()
    private val dataBase = NumberRepository(dao)
    private val errorInputNumber = MutableLiveData(ERR_NO)
    private val progressBarIsVisible = MutableLiveData(false)
    private val toastNoInternetConnection = MutableLiveData(false)

    private var items: LiveData<List<FactItem>> = dataBase.allItems.asLiveData()

    fun receiveNumber(enteredNumber: String) {
        setInputTextError(ERR_NO)
        val value: Int? = enteredNumber.toIntOrNull()
        if (value == null) {
            setInputTextError(ERR_SIZE)
        } else if (value < 0) {
            setInputTextError(ERR_LESS_ZERO)
        } else if (value >= 0) {
            progressBarIsVisible.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val item: Response<FactItem> = repository.getNumberFact(value)
                withContext(Dispatchers.Main) {
                    if (item.body() != null) {
                        insert(item.body()!!)
                    }
                    progressBarIsVisible.value = false
                }
            }
        }
    }

    fun receiveRandomNumber() {
        progressBarIsVisible.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val item: Response<FactItem> = repository.getRandomNumberFact()
            withContext(Dispatchers.Main) {
                if (item.body() != null) {
                    insert(item.body()!!)
                }
                progressBarIsVisible.value = false
            }
        }
    }

    private suspend fun insert(item: FactItem) {
        dataBase.insert(item)
    }

    fun getItems(): LiveData<List<FactItem>> {
        return items
    }

    fun getButtonState(): LiveData<Boolean> {
        return progressBarIsVisible
    }

    fun getToastInternetErrorState(): LiveData<Boolean> {
        return toastNoInternetConnection
    }

    fun setToastInternetError() {
        toastNoInternetConnection.value = true
        toastNoInternetConnection.value = false
    }

    private fun setInputTextError(i: Int) {
        errorInputNumber.value = i
    }

    val inputTextErrorsStatus: LiveData<Int>
        get() = errorInputNumber
}