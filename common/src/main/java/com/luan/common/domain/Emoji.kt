package com.luan.common.domain


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Emoji(
    @PrimaryKey var key: String,
     var source: String
)