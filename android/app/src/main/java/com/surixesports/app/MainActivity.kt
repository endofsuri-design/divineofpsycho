package com.surixesports.app

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
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
    val entryFee: Int,
    val winnerPrize: String,
    val prizePool: Int,
    val spots: String,
    val prizeDistribution: String,
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
    var walletBalance by remember { mutableDoubleStateOf(200.0) }
    var adminProfitPool by remember { mutableDoubleStateOf(110.0) }
    var selectedMatchToJoin by remember { mutableStateOf<GameMode?>(null) }
    var showDepositDialog by remember { mutableStateOf(false) }
    var depositSelectedAmount by remember { mutableStateOf(25.0) }

    val adminUpi = "x1prince@fam"

    val tournamentModes = listOf(
        GameMode(
            id = "ff_full_solo",
            title = "FF MAX FULL MAP",
            subtitle = "48 Players Solo Battle",
            map = "Bermuda",
            entryFee = 10,
            winnerPrize = "1st: ₹200",
            prizePool = 380,
            spots = "38/48 joined",
            prizeDistribution = "1st: ₹200 | 2nd: ₹100 | 3rd: ₹50 | 4th-5th: ₹15 (Host: ₹100)",
            accentColor = Color(0xFFFF5722)
        ),
        GameMode(
            id = "cs_1v1",
            title = "CS 1V1 DUEL",
            subtitle = "Clash Squad Quick 1v1",
            map = "Custom Room",
            entryFee = 25,
            winnerPrize = "Winner: ₹40",
            prizePool = 40,
            spots = "1/2 joined",
            prizeDistribution = "Winner: ₹40 (Host Fee: ₹10)",
            accentColor = Color(0xFF9C27B0)
        ),
        GameMode(
            id = "lw_1v1",
            title = "LONE WOLF 1V1",
            subtitle = "Iron Cage Deathmatch",
            map = "Iron Cage",
            entryFee = 25,
            winnerPrize = "Winner: ₹40",
            prizePool = 40,
            spots = "1/2 joined",
            prizeDistribution = "Winner: ₹40 (Host Fee: ₹10)",
            accentColor = Color(0xFF3F51B5)
        ),
        GameMode(
            id = "cs_2v2",
            title = "CS 2V2 DUO",
            subtitle = "2 Teams vs 2 Teams",
            map = "Warehouse",
            entryFee = 30,
            winnerPrize = "Winner Team: ₹50",
            prizePool = 50,
            spots = "1/2 teams",
            prizeDistribution = "Winning Team: ₹50 (Host Fee: ₹10)",
            accentColor = Color(0xFFFF9800)
        ),
        GameMode(
            id = "lw_2v2",
            title = "LONE WOLF 2V2",
            subtitle = "Duo Cage Fight",
            map = "Lone Arena",
            entryFee = 50,
            winnerPrize = "Winner Team: ₹80",
            prizePool = 80,
            spots = "1/2 teams",
            prizeDistribution = "Winning Team: ₹80 (Host Fee: ₹20)",
            accentColor = Color(0xFF009688)
        ),
        GameMode(
            id = "ff_squad",
            title = "FF MAX SQUAD",
            subtitle = "12 Squads / 48 Players",
            map = "Purgatory",
            entryFee = 40,
            winnerPrize = "1st Squad: ₹240",
            prizePool = 360,
            spots = "9/12 squads",
            prizeDistribution = "1st Squad: ₹240 | 2nd Squad: ₹120 (Host Profit: ₹120)",
            accentColor = Color(0xFFE91E63)
        )
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
                            .clickable {
                                depositSelectedAmount = 25.0
                                showDepositDialog = true
                            }
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
                    Triple("refer", "Refer", Icons.Default.Share),
                    Triple("support", "Support", Icons.Default.Call)
                )
                items.forEach { (tab, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 10.sp) },
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
                    adminProfit = adminProfitPool,
                    adminUpi = adminUpi,
                    onOpenDeposit = { amt ->
                        depositSelectedAmount = amt
                        showDepositDialog = true
                    },
                    onWithdraw = { withdrawAmt, userUpi ->
                        if (withdrawAmt > walletBalance) {
                            Toast.makeText(context, "Insufficient Balance!", Toast.LENGTH_SHORT).show()
                        } else if (withdrawAmt < 30) {
                            Toast.makeText(context, "Minimum withdrawal is ₹30", Toast.LENGTH_SHORT).show()
                        } else {
                            walletBalance -= withdrawAmt
                            Toast.makeText(context, "₹$withdrawAmt withdrawal requested to $userUpi!", Toast.LENGTH_LONG).show()
                        }
                    }
                )
                "refer" -> ReferScreenView()
                "support" -> SupportScreenView()
            }

            // Tournament Join Confirmation Dialog
            selectedMatchToJoin?.let { mode ->
                AlertDialog(
                    onDismissRequest = { selectedMatchToJoin = null },
                    title = { Text("Join ${mode.title}", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Map: ${mode.map}", fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Entry Fee: ₹${mode.entryFee}", fontWeight = FontWeight.Bold, color = Color(0xFFE50914))
                            Text("Winner Prize: ${mode.winnerPrize}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Prize Breakdown:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text(mode.prizeDistribution, fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Room ID & Pass will be provided 15 min before match.", fontSize = 11.sp, color = Color(0xFF1E88E5))
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (walletBalance >= mode.entryFee) {
                                    walletBalance -= mode.entryFee
                                    adminProfitPool += (mode.entryFee * 0.20)
                                    Toast.makeText(context, "Joined ${mode.title}! Fee deducted.", Toast.LENGTH_SHORT).show()
                                    selectedMatchToJoin = null
                                } else {
                                    Toast.makeText(context, "Insufficient balance! Please add ₹${mode.entryFee}", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                        ) {
                            Text("Pay ₹${mode.entryFee} & Join")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { selectedMatchToJoin = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Direct UPI Payment Deposit Modal
            if (showDepositDialog) {
                DepositUpiDialog(
                    amount = depositSelectedAmount,
                    adminUpi = adminUpi,
                    onDismiss = { showDepositDialog = false },
                    onPaymentSuccess = { utr ->
                        walletBalance += depositSelectedAmount
                        Toast.makeText(context, "₹${depositSelectedAmount.toInt()} deposited! Ref: $utr", Toast.LENGTH_LONG).show()
                        showDepositDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun DepositUpiDialog(
    amount: Double,
    adminUpi: String,
    onDismiss: () -> Unit,
    onPaymentSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var utrNumber by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add ₹${amount.toInt()} via UPI", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pay directly to Official Admin UPI ID:", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF0F4F8),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("ADMIN UPI ID", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(adminUpi, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF0F2027))
                        }
                        Button(
                            onClick = {
                                clipboard.setText(AnnotatedString(adminUpi))
                                Toast.makeText(context, "UPI ID Copied!", Toast.LENGTH_SHORT).show()
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
                        ) {
                            Text("Copy", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val upiUri = Uri.parse("upi://pay?pa=$adminUpi&pn=SuriEsports&am=${amount.toInt()}&cu=INR")
                        val upiIntent = Intent(Intent.ACTION_VIEW, upiUri)
                        try {
                            context.startActivity(Intent.createChooser(upiIntent, "Pay with"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "No UPI app found. Please copy UPI ID manually.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("OPEN UPI APPS (GPay / PhonePe)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = utrNumber,
                    onValueChange = { utrNumber = it },
                    label = { Text("Enter 12-digit UTR / Ref No.") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (utrNumber.trim().length >= 4) {
                        onPaymentSuccess(utrNumber.trim())
                    } else {
                        Toast.makeText(context, "Please enter valid Ref / UTR number", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
            ) {
                Text("Verify & Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HomeScreenView(modes: List<GameMode>, onJoinClick: (GameMode) -> Unit) {
    var selectedCategory by remember { mutableStateOf("ALL") }

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
                        "FAIR PLAY: 100% INSTANT UPI WITHDRAWAL ⚡",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
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
                            " TOURNAMENT ARENA ",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "LOW ENTRY • BIG WINNINGS",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp
                    )
                    Text(
                        "Fair Scrims & Tournament Platform",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
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
                listOf("ALL", "SOLO 1V1", "DUO 2V2", "FULL MAP").forEach { tab ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedCategory == tab) Color(0xFF1E1E1E) else Color.Transparent)
                            .clickable { selectedCategory = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            tab,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedCategory == tab) Color.White else Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        val filteredList = when (selectedCategory) {
            "SOLO 1V1" -> modes.filter { it.title.contains("1V1") }
            "DUO 2V2" -> modes.filter { it.title.contains("2V2") }
            "FULL MAP" -> modes.filter { it.title.contains("FULL MAP") || it.title.contains("SQUAD") }
            else -> modes
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
                                Text(mode.title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                                Text(mode.subtitle, fontSize = 11.sp, color = Color.Gray)
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

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEEEEEE)))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("WINNER PRIZE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Text(mode.winnerPrize, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50))
                            Text("Pool: ₹${mode.prizePool}", fontSize = 10.sp, color = Color.Gray)
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

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(mode.spots, fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun WalletScreenView(
    balance: Double,
    adminProfit: Double,
    adminUpi: String,
    onOpenDeposit: (Double) -> Unit,
    onWithdraw: (Double, String) -> Unit
) {
    val context = LocalContext.current
    var withdrawInput by remember { mutableStateOf("") }
    var userUpiInput by remember { mutableStateOf("") }

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
                    Text("YOUR AVAILABLE WALLET BALANCE", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "₹${String.format("%.2f", balance)}",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF2C2C2C)
                    ) {
                        Text(
                            "Admin Profit Pool: ₹${adminProfit.toInt()} (UPI: $adminUpi)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFB300),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Text("Add Funds to Wallet (Instant UPI)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(10.0, 25.0, 50.0, 100.0).forEach { amount ->
                    Button(
                        onClick = { onOpenDeposit(amount) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE50914))
                    ) {
                        Text("+₹${amount.toInt()}", color = Color(0xFFE50914), fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Direct Bank / UPI transfer within 12 hours.", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = userUpiInput,
                        onValueChange = { userUpiInput = it },
                        label = { Text("Your UPI ID (e.g. name@paytm)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = withdrawInput,
                        onValueChange = { withdrawInput = it },
                        label = { Text("Amount (₹ Min 30)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val amt = withdrawInput.toDoubleOrNull() ?: 0.0
                            if (userUpiInput.trim().isEmpty()) {
                                Toast.makeText(context, "Please enter your UPI ID first!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            onWithdraw(amt, userUpiInput.trim())
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
    val referCode = "SURI2026"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFEBEE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE50914), modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("REFER FRIENDS & WIN BONUS", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color(0xFF1E1E1E))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Apne dosto ko app share karein. Jaise hi wo install karenge aur pehla match khelenge, dono ko ₹10 Match Discount Bonus milega!",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEEEEEE),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                "YOUR CODE: $referCode",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFF1E1E1E),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Bhai Suri Esports download kar aur Free Fire / Solo 1v1 custom tournaments me prize jeet! Mera referral code: $referCode")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share App Link"))
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
fun SupportScreenView() {
    val context = LocalContext.current
    val adminPhone = "+260953484261"
    val tgLink = "https://t.me/divineofpsycho"
    val waChannelLink = "https://whatsapp.com/channel/0029Vb90behJuyAGTTbZY53z"
    val instaUser = "siuuu_xri"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "ADMIN OFFICIAL SUPPORT 🪽",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Koi bhi tournament, payment ya app problem ho toh direct Admin se sampark karein.",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // WhatsApp Channel
        item {
            SupportActionCard(
                title = "Join WhatsApp Channel",
                subtitle = "DIVINE OF PSYCHO 🪽 Official Channel",
                badgeText = "OFFICIAL UPDATES",
                badgeColor = Color(0xFF25D366),
                icon = Icons.Default.Notifications,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(waChannelLink))
                    context.startActivity(intent)
                }
            )
        }

        // Telegram Channel
        item {
            SupportActionCard(
                title = "Join Telegram Channel",
                subtitle = "@divineofpsycho (Custom Room ID & Pass)",
                badgeText = "MATCH ROOMS",
                badgeColor = Color(0xFF0088CC),
                icon = Icons.Default.Send,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tgLink))
                    context.startActivity(intent)
                }
            )
        }

        // Direct WhatsApp Chat with Admin
        item {
            SupportActionCard(
                title = "Direct Admin WhatsApp & Call",
                subtitle = adminPhone,
                badgeText = "24x7 HELPLINE",
                badgeColor = Color(0xFF4CAF50),
                icon = Icons.Default.Call,
                onClick = {
                    val cleanPhone = adminPhone.replace("+", "").trim()
                    val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=Hello%20Admin%20Suri%20Esports,%20I%20need%20help."))
                    try {
                        context.startActivity(waIntent)
                    } catch (e: Exception) {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$adminPhone"))
                        context.startActivity(dialIntent)
                    }
                }
            )
        }

        // Instagram Handle
        item {
            SupportActionCard(
                title = "Follow on Instagram",
                subtitle = "@$instaUser",
                badgeText = "INSTAGRAM",
                badgeColor = Color(0xFFE1306C),
                icon = Icons.Default.Person,
                onClick = {
                    val instaIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/_u/$instaUser"))
                    instaIntent.setPackage("com.instagram.android")
                    try {
                        context.startActivity(instaIntent)
                    } catch (e: Exception) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/$instaUser")))
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SupportActionCard(
    title: String,
    subtitle: String,
    badgeText: String,
    badgeColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = badgeColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        badgeText,
                        color = badgeColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E1E1E))
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.LightGray)
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
