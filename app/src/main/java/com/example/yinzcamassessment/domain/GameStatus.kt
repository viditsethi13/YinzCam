package com.example.yinzcamassessment.domain

enum class GameStatus(status: String) {
    FINAL("F"),
    BYE("B"),
    SCHEDULED("S");

    companion object {
        fun fromType(type: String): GameStatus {
            return when (type) {
                "F" -> FINAL
                "S" -> SCHEDULED
                else -> BYE
            }
        }
    }
}