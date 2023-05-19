package fr.isen.eval_360_mobile.allclasse

interface StudentAdapterListener {
    fun onStudentDataChanged(students: List<Student>)
    fun onRemoveClicked(student: Student)
}
