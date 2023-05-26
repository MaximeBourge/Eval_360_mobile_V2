package fr.isen.eval_360_mobile.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.allclasse.Student
import fr.isen.eval_360_mobile.allclasse.StudentAdapterListener
import fr.isen.eval_360_mobile.studentView.DetailNotation

class MarkAdapter(private var students: List<Student>,
                     ) : RecyclerView.Adapter<MarkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notation_eleve2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    override fun getItemCount() = students.size

    fun setStudents(students: List<Student>) {
        this.students = students
        notifyDataSetChanged()
        //listener?.onStudentDataChanged(students)
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(student: Student) {
            itemView.findViewById<Button>(R.id.buttonNote).apply {
                text = student.mark.toString()
                setOnClickListener {
                    // Redirection vers la page "DetailNotation" pour l'élève sélectionné
                    val intent = Intent(itemView.context, DetailNotation::class.java)
                    intent.putExtra("studentId", student.id) // Passer l'ID de l'élève si nécessaire
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}