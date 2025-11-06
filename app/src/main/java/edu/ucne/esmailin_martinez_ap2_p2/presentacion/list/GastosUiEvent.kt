package edu.ucne.esmailin_martinez_ap2_p2.presentacion.list

sealed class GastosUiEvent {
    data class Load(val id: Int) : GastosUiEvent()
    data class suplidorChanged(val value: String) : GastosUiEvent()
    data class fechaChanged(val value: String) : GastosUiEvent()
    data class ncfChanged(val value: String) : GastosUiEvent()
    data class itbisChanged(val value: Double) : GastosUiEvent()
    data class montoChanged(val value: Double) : GastosUiEvent()
    object Save : GastosUiEvent()
    object Delete : GastosUiEvent()
    object showSheet : GastosUiEvent()
    object hideSheet : GastosUiEvent()
    object userMessageShown : GastosUiEvent()
}
