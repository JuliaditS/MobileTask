package com.codelabs.newunikomradio.utilities.helper

open class Event<out T>(private val content: T){

    var hasBeenEnabled = false
        private set // allow external read but not write

    /*
    * Returns the content and prevents its use again
    * */

    fun getContentIfNotHandled(): T?{
        return if (hasBeenEnabled){
            null
        } else {
            hasBeenEnabled = true
            content
        }
    }

    /*
    * Returns the content, even if it's already been handled.
    * */
    fun peekContent(): T = content

}