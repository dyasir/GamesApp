package com.unity3d.player.http;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxSchedulers {

    final static ObservableTransformer Stf = upstream -> upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());

    static <T> ObservableTransformer<T, T> applySchedulers() {
        return Stf;
    }

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> io_main() {
        return applySchedulers();
    }
}
