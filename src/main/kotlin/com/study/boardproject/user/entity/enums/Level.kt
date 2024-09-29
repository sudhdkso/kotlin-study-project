package com.study.boardproject.user.entity.enums

enum class Level(val value:Int) {
    EMPTY(-1),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    MANAGER(5);

    companion object {
        fun fromLevel(level: Int) : Level {
            return values()
                .find { it.value == level }
                ?: EMPTY
        }
    }
}