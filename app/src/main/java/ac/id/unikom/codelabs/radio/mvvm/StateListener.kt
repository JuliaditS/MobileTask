package ac.id.unikom.codelabs.radio.mvvm

interface StateListener  {
    fun isPlayingRadio(): Boolean?
    fun setStatePlayingRadio(state: Boolean?)
}