package edu.cit.capuras.studymate.mobile.feature.session

import edu.cit.capuras.studymate.mobile.feature.subject.SubjectResponse

data class SessionRequest(
    val subjectId: Long,
    val durationMinutes: Int,
    val sessionDate: String, // ISO yyyy-MM-dd
    val notes: String?
)

data class SessionResponse(
    val id: Long,
    val subject: SubjectResponse,
    val durationMinutes: Int,
    val sessionDate: String,
    val notes: String?
)
