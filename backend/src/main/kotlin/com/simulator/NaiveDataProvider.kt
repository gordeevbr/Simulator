package com.simulator

import com.simulator.math.Generator
import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class NaiveDataProvider(private val generator: Generator): DataProvider {

    private val flux = DirectProcessor.create<Long>()
            .let { processor -> processor.publish().autoConnect()
                    .map { DataPackage("NaiveDataProvider", generator.valueAt(it), it) } to
                    processor.sink()
            }

    private val scheduler = Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat("naive-data-provider-%d")
                    .build()
    )

    init {
        scheduler.scheduleAtFixedRate(
                { flux.second.next(Instant.now().toEpochMilli()) },
                100L,
                100L,
                TimeUnit.MILLISECONDS
        )
    }

    override fun subscribe(sourceId: String): Flux<DataPackage> = flux.first
}