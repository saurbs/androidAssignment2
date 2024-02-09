package com.example.sauravgautam_assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.sauravgautam_assignment2.ui.theme.SauravGautam_Assignment2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SauravGautam_Assignment2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = DataStoreContacts(context)




    var contactName by remember { mutableStateOf("") }
    var contactNum by remember { mutableStateOf("") }
    var contactsList by remember { mutableStateOf(listOf("")) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 100.dp)) {


            TextField(
                value = contactName,
                onValueChange = { contactName = it },
                label = {
                    Text("Contact Name")
                },
                modifier = Modifier.padding(bottom = 20.dp)
            )







            TextField(
                value = contactNum,
                onValueChange = { contactNum = it },
                label = {
                    Text("Contact No")
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 20.dp)) {
            Button(onClick = {
                scope.launch {
                    contactsList = listOf("")
                    dataStore.getContactsData()
                        .flowOn(Dispatchers.IO)
                        .collect { dataList ->
                            contactsList = dataList.map { data ->
                                "${data.name} ${data.num}"
                            }
                        }
                }
            },
                modifier = Modifier
                    .width(150.dp)
                    .padding(10.dp)
            ) {
                Text("Load")
            }




            Button(onClick = {
                scope.launch {
                    contactsList = listOf("")
                    if (contactName != "" && contactNum != "") {
                        dataStore.saveContactsData(ContactsData(contactName, contactNum))
                    }
                }
            },
                modifier = Modifier
                    .width(150.dp)
                    .padding(10.dp)
            ) {
                Text("Save")
            }
        }









        Text("Contacts", modifier = Modifier.offset(x = -100.dp))
        ScrollableTextList(contactsList)


        Button(onClick = {
            scope.launch {
                contactsList = listOf()
                dataStore.clearContactsData()
            }
        },
            modifier = Modifier
                .width(150.dp)
                .padding(10.dp)
        ) {
            Text("Clear")
        }



        Box(
            modifier = Modifier.padding(top = 60.dp)
        ) {
            Text(
                "Name: Saurav Gautam\nCollege ID: 301286980",
                modifier = Modifier
                    .border(1.dp, Color.Blue)
                    .padding(8.dp)
            )
        }
    }
}






@Composable
fun ScrollableTextList(textList: List<String>) {
    LazyColumn(modifier = Modifier
        .padding(top = 20.dp)
        .height(200.dp)) {
        items(textList) { text ->
            Text(text)
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SauravGautam_Assignment2Theme {
        MainScreen()
    }
}
