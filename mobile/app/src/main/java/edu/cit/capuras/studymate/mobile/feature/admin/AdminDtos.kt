package edu.cit.capuras.studymate.mobile.feature.admin

data class UserSummaryResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
)

data class CreateAdminRequest(
    val username: String,
    val email: String,
    val password: String
)
