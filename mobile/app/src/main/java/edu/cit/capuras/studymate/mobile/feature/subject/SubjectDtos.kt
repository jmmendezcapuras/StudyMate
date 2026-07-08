package edu.cit.capuras.studymate.mobile.feature.subject

data class SubjectRequest(
    val name: String
)

data class SubjectResponse(
    val id: Long,
    val name: String
)
