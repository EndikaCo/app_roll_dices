package com.endcodev.roll_dices.presenter.dice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endcodev.roll_dices.databinding.DiceHolderBinding

class DicesAdapter(private var list: MutableList<Int>) :
    RecyclerView.Adapter<DicesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: DiceHolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            DiceHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return (ViewHolder(binding))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]
        holder.binding.holderRandName.text = currentItem.toString()

        if (position == list.size - 1) {
            holder.binding.holderArrow.visibility = View.VISIBLE
        } else {
            holder.binding.holderArrow.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun swapData() {
        if (list.size > 10) {
            list.removeAt(0)
            notifyItemRemoved(0)
        }
        else
            notifyItemInserted(list.size)
        for ((index) in list.withIndex()) {
            notifyItemChanged(index)
        }
    }
}