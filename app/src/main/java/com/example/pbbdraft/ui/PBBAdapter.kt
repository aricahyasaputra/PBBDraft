package com.example.pbbdraft.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.databinding.AdapterPbbBinding

class PBBAdapter (private val pajaks: ArrayList<PBB>, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<PBBAdapter.pajakViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pajakViewHolder {
        val binding = AdapterPbbBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return pajakViewHolder(binding)
    }

    override fun getItemCount() = pajaks.size
    override fun onBindViewHolder(holder: pajakViewHolder, position: Int) {
        with(holder){
            val pajak = pajaks[position]

            holder.binding.textTitle.text = pajak.nama
            holder.binding.NOP.text = pajak.NOP
            holder.binding.Alamat.text = pajak.alamat
            holder.binding.luasTanah.text = pajak.luasTanah.toString()
            holder.binding.pajakDitetapkan.text = pajak.pajakDitetapkan.toString()
            holder.binding.textTitle.setOnClickListener{
                listener.onClik( pajak )
            }
            holder.binding.iconEdit.setOnClickListener{
                listener.onUpdate( pajak )
            }
            holder.binding.iconDelete.setOnClickListener{
                listener.onDelete( pajak )
            }
        }

    }
    class pajakViewHolder(val binding: AdapterPbbBinding) : RecyclerView.ViewHolder(binding.root)
    fun setData(list: List<PBB>){
        pajaks.clear()
        pajaks.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener{
        fun onClik(pajak : PBB)
        fun onUpdate(pajak : PBB)
        fun onDelete(pajak : PBB)
    }
}