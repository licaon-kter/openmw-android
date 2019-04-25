package ui.activity

import android.graphics.Color
import com.libopenmw.openmw.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import java.util.*
import android.view.View
import android.support.v7.widget.LinearLayoutManager




class RecyclerViewAdapter(private val data: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(), ItemMoveCallback.ItemTouchHelperContract {

    inner class MyViewHolder(internal var rowView: View) : RecyclerView.ViewHolder(rowView) {

        val mTitle: TextView = rowView.findViewById(R.id.txtTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mod_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = data[position]
    }


    override fun getItemCount(): Int {
        return data.size
    }


    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: MyViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY)

    }

    override fun onRowClear(myViewHolder: MyViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE)
    }
}

class ItemMoveCallback(private val mAdapter: ItemTouchHelperContract) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        mAdapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?,
                                   actionState: Int) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is RecyclerViewAdapter.MyViewHolder) {
                mAdapter.onRowSelected(viewHolder)
            }

        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?,
                           viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is RecyclerViewAdapter.MyViewHolder) {
            mAdapter.onRowClear(viewHolder)
        }
    }

    interface ItemTouchHelperContract {

        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(myViewHolder: RecyclerViewAdapter.MyViewHolder)
        fun onRowClear(myViewHolder: RecyclerViewAdapter.MyViewHolder)

    }

}


class ModsActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: RecyclerViewAdapter
    var stringArrayList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mods)

        recyclerView = findViewById(R.id.mods_list)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        stringArrayList.add("Item 1")
        stringArrayList.add("Item 2")
        stringArrayList.add("Item 3")
        stringArrayList.add("Item 4")
        stringArrayList.add("Item 5")
        stringArrayList.add("Item 6")
        stringArrayList.add("Item 7")
        stringArrayList.add("Item 8")
        stringArrayList.add("Item 9")
        stringArrayList.add("Item 10")
        stringArrayList.add("Item 11")
        stringArrayList.add("Item 12")
        stringArrayList.add("Item 13")

        mAdapter = RecyclerViewAdapter(stringArrayList)

        val callback = ItemMoveCallback(mAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = mAdapter
    }
}