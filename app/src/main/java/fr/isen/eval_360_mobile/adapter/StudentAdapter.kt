package fr.isen.eval_360_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.allclasse.Student

class StudentAdapter(private var students: List<Student>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_STUDENT = 0
    private val VIEW_TYPE_NOTATION_ELEVE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_STUDENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
                StudentViewHolder(view)
            }
            VIEW_TYPE_NOTATION_ELEVE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notation_eleve, parent, false)
                NotationEleveViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_STUDENT -> {
                val studentHolder = holder as StudentViewHolder
                val student = students[position]
                studentHolder.bind(student)
            }
            VIEW_TYPE_NOTATION_ELEVE -> {
                val notationHolder = holder as NotationEleveViewHolder
                val student = students[position]
                notationHolder.bind(student)
            }
        }
    }

    override fun getItemCount() = students.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_STUDENT
        } else {
            VIEW_TYPE_NOTATION_ELEVE
        }
    }

    fun setStudents(students: List<Student>) {
        this.students = students
        notifyDataSetChanged()
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(student: Student) {
            itemView.findViewById<TextView>(R.id.textViewStudent).text = student.name
        }
    }

    inner class NotationEleveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNomEleve: TextView = itemView.findViewById(R.id.textViewNomEleveANoter)
        private val buttonNoter: Button = itemView.findViewById(R.id.buttonNoter)

        fun bind(student: Student) {
            textViewNomEleve.text = student.name
            buttonNoter.setOnClickListener {
                // Gérer la logique pour rediriger vers "DetailNotationActivity"
            }
        }
    }
}

