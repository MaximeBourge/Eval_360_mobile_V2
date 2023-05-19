package fr.isen.eval_360_mobile.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.allclasse.Project


class ProjectAdapter(private var projects: List<Project>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentProject = projects[position]
        holder.projectButton.text = currentProject.name
    }

    override fun getItemCount() = projects.size

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val projectButton: Button = itemView.findViewById(R.id.projectButton)

        init {
            projectButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val project = projects[position]
                listener.onItemClick(project)
            }
        }
    }

    fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(project: Project)
    }
}
