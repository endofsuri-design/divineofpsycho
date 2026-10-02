package com.surixesports.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GameMode(
    val id: String,
    val title: String,
    val subtitle: String,
    val map: String,
    val prizePool: Int,
    val entryFee: Int,
    val spots: String,
    val accentColor: Color
)

data class PlayerRank(
    val rank: Int,
    val name: String,
    val wins: Int,
    val kills: Int,
    val points: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuriEsportsMasterApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuriEsportsMasterApp() {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("home") }
    var walletBalance by remember { mutableDoubleStateOf(500.0) }
    var selectedMatchToJoin by remember { mutableStateOf<GameMode?>(null) }

    val tournamentModes = listOf(
        GameMode("ff_max_1", "FF MAX 1", "Full Map Clash", "Bermuda", 2000, 30, "42/50 joined", Color(0xFFFF5722)),
        GameMode("cs_1v1", "CS 1V1", "Clash Squad Duel", "Custom 1v1", 100, 20, "1/2 joined", Color(0xFF9C27B0)),
        GameMode("lw_1v1", "LW 1V1", "Lone Wolf Deathmatch", "Iron Cage", 150, 25, "1/2 joined", Color(0xFF3F51B5)),
        GameMode("ff_max_2", "FF MAX 2", "Squad Hardcore", "Purgatory", 5000, 50, "84/100 joined", Color(0xFFE91E63)),
        GameMode("cs_2v2", "CS 2V2", "Squad TDM Duel", "Warehouse", 500, 40, "3/4 joined", Color(0xFFFF9800)),
        GameMode("lw_2v2", "LW 2V2", "Duo Showdown", "Lone Arena", 600, 50, "2/4 joined", Color(0xFF009688))
    )

    val topRankers = listOf(
        PlayerRank(1, "jinwoo", 98, 210, "5,676"),
        PlayerRank(2, "ashish07", 84, 180, "5,442"),
        PlayerRank(3, "mg_gamer00", 72, 140, "4,770"),
        PlayerRank(4, "Arulraj", 65, 120, "4,624"),
        PlayerRank(5, "RaviLord", 51, 95, "4,400"),
        PlayerRank(6, "minato007", 49, 88, "4,250")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Samurai Brand Logo Display
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "Suri Esports Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "DIVINE OF PSYCHO",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF111111),
                                fontSize = 15.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                "SURI ESPORTS ARENA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF1E1E1E),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { currentScreen = "wallet" }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "₹${walletBalance.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF4CAF50),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val items = listOf(
                    Triple("home", "Play", Icons.Default.PlayArrow),
                    Triple("rank", "Leaderboard", Icons.Default.Star),
                    Triple("wallet", "Wallet", Icons.Default.ShoppingCart),
                    Triple("refer", "Refer", Icons.Default.Share)
                )
                items.forEach { (tab, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        selected = currentScreen == tab,
                        onClick = { currentScreen = tab },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFE50914),
                            selectedTextColor = Color(0xFFE50914),
                            indicatorColor = Color(0xFFFFEBEE),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F7F9))
        ) {
            when (currentScreen) {
                "home" -> HomeScreenView(
                    modes = tournamentModes,
                    onJoinClick = { mode -> selectedMatchToJoin = mode }
                )
                "rank" -> RankScreenView(topRankers)
                "wallet" -> WalletScreenView(
                    balance = walletBalance,
                    onAddFunds = { add ->
                        walletBalance += add
                        Toast.makeText(context, "Added ₹$add successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onWithdraw = { withdrawAmt ->
                        if (withdrawAmt > walletBalance) {
                            Toast.makeText(context, "Insufficient Balance!", Toast.LENGTH_SHORT).show()
                        } else if (withdrawAmt < 50) {
                            Toast.makeText(context, "Minimum withdrawal is ₹50", Toast.LENGTH_SHORT).show()
                        } else {
                            walletBalance -= withdrawAmt
                            Toast.makeText(context, "Withdrawal Request of ₹$withdrawAmt Sent!", Toast.LENGTH_LONG).show()
                        }
                    }
                )
                "refer" -> ReferScreenView()
            }

            selectedMatchToJoin?.let { mode ->
                AlertDialog(
                    onDismissRequest = { selectedMatchToJoin = null },
                    title = { Text("Join ${mode.title}", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Map: ${mode.map}")
                            Text("Entry Fee: ₹${mode.entryFee}", fontWeight = FontWeight.Bold)
                            Text("Prize Pool: ₹${mode.prizePool}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Room ID & Password match shuru hone se 15 minute pehle update honge.", fontSize = 12.sp, color = Color.Gray)
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (walletBalance >= mode.entryFee) {
                                    walletBalance -= mode.entryFee
                                    Toast.makeText(context, "Joined ${mode.title}!", Toast.LENGTH_SHORT).show()
                                    selectedMatchToJoin = null
                                } else {
                                    Toast.makeText(context, "Insufficient balance! Please recharge wallet.", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                        ) {
                            Text("Confirm (₹${mode.entryFee})")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { selectedMatchToJoin = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreenView(modes: List<GameMode>, onJoinClick: (GameMode) -> Unit) {
    var selectedCategory by remember { mutableStateOf("TOURNAMENT") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFF6F00),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "WITHDRAWAL COMPLETE IN 12 HOURS ⚡",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Surface(
                        color = Color(0xFFE50914),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            " LIVE ESPORTS ",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "PLAY HARD, WIN BIG!",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                    Text(
                        "Custom Scrims & Tournament Platform",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        item {
            Text("My Matches", fontWeight = FontWeight.Bold, color = Color(0xFF222222), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MatchTabCard("Ongoing", Icons.Default.Refresh, Color(0xFF4CAF50), Modifier.weight(1f))
                MatchTabCard("Upcoming", Icons.Default.DateRange, Color(0xFF2196F3), Modifier.weight(1f))
                MatchTabCard("Completed", Icons.Default.CheckCircle, Color(0xFF757575), Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedCategory == "TOURNAMENT") Color(0xFF1E1E1E) else Color.Transparent)
                        .clickable { selectedCategory = "TOURNAMENT" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "TOURNAMENT",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedCategory == "TOURNAMENT") Color.White else Color.Gray,
                        fontSize = 13.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedCategory == "SOLO") Color(0xFF1E1E1E) else Color.Transparent)
                        .clickable { selectedCategory = "SOLO" }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "SOLO 1V1",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedCategory == "SOLO") Color.White else Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        val filteredList = if (selectedCategory == "SOLO") {
            modes.filter { it.title.contains("1V1") }
        } else {
            modes
        }

        items(filteredList) { mode ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(mode.accentColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = mode.accentColor)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(mode.title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                                Text(mode.subtitle, fontSize = 12.sp, color = Color.Gray)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF0F0F0)
                        ) {
                            Text(
                                mode.map,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF555555),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEEEEEE)))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("PRIZE POOL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Text("₹${mode.prizePool}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50))
                        }

                        Column {
                            Text("ENTRY FEE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Text("₹${mode.entryFee}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE50914))
                        }

                        Button(
                            onClick = { onJoinClick(mode) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 6.dp)
                        ) {
                            Text("JOIN", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mode.spots, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MatchTabCard(title: String, icon: ImageVector, color: Color, modifier: Modifier) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        modifier = modifier
            .clickable {
                Toast.makeText(context, "No $title Matches right now", Toast.LENGTH_SHORT).show()
            },
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
        }
    }
}

@Composable
fun WalletScreenView(
    balance: Double,
    onAddFunds: (Double) -> Unit,
    onWithdraw: (Double) -> Unit
) {
    var withdrawInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("TOTAL WALLET BALANCE", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "₹${String.format("%.2f", balance)}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("100% Safe & Instant Transfers", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }
        }

        item {
            Text("Quick Deposit / Add Funds", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(50.0, 100.0, 200.0).forEach { amount ->
                    Button(
                        onClick = { onAddFunds(amount) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE50914))
                    ) {
                        Text("+₹${amount.toInt()}", color = Color(0xFFE50914), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Withdraw Winnings", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Enter Amount to withdraw directly to Bank / UPI.", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = withdrawInput,
                        onValueChange = { withdrawInput = it },
                        label = { Text("Amount (₹)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            val amt = withdrawInput.toDoubleOrNull() ?: 0.0
                            onWithdraw(amt)
                            withdrawInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("REQUEST WITHDRAWAL", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ReferScreenView() {
    val context = LocalContext.current
    val referCode = "SURIX100"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFEBEE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE50914), modifier = Modifier.size(46.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("REFER & EARN ₹50", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color(0xFF1E1E1E))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Apne dosto ko invite karein. Jaise hi wo pehla match khelenge aap dono ko ₹50 bonus milega!",
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEEEEEE),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                "YOUR CODE: $referCode",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = Color(0xFF1E1E1E),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Suri Esports app join karo aur real custom matches khelo! Code: $referCode")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Referral Code"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("SHARE WITH FRIENDS", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RankScreenView(rankers: List<PlayerRank>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("TOP RANKED PLAYERS", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF1E1E1E))
            Text("Weekly tournament champions", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                PodiumColumn(rankers[1], 100.dp, Color(0xFF9E9E9E), "#2")
                PodiumColumn(rankers[0], 135.dp, Color(0xFFFFB300), "#1")
                PodiumColumn(rankers[2], 85.dp, Color(0xFF795548), "#3")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("All Rankings", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
        }

        items(rankers.drop(3)) { player ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#${player.rank}", fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.width(30.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEBEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(player.name.first().toString().uppercase(), fontWeight = FontWeight.Bold, color = Color(0xFFE50914))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(player.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${player.wins} Wins • ${player.kills} Kills", fontSize = 11.sp, color = Color.Gray)
                    }
                    Text("${player.points} pts", fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun PodiumColumn(player: PlayerRank, height: Dp, podiumColor: Color, badge: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(player.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(badge, fontWeight = FontWeight.Black, color = podiumColor, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(podiumColor),
            contentAlignment = Alignment.Center
        ) {
            Text("${player.points} pts", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}
