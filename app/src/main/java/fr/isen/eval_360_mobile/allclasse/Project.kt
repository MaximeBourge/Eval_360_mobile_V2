package fr.isen.eval_360_mobile.allclasse

import java.io.Serializable

data class Project (
    val id: String = "",
    val name: String = "",
    val groupList: List<Group>? = null
) : Serializable