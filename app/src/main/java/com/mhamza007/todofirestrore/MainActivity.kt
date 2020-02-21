package com.mhamza007.todofirestrore

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mhamza007.todofirestrore.adapter.ListItemAdapter
import com.mhamza007.todofirestrore.model.ToDo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var todoList: ArrayList<ToDo> = ArrayList()
    private lateinit var db: FirebaseFirestore

    private lateinit var listItem: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var title: EditText
    lateinit var description: EditText

    var isUpdate = false //flag to check is update or is add new
    var idUpdate = "" //Id of the item to update

    lateinit var adapter: ListItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init Firestore
        db = FirebaseFirestore.getInstance()

        //View
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)

        fab.setOnClickListener {
            //Add New
            if (!isUpdate)
                setData(title.text.toString().trim(), description.text.toString().trim())
            else {
                updateData(title.text.toString().trim(), description.text.toString().trim())
                isUpdate = !isUpdate // reset flag
            }
        }

        listItem = findViewById(R.id.list_todo)
        listItem.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        listItem.layoutManager = layoutManager

        loadData() // Load Date From Firestore

    }

    private fun updateData(title: String, description: String) {
        db.collection("ToDoList").document(idUpdate)
            .update(
                "title", title,
                "description", description,
                "time", System.currentTimeMillis()
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
            }

        //Realtime update refresh data
        db.collection("ToDoList").document(idUpdate)
            .addSnapshotListener { p0, p1 -> loadData() }
    }

    private fun setData(title: String, description: String) {
        //Random Id
        val id = UUID.randomUUID().toString()
        db.collection("ToDoList").document(id)
            .set(ToDo(id, title, description, System.currentTimeMillis()))
            .addOnSuccessListener {
                //Refresh data
                loadData()
            }
    }

    private fun loadData() {
        if (todoList.size > 0)
            todoList.clear() // remove old value
        db.collection("ToDoList")
            .get()
            .addOnCompleteListener {
                for (doc in it.result!!) {
                    val todo = ToDo(
                        doc.getString("id")!!,
                        doc.getString("title")!!,
                        doc.getString("description")!!,
                        doc.get("time")!!
                    )
                    todoList.add(todo)

                    val time =  doc.get("time").toString()
                    val timeinLont = time.toLong()
                    todoList.sortBy { timeinLont }
                }
                adapter = ListItemAdapter(this, todoList)
                listItem.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error : ${it.message}")
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title == "DELETE")
            deleteItem(item.order)
        return super.onContextItemSelected(item)
    }

    private fun deleteItem(index: Int) {
        db.collection("ToDoList").document(todoList[index].id).delete()
            .addOnSuccessListener {
                loadData()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error Deleting", Toast.LENGTH_SHORT).show()
            }
    }
}