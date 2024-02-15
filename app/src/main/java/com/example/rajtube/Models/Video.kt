package com.example.rajtube.Models

 class Video {
    var videoUrl: String = ""
    var tittle: String = ""
    var thumbnail: String = ""
    var email: String = ""
    var time: String = ""
     var like : Int = 0
     var disLike : Int = 0
     var views : Int = 0

    constructor()
    constructor(videoUrl: String, tittle: String, thumbnail: String, email: String) {
        this.videoUrl = videoUrl
        this.tittle = tittle
        this.thumbnail = thumbnail
        this.email = email
    }

    constructor(videoUrl: String, tittle: String, thumbnail: String, email: String, time: String) {
        this.videoUrl = videoUrl
        this.tittle = tittle
        this.thumbnail = thumbnail
        this.email = email
        this.time = time
    }

     constructor(
         videoUrl: String,
         tittle: String,
         thumbnail: String,
         email: String,
         time: String,
         like: Int,
         disLike: Int,
         views : Int
     ) {
         this.videoUrl = videoUrl
         this.tittle = tittle
         this.thumbnail = thumbnail
         this.email = email
         this.time = time
         this.like = like
         this.disLike = disLike
         this.views = views
     }


 }