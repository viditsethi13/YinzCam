package com.example.yinzcamassessment.data.remote.dto

enum class GameStatus() {
    FINAL(),
    BYE(),
    SCHEDULED();

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