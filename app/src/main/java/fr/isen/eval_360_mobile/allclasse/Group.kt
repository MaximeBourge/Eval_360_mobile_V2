package fr.isen.eval_360_mobile.allclasse

import java.io.Serializable


data class Group(
    val id: String = "",
    val nomDuGroup: String = "",
    val students: List<Student> = emptyList()
)  : Serializable
