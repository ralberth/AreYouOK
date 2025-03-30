package org.ralberth.areyouok.navigation

import androidx.navigation.NavHostController


private object RuokScreens {
    const val COUNTDOWN_SCREEN = "countdown"
    const val SETTINGS_SCREEN = "settings"
    const val MESSAGES_SCREEN = "messages"
}


//object RuokDestinationsArgs {
//    const val USER_MESSAGE_ARG = "userMessage"
//    const val TASK_ID_ARG = "taskId"
//    const val TITLE_ARG = "title"
//}


object RuokDestinations {
    const val COUNTDOWN_ROUTE = RuokScreens.COUNTDOWN_SCREEN   // "$COUNTDOWN_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val SETTINGS_ROUTE = RuokScreens.SETTINGS_SCREEN
    const val MESSAGES_ROUTE = RuokScreens.MESSAGES_SCREEN   // "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
}


class RuokNavigationActions(private val navController: NavHostController) {

    fun navigateToCountdown() {
        navController.navigate(RuokDestinations.COUNTDOWN_ROUTE)
    }

    fun navigateToSettings() {
        navController.navigate(RuokDestinations.SETTINGS_ROUTE)
    }

    fun navigateToMessages() {
        navController.navigate(RuokDestinations.MESSAGES_ROUTE)
    }
//            COUNTDOWN_SCREEN.let {
//                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
//            }
//        ) {
//            popUpTo(navController.graph.findStartDestination().id) {
//                inclusive = !navigatesFromDrawer
//                saveState = navigatesFromDrawer
//            }
//            launchSingleTop = true
//            restoreState = navigatesFromDrawer
//        }
//    }

//    fun navigateToStatistics() {
//        navController.navigate(TodoDestinations.STATISTICS_ROUTE) {
//            // Pop up to the start destination of the graph to
//            // avoid building up a large stack of destinations
//            // on the back stack as users select items
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
//            // Avoid multiple copies of the same destination when
//            // reselecting the same item
//            launchSingleTop = true
//            // Restore state when reselecting a previously selected item
//            restoreState = true
//        }
//    }
//
//    fun navigateToTaskDetail(taskId: String) {
//        navController.navigate("$TASK_DETAIL_SCREEN/$taskId")
//    }
//
//    fun navigateToAddEditTask(title: Int, taskId: String?) {
//        navController.navigate(
//            "$ADD_EDIT_TASK_SCREEN/$title".let {
//                if (taskId != null) "$it?$TASK_ID_ARG=$taskId" else it
//            }
//        )
//    }
}