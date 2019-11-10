package com.cardinalblue.common

class Reader<C, A>(val runBy: (C) -> A) {

    fun <B> map(mapper: (A) -> B): Reader<C, B> {
        return Reader { context -> mapper(runBy(context)) }
    }

    fun <B> flatMap(mapper: (A) -> Reader<C, B>): Reader<C, B> {
        return Reader { context ->
            mapper(runBy(context))
                .runBy(context)
        }
    }
}