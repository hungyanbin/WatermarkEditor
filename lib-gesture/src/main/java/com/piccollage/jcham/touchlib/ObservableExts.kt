package com.piccollage.jcham.touchlib

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

fun <T> Observable<T>.pairwise(): Observable<Pair<T, T>> {
    return Observables.zip(this, this.skip(1)
    ) { a, b -> Pair(a, b) }
}

fun <T> Observable<T>.pairFirst() =
    Observables.combineLatest(
        take(1),
        skip(1))
    { a: T, b: T -> Pair(a, b) }

fun <IN, OUT> Observable<IN>.filterMap(f: (IN) -> OUT?): Observable<OUT> {
    class Wrapper(val inner: OUT?)
    return this
        .map    { Wrapper(f(it)) }
        .filter { it.inner != null }
        .map    { it.inner }
}
