


import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxEvent {
    private  var mSubjectEvent: PublishSubject<String> = PublishSubject.create()


    fun sendEvent(event:String) {
        mSubjectEvent.onNext(event)
    }

    fun getObservable(): Observable<String> {
        return mSubjectEvent
    }
}
