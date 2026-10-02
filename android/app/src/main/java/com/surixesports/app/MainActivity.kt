package com.surixesports.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Data Models ---
data class Tournament(
    val id: Int,
    val title: String,
    val map: String,
    val prizePool: String,
    val entryFee: String,
    val filledSlots: Int,
    val maxSlots: Int,
    val timing: String
)

data class LiveMatch(
    val id: Int,
    val title: String,
    val status: String,
    var roomId: String,
    var roomPass: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurixEsportsApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurixEsportsApp() {
    val darkColors = darkColorScheme(
        primary = Color(0xFFFF5722),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(colorScheme = darkColors) {
        var currentScreen by remember { mutableStateOf("tournaments") }
        var walletBalance by remember { mutableStateOf(450) }

        val tournaments = remember {
            mutableStateListOf(
                Tournament(1, "BGMI Sunday Squad War", "Erangel", "₹5,000", "₹50", 84, 100, "Today 8:00 PM"),
                Tournament(2, "Free Fire Solo Clash", "Bermuda", "₹2,000", "₹30", 42, 50, "Today 9:30 PM"),
                Tournament(3, "TDM Hardcore 4v4", "Warehouse", "₹1,000", "₹20", 16, 16, "Running")
            )
        }

        val liveMatches = remember {
            mutableStateListOf(
                LiveMatch(1, "BGMI Championship Finals", "LIVE NOW", "849201", "7788"),
                LiveMatch(2, "Free Fire Night Scrims", "STARTING SOON", "Locked", "Locked")
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "SURIX ESPORTS",
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF2C2C2C))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { currentScreen = "wallet" }
                        ) {
                            Text(text = "₹ $walletBalance", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF181818))
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF181818)) {
                    val navItems = listOf(
                        Triple("tournaments", "Tournaments", Icons.Default.SportsEsports),
                        Triple("live", "Live", Icons.Default.PlayCircle),
                        Triple("wallet", "Wallet", Icons.Default.AccountBalanceWallet),
                        Triple("profile", "Profile", Icons.Default.Person),
                        Triple("admin", "Admin", Icons.Default.AdminPanelSettings)
                    )
                    navItems.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label, fontSize = 10.sp) },
                            selected = currentScreen == route,
                            onClick = { currentScreen = route },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = Color(0xFF2A2A2A),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFF121212))
            ) {
                when (currentScreen) {
                    "tournaments" -> TournamentListScreen(tournaments)
                    "live" -> LiveMatchesScreen(liveMatches)
                    "wallet" -> WalletScreen(walletBalance) { added -> walletBalance += added }
                    "profile" -> ProfileScreen()
                    "admin" -> AdminScreen(
                        onAddTournament = { tournaments.add(it) },
                        liveMatches = liveMatches
                    )
                }
            }
        }
    }
}

// --- Screen 1: Tournaments List ---
@Composable
fun TournamentListScreen(list: List<Tournament>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        items(list) { tourney ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(tourney.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                        Badge(containerColor = Color(0xFF333333)) {
                            Text(tourney.map, modifier = Modifier.padding(4.dp), color = Color(0xFFFFB300))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("PRIZE POOL", fontSize = 11.sp, color = Color.Gray)
                            Text(tourney.prizePool, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50), fontSize = 15.sp)
                        }
                        Column {
                            Text("ENTRY FEE", fontSize = 11.sp, color = Color.Gray)
                            Text(tourney.entryFee, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        }
                        Column {
                            Text("TIME", fontSize = 11.sp, color = Color.Gray)
                            Text(tourney.timing, fontSize = 13.sp, color = Color.LightGray)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { tourney.filledSlots.toFloat() / tourney.maxSlots },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFFFF5722),
                        trackColor = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${tourney.filledSlots}/${tourney.maxSlots} joined", fontSize = 12.sp, color = Color.Gray)
                        Button(
                            onClick = { /* Join Logic */ },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                        ) {
                            Text("JOIN MATCH", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --- Screen 2: Live Matches ---
@Composable
fun LiveMatchesScreen(matches: List<LiveMatch>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(14.dp)) {
        items(matches) { match ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(match.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        Text(match.status, color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFF2C2C2C))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("ROOM ID", fontSize = 11.sp, color = Color.Gray)
                            Text(match.roomId, fontWeight = FontWeight.SemiBold, color = Color.Yellow, fontSize = 16.sp)
                        }
                        Column {
                            Text("PASSWORD", fontSize = 11.sp, color = Color.Gray)
                            Text(match.roomPass, fontWeight = FontWeight.SemiBold, color = Color.Yellow, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

// --- Screen 3: Wallet Screen ---
@Composable
fun WalletScreen(balance: Int, onAddMoney: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TOTAL BALANCE", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("₹ $balance", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color(0xFF4CAF50))
            }
        }

        Text("QUICK DEPOSIT", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf(50, 100, 200).forEach { amount ->
                Button(
                    onClick = { onAddMoney(amount) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("+ ₹$amount", color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /* Withdraw */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("WITHDRAW WINNINGS", fontWeight = FontWeight.Bold)
        }
    }
}

// --- Screen 4: User Profile ---
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF5722)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(46.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("SurixProGamer", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        Text("player@surixesports.in", fontSize = 13.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Played", color = Color.Gray, fontSize = 12.sp)
                    Text("28", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Won", color = Color.Gray, fontSize = 12.sp)
                    Text("11", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("K/D", color = Color.Gray, fontSize = 12.sp)
                    Text("3.4", fontWeight = FontWeight.Bold, color = Color(0xFFFFB300), fontSize = 16.sp)
                }
            }
        }
    }
}

// --- Screen 5: Admin Panel ---
@Composable
fun AdminScreen(onAddTournament: (Tournament) -> Unit, liveMatches: MutableList<LiveMatch>) {
    var title by remember { mutableStateOf("") }
    var entry by remember { mutableStateOf("") }
    var prize by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("ADMIN CONTROL PANEL", fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF5722), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(14.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("HOST NEW TOURNAMENT", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Game & Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = entry,
                        onValueChange = { entry = it },
                        label = { Text("Entry Fee (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = prize,
                        onValueChange = { prize = it },
                        label = { Text("Prize Pool (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                onAddTournament(
                                    Tournament(
                                        id = (10..999).random(),
                                        title = title,
                                        map = "Custom",
                                        prizePool = "₹$prize",
                                        entryFee = "₹$entry",
                                        filledSlots = 0,
                                        maxSlots = 100,
                                        timing = "Upcoming"
                                    )
                                )
                                title = ""
                                entry = ""
                                prize = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("PUBLISH TOURNAMENT", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
