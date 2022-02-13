package com.example.store.presentation.ui.adapters

import android.view.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ListMainListBinding
import com.example.store.repository.data.entities.MainListData


class MainListInfoAdapter(
    private val popupMenu: (View,MainListData) -> Unit,
    private val listener: (MainListData) -> Unit,
) : RecyclerView.Adapter<MainListInfoAdapter.ViewHolder>() {


    /* private var multiSelect = false
        private var selectItems = arrayListOf<MainListData>()
        */private var actionMode: ActionMode? = null

   /* private val contextualMenu = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            //val inflater = mode!!.menuInflater
            mode!!.menuInflater.inflate(R.menu.contextual_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {

            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.item_delete -> {
                    Toast.makeText(context, "Delete click", Toast.LENGTH_LONG).show()
                    deleteEvent(selectItems.toList())
                    mode!!.finish()
                }
            }
            return true
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            multiSelect = false
            selectItems.clear()
            notifyDataSetChanged()
            Log.i(TAG, "selected item size = ${selectItems.size}")
        }
    }*/

    class ViewHolder(val binding: ListMainListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListMainListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = mainDataList[position]
        holder.binding.apply {
            tvMainListName.text = currentItem.listName
            tvMainListType.text = currentItem.listType

           /* mainList.setBackgroundColor(
                if (selectItems.contains(currentItem)) {
                    Color.CYAN
                } else
                    Color.WHITE
            )*/

            mainList.setOnClickListener {
                /*if (multiSelect) {
                    selectItem(holder, currentItem)
                } else*/
                listener(currentItem)
            }
            ibMore.setOnClickListener {
                popupMenu(it,currentItem)
            }

            /*mainList.setOnLongClickListener {
                if (!multiSelect) {
                    Log.i(TAG, "selected item size = ${selectItems.size}")
                    multiSelect = true
                    actionMode = activity.startActionMode(contextualMenu)
                    selectItem(holder, currentItem)
                    true
                } else {
                    false
                }
            }*/
        }
    }

    override fun getItemCount() = mainDataList.size


    private val diffCalBack = object : DiffUtil.ItemCallback<MainListData>() {
        override fun areItemsTheSame(oldItem: MainListData, newItem: MainListData): Boolean {
            return oldItem.listName == newItem.listName
        }

        override fun areContentsTheSame(oldItem: MainListData, newItem: MainListData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCalBack)
    var mainDataList: List<MainListData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    /*private fun selectItem(holder: MainListInfoAdapter.ViewHolder, currentItem: MainListData) {
        if (selectItems.contains(currentItem)) {
            selectItems.remove(currentItem)
            setContextualMenuText(selectItems.size)
            holder.binding.mainList.setBackgroundColor(Color.WHITE)
        } else {
            selectItems.add(currentItem)
            setContextualMenuText(selectItems.size)
            holder.binding.mainList.setBackgroundColor(Color.CYAN)
        }
    }*/
    private fun setContextualMenuText(listSize: Int) {
        ("$listSize selected").also {
            actionMode!!.title = it
        }
    }
}