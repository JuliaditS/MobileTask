package com.codelabs.newunikomradio.mvvm

interface StateListener  {
    fun isPlayingRadio(): Boolean?
    fun setStatePlayingRadio(state: Boolean?)
}