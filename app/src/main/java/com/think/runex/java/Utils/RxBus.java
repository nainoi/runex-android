package com.think.runex.java.Utils;

import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static final SparseArray<PublishSubject<Object>> sSubjectMap = new SparseArray<>();
    private static final Map<Object, CompositeDisposable> sSubscriptionsMap = new HashMap<>();
    public static final int SUBJECT = 0;
    public static final int ANOTHER_SUBJECT = 1;

    @Retention(SOURCE)
    @IntDef({SUBJECT, ANOTHER_SUBJECT})
    @interface Subject {
    }

    private RxBus() {
        // hidden constructor
    }

    /**
     * Get the subject or create it if it's not already in memory.
     */
    @NonNull
    private static PublishSubject<Object> getSubject(@Subject int subjectCode) {
        PublishSubject<Object> subject = sSubjectMap.get(subjectCode);
        if (subject == null) {
            subject = PublishSubject.create();
            subject.subscribeOn(AndroidSchedulers.mainThread());
            sSubjectMap.put(subjectCode, subject);
        }
        return subject;
    }

    /**
     * Get the CompositeDisposable or create it if it's not already in memory.
     */
    @NonNull
    private static CompositeDisposable getCompositeDisposable(@NonNull Object object) {
        CompositeDisposable compositeDisposable = sSubscriptionsMap.get(object);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            sSubscriptionsMap.put(object, compositeDisposable);
        }
        return compositeDisposable;
    }

    /**
     * Subscribe to the specified subject and listen for updates on that subject. Pass in an object to associate
     * your registration with, so that you can unsubscribe later.
     * <br/><br/>
     * <b>Note:</b> Make sure to call {@link RxBus#unregister(Object)} to avoid memory leaks.
     */
    public static void subscribe(@Subject int subject, @NonNull Object lifecycle, @NonNull Consumer<Object> consumer) {
        Disposable disposable = getSubject(subject).subscribe(consumer);
        Log.w(TAG, "Subscribe: " + lifecycle.toString());
        getCompositeDisposable(lifecycle).add(disposable);
    }

    /**
     * Unregisters this object from the bus, removing all subscriptions.
     * This should be called when the object is going to go out of memory.
     */
    public static void unregister(@NonNull Object lifecycle) {
        //We have to remove the compositeDisposable from the map, because once you dispose it can't be used anymore
        CompositeDisposable compositeDisposable = sSubscriptionsMap.get(lifecycle);
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            sSubscriptionsMap.remove(lifecycle);
            Log.e(TAG, "Un subscribe: " + lifecycle.toString());
            compositeDisposable = null;
        }
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    public static void publish(@Subject int subject, @NonNull Object message) {
        Log.w(TAG, "Publish: " + message.getClass().getSimpleName());
        getSubject(subject).onNext(message);
    }
}
