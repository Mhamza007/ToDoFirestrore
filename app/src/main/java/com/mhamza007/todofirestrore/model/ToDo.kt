package com.mhamza007.todofirestrore.model

class ToDo {
    lateinit var id: String
    lateinit var title: String
    lateinit var description: String
    lateinit var time : Any

    constructor()

    constructor(id: String, title: String, description: String, time : Any) {
        this.id = id
        this.title = title
        this.description = description
        this.time = time
    }
}