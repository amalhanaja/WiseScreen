package com.amalcodes.wisescreen.presentation

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


sealed class UIEvent {

    data class RestoreUIState(val uiState: UIState?) : UIEvent()

    abstract class Abstract : UIEvent()

    fun unhandled(): Nothing {
        throw IllegalStateException("Unhandled Event: $this")
    }
}

sealed class UIState {
    object Initial : UIState()
    object Loading : UIState()
    object Refreshing : UIState()
    abstract class Abstract : UIState()

    sealed class UIFailure(
        val message: String? = null,
        val cause: Throwable? = null
    ) : UIState() {
        object NoInternet : UIFailure("no_internet")
        class Unknown(cause: Throwable) : UIFailure("unknown", cause)
        object NoData : UIFailure("no_data")
        abstract class Abstract(
            message: String?,
            cause: Throwable?
        ) : UIFailure(message, cause)
    }

}