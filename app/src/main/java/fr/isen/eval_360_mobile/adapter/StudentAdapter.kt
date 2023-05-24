package fr.isen.eval_360_mobile.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.allclasse.Student
import fr.isen.eval_360_mobile.allclasse.StudentAdapterListener

class StudentAdapter(private var students: List<Student>,
                     ) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
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
            //Mettre ici, des "variables" à mettre dans la recyclerView
            itemView.findViewById<TextView>(R.id.textViewStudent).text = student.name
            //itemView.findViewById<TextView>(R.id.textViewMark).text = student.mark.toString()  --> mettre une textView "note/20"
        }
    }
}