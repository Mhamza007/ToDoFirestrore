package com.mhamza007.todofirestrore.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mhamza007.todofirestrore.MainActivity
import com.mhamza007.todofirestrore.R
import com.mhamza007.todofirestrore.model.ToDo

class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
    View.OnCreateContextMenuListener {

    lateinit var itemClickListener: ItemClickListener
    var itemTitle: TextView
    var itemDescription: TextView

    init {
        itemView.setOnClickListener(this)
        itemView.setOnCreateContextMenuListener(this)

        itemTitle = itemView.findViewById(R.id.item_title)
        itemDescription = itemView.findViewById(R.id.item_description)
    }


    override fun onClick(p0: View?) {
        itemClickListener.onClick(p0, adapterPosition, false)
    }

    override fun onCreateContextMenu(
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0?.setHeaderTitle("Select Action")
        p0?.add(0, 0, adapterPosition, "DELETE")
    }

}

class ListItemAdapter : RecyclerView.Adapter<ListItemViewHolder> {

    lateinit var activity: MainActivity
    lateinit var todoList: List<ToDo>

    constructor(activity: MainActivity, todoList: List<ToDo>) : super() {
        this.activity = activity
        this.todoList = todoList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ListItemViewHolder(view)

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        //set data for item
        holder.itemTitle.text = todoList[position].title
        holder.itemDescription.text = todoList[position].description

        holder.itemClickListener = object : ItemClickListener {
            override fun onClick(view: View?, position: Int, isLingClick: Boolean) {
                activity.title.setText(todoList[position].title)
                activity.description.setText(todoList[position].description)

                activity.isUpdate = true // set flag is update = true
                activity.idUpdate = todoList.get(position).id
            }
        }
    }
}