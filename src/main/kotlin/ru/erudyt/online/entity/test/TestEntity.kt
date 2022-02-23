package ru.erudyt.online.entity.test

data class TestEntity(
    /** Идентификатор теста */
    val id: String,
    /** Количество вопросов */
    val max: Int,
    /** Количество возможных вопросов */
    val total: Int,
    /** Тип теста ??? */
    val type: Int,
    /** Навигация ??? */
    val navigation: Int,
    /** Название теста */
    val name: String,
    /** Возрастная категория */
    val age: String,
    /** Перемешивать ли (что???) */
    val shuffle: Boolean,
    /** Все вопросы на одной странице */
    val allInOne: Boolean,
    /** Показывать ответы ??? */
    val showAnswer: Boolean,
    /** ??? */
    val needPreface: Boolean,
    /** Тип диплома */
    val diploma: String,
    /** ??? */
    val diplomaS: String,
    /** ??? */
    val diplomaT: String,
    /** ??? */
    val diplomaC: String,
    /** Текст для диплома */
    val diplomaText: String,
    /** ??? */
    val cost: Int,
    /** Места ??? */
    val places: List<Int>,
    /** Список вопросов */
    val questions: List<QuestionEntity>,
)