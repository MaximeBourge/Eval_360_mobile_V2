package fr.isen.eval_360_mobile.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R


class GroupAdapter(private var groups: List<String>, private val listener: OnItemClickListener? = null) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val groupName = groups[position]
        holder.groupNameButton.text = groupName
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val groupNameButton: Button = itemView.findViewById(R.id.groupButton)

        init {
            groupNameButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val groupName = groups[position]
                listener?.onItemClick(groupName)
            }
        }
    }

    fun setGroups(groups: List<String>) {
        this.groups = groups
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(groupName: String)
    }

}
