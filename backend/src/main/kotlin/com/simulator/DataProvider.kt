package com.simulator

import reactor.core.publisher.Flux

interface DataProvider {
    fun subscribe(sourceId: String): Flux<DataPackage>
}