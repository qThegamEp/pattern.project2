package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.container.IoStrategy;
import org.glassfish.grizzly.IOStrategy;
import org.glassfish.grizzly.strategies.LeaderFollowerNIOStrategy;
import org.glassfish.grizzly.strategies.SameThreadIOStrategy;
import org.glassfish.grizzly.strategies.SimpleDynamicNIOStrategy;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOStrategyFactoryServiceImpl implements IOStrategyFactoryService {

    private static final Logger LOG = LoggerFactory.getLogger(IOStrategyFactoryServiceImpl.class);

    @Override
    public IOStrategy createIOStrategy(IoStrategy ioStrategy) {
        LOG.info("IO strategy: {}", ioStrategy);
        switch (ioStrategy) {
            case WORKER_IO_STRATEGY:
                return WorkerThreadIOStrategy.getInstance();
            case SAME_IO_STRATEGY:
                return SameThreadIOStrategy.getInstance();
            case DYNAMIC_IO_STRATEGY:
                return SimpleDynamicNIOStrategy.getInstance();
            case LEADER_FOLLOWER_IO_STRATEGY:
            default:
                return LeaderFollowerNIOStrategy.getInstance();
        }
    }
}
