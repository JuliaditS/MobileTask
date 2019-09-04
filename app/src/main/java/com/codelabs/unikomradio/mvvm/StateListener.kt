package com.codelabs.unikomradio.mvvm

interface StateListener  {
    fun isPlayingRadio(): Boolean?
    fun setStatePlayingRadio(state: Boolean?)
}