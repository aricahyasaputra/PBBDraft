package com.example.pbbdraft.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbbdraft.databinding.AdapterPbbBinding
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBB


class PBBAdapter (private val pajaks: ArrayList<PBB>, private val listener: OnAdapterListener, private val intent_type:Int)
    : RecyclerView.Adapter<PBBAdapter.pajakViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pajakViewHolder {
        val binding = AdapterPbbBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return pajakViewHolder(binding)
    }

    override fun getItemCount() = pajaks.size
    override fun onBindViewHolder(holder: pajakViewHolder, position: Int) {
        val pajak = pajaks[position]

        holder.binding.namaPemilik.text = pajak.namaWajibPajak
        holder.binding.NOP.text = "NOP: " + pajak.NOP
        holder.binding.luasTanah.text = "Luas : " + pajak.luasObjekPajak.toString()

        holder.binding.namaPemilik.setOnClickListener{
            listener.onClik( pajak )
        }

        when (this.intent_type){
            Constant.TYPE_EKSPORT -> {
                holder.binding.iconDelete.visibility = View.GONE
                holder.binding.iconEdit.visibility = View.GONE
            }
            Constant.TYPE_UPDATE -> {
                holder.binding.iconEdit.setOnClickListener{
                    listener.onUpdate( pajak )
                }
                holder.binding.iconDelete.setOnClickListener{
                    listener.onDelete( pajak )
                }
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