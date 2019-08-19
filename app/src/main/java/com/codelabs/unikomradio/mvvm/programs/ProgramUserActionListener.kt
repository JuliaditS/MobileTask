package com.codelabs.unikomradio.mvvm.programs

import com.codelabs.unikomradio.data.model.Program

interface ProgramUserActionListener {
    fun onPlayRadio()

    fun onClickItem(program: Program)
}