package com.e.cryptocracy.utils

sealed class EventsChannel {
    class Success(val data: Any? = null) : EventsChannel()
    class Failure(val errorMsg: String = "", val data: Any? = null) : EventsChannel()
    class Loading(val loading: Boolean = false) : EventsChannel()
}
