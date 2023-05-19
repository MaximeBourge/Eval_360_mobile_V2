package fr.isen.eval_360_mobile.allclasse

import java.io.Serializable

data class Student(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val mark: Int = 0
) : Serializable