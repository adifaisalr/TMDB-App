package com.adifaisalr.tmdbapplication.presentation.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

object NavigationUtils {
    /**
     * extension function to prevent java.lang.IllegalArgumentException: navigation destination XXX
     * is unknown to this NavController
     */
    fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
    }

    fun NavController.safeNavigate(route: String) {
        val destination = findDestination(route)
        destination?.let { navigate(route) }
    }

    fun NavController.safeNavigate(
        @IdRes currentDestinationId: Int,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        if (currentDestinationId == currentDestination?.id) {
            navigate(id, args)
        }
    }
}