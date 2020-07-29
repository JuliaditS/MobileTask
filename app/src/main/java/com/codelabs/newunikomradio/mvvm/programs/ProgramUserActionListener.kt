package com.codelabs.newunikomradio.mvvm.programs

import com.codelabs.newunikomradio.data.model.Program

interface ProgramUserActionListener {
    fun onPlayRadio()

    fun onClickItem(program: Program)
}