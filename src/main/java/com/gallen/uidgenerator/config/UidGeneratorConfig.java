package com.gallen.uidgenerator.config;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.buffer.RejectedPutBufferHandler;
import com.baidu.fsg.uid.buffer.RejectedTakeBufferHandler;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述
 *
 * @author Gallen - 2020/4/1
 */
@Configuration
@Slf4j
public class UidGeneratorConfig {

    @Value("${timeBits}")
    private int timeBits;
    @Value("${workerBits}")
    private int workerBits;
    @Value("${seqBits}")
    private int seqBits;
    @Value("${epochDate}")
    private String epochDate;
    @Value("${cache.boostPower}")
    private int boostPower;
    @Value("${cache.paddingFactor}")
    private int paddingFactor;
    @Value("${scheduleInterval}")
    private long scheduleInterval;

    @Bean
    @ConditionalOnMissingBean(WorkerIdAssigner.class)
    public WorkerIdAssigner workerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    @ConditionalOnMissingBean(RejectedPutBufferHandler.class)
    public RejectedPutBufferHandler rejectedPutBufferHandler() {
        return (ringBuffer, uid) -> log.warn("Rejected putting buffer for uid:{}. {}", uid, ringBuffer);
    }

    @Bean
    @ConditionalOnMissingBean(RejectedTakeBufferHandler.class)
    public RejectedTakeBufferHandler rejectedTakeBufferHandler() {
        return (ringBuffer) -> {
            log.warn("Rejected take buffer. {}", ringBuffer);
            throw new RuntimeException("Rejected take buffer. " + ringBuffer);
        };
    }

    @Bean
    public UidGenerator uidGenerator() {
        CachedUidGenerator uidGenerator = new CachedUidGenerator();
        uidGenerator.setWorkerIdAssigner(workerIdAssigner());
        uidGenerator.setTimeBits(timeBits);
        uidGenerator.setWorkerBits(workerBits);
        uidGenerator.setSeqBits(seqBits);
        uidGenerator.setEpochStr(epochDate);
        uidGenerator.setBoostPower(boostPower);

        if (scheduleInterval != 0) {
            uidGenerator.setScheduleInterval(scheduleInterval);
        }

        // 拒绝策略: 当环已满, 无法继续填充时
        uidGenerator.setRejectedPutBufferHandler(rejectedPutBufferHandler());
        // 拒绝策略: 当环已空, 无法继续获取时
        uidGenerator.setRejectedTakeBufferHandler(rejectedTakeBufferHandler());
        // 还有一些可选配置，此处使用默认值
        return uidGenerator;
    }
}
