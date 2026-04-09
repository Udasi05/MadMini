package com.example.myminiproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector,
    val activeIcon: ImageVector
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Screen.Dashboard.route, Icons.Outlined.Home, Icons.Filled.Home),
        BottomNavItem("Track", Screen.TrackMoney.route, Icons.Outlined.TrendingUp, Icons.Filled.TrendingUp),
        BottomNavItem("Learn", Screen.Learn.route, Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
        BottomNavItem("Schemes", Screen.Schemes.route, Icons.Outlined.AccountBalance, Icons.Filled.AccountBalance),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Outlined.Person, Icons.Filled.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isActive = currentRoute == item.route
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Dashboard.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isActive) Blue50 else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isActive) item.activeIcon else item.icon,
                        contentDescription = item.label,
                        tint = if (isActive) Blue600 else Gray400,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = item.label,
                    fontSize = 10.sp,
                    fontWeight = if (isActive) androidx.compose.ui.text.font.FontWeight.SemiBold else androidx.compose.ui.text.font.FontWeight.Normal,
                    color = if (isActive) Blue600 else Gray400
                )
            }
        }
    }
}
