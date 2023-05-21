package com.e.cryptocracy.appEvents

import com.e.cryptocracy.firebaseDatabase.UpdateFavouriteIds
import com.e.cryptocracy.redux.ApplicationState
import javax.inject.Inject

class OnFavouriteCoinEvent @Inject constructor(
    private val updateFavouriteIds: UpdateFavouriteIds,
) {
    fun invoke(state: ApplicationState, selectedId: String): ApplicationState {
        val currentFavIds = state.favouriteCoinIds
        val neeFavIds = if (currentFavIds.contains(selectedId)) {
            currentFavIds.filter { it != selectedId }.toSet()
        } else {
            currentFavIds + setOf(selectedId)
        }
        updateFavouriteIds.update(selectedId, add = !currentFavIds.contains(selectedId))
        return state.copy(
            favouriteCoinIds = neeFavIds
        )
    }
}