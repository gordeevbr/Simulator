package com.simulator

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/1.0/data")
class DataController(private val dataProvider: DataProvider) {

    @PostMapping("/{ds}", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getData(@PathVariable("ds") source: String) = dataProvider.subscribe(source)
}