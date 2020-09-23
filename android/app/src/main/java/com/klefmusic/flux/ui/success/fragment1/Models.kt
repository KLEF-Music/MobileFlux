package com.klefmusic.flux.ui.success.fragment1

import com.klefmusic.rxfluxcore.*
import com.klefmusic.rxfluxcore.models.State
import com.klefmusic.rxfluxcore.models.UiEvent


sealed class Fragment1Events : UiEvent {
    object clickedOpen : Fragment1Events()
}

data class Fragment1State(val loading: Boolean = false) : State


sealed class Fragment1Effects : Effect {
    object openFragment2 : Fragment1Effects()
}

