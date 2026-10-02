package com.surixesports.app
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

private const val API="http://10.0.2.2:8080/api"
class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);setContent{App()}}}
@Composable fun App(){var screen by remember{mutableStateOf("home")}; Scaffold(topBar={TopAppBar(title={Text("Suri Esports")})},bottomBar={NavigationBar{listOf("home" to "Home","tournaments" to "Tournaments","profile" to "Profile").forEach{(id,n)->NavigationBarItem(selected=screen==id,onClick={screen=id},icon={},label={Text(n)})}}}){p->when(screen){"home"->Home(p){screen="tournaments"};"tournaments"->Tournaments(p);else->Profile(p)}}}
@Composable fun Home(p:PaddingValues,go:()->Unit){Column(Modifier.padding(p).padding(20.dp)){Text("Suri Esports",style=MaterialTheme.typography.headlineMedium);Spacer(Modifier.height(10.dp));Text("Play • Compete • Win");Spacer(Modifier.height(20.dp));Button(onClick=go){Text("Browse Tournaments")}}}
@Composable fun Tournaments(p:PaddingValues){var data by remember{mutableStateOf(listOf<String>())};LaunchedEffect(Unit){data=withContext(Dispatchers.IO){try{val r=OkHttpClient().newCall(Request.Builder().url(API+"/tournaments").build()).execute();val a=JSONArray(r.body?.string()? :"[]");List(a.length()){i->val o=a.getJSONObject(i);"${o.optString("name")} • ₹${o.optInt("entry_fee")} • ${o.optString("status")}"}}catch(e:Exception){listOf("Backend not connected — start backend on port 8080")}}};LazyColumn(Modifier.padding(p).padding(16.dp)){items(data){Card(Modifier.fillMaxWidth().padding(vertical=6.dp)){Text(it,Modifier.padding(18.dp))}}}}
@Composable fun Profile(p:PaddingValues){Column(Modifier.padding(p).padding(20.dp)){Text("Profile",style=MaterialTheme.typography.headlineSmall);Text("Registration/payment status is stored by the secure backend.")}}
