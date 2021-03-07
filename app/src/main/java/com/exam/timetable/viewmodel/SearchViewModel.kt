package com.exam.timetable.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.timetable.common.BaseViewModel
import com.exam.timetable.livedata.Event
import com.exam.timetable.model.data.LecureDataParent
import com.exam.timetable.model.repository.search.SearchRepository
import com.exam.timetable.utils.Const
import com.exam.timetable.utils.Resource
import com.exam.timetable.utils.isNetworkConnected
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class SearchViewModel(private val searchRepository: SearchRepository) :  BaseViewModel()  {
    private val textChangeSubject = PublishSubject.create<String>()

    init {
        textChangeSubject.onNext("")
    }

    private val _itemLiveData = MutableLiveData<Event<Resource<LecureDataParent>>>()
    val itemLiveData: LiveData<Event<Resource<LecureDataParent>>> get() = _itemLiveData
    private val _itemCodeSearchLiveData = MutableLiveData<Event<Resource<LecureDataParent>>>()
    val itemCodeSearchLiveData: LiveData<Event<Resource<LecureDataParent>>> get() = _itemCodeSearchLiveData

    private val _itemNameSearchLiveData = MutableLiveData<Event<Resource<LecureDataParent>>>()
    val itemNameSearchLiveData: LiveData<Event<Resource<LecureDataParent>>> get() = _itemNameSearchLiveData

    fun getAllLectures() {
        if (!isNetworkConnected())
            return

        compositeDisposable.add(
            searchRepository.requestSearchData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    val res = Event(Resource.success(it))
                    _itemLiveData.postValue(res)

                }, {
                    val res = Event(Resource.error(it.message.toString(), null))
                    _itemLiveData.postValue(res)
                })
        )
    }

    fun getLecturesToCode(code:String) {
        if (!isNetworkConnected())
            return

        compositeDisposable.add(
            searchRepository.requestSearchDataCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    val res = Event(Resource.success(it))
                    _itemCodeSearchLiveData.postValue(res)

                }, {
                    val res = Event(Resource.error(it.message.toString(), null))

                    _itemCodeSearchLiveData.postValue(res)
                })
        )
    }

    fun getLecturesToName(name:String) {
        if (!isNetworkConnected())
            return

        compositeDisposable.add(
            searchRepository.requestSearchDataName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    val res = Event(Resource.success(it))
                    _itemNameSearchLiveData.postValue(res)

                }, {
                    val res = Event(Resource.error(it.message.toString(), null))
                    _itemNameSearchLiveData.postValue(res)
                })
        )
    }


    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int){
        if (s.isNullOrEmpty())
            textChangeSubject.onNext("")
        else
            textChangeSubject.onNext(s.toString())
    }



    fun getSearchKeyChangeObserver(): Observable<String> {
        return textChangeSubject.debounce(Const.EMIT_INTERVAL, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
    }


    override fun onCleared() {
        super.onCleared()
    }


}