package ac.id.unikom.codelabs.radio.mvvm.programs

import ac.id.unikom.codelabs.radio.data.model.Program

interface ProgramUserActionListener {
    fun onPlayRadio()

    fun onClickItem(program: Program)
}