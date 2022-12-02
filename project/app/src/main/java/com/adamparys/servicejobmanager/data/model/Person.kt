package com.adamparys.servicejobmanager.data.model

class Person {
    data class PersonItem(
        val fistName: String? = "",
        val lastName : String? = "",
        val phoneNumber : String? = "",
        val jobType : String? = "",
        val email : String? = "",
        val id : String?
    )
}