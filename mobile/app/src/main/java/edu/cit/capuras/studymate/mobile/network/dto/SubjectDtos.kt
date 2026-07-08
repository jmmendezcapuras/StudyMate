package edu.cit.capuras.studymate.mobile.network.dto

/** Mirrors edu.cit.capuras.studymate.dto.SubjectRequest on the backend. */
data class SubjectRequest(
    val name: String
)

/** Mirrors edu.cit.capuras.studymate.model.Subject as returned by the API. */
data class SubjectResponse(
    val id: Long,
    val name: String
)
