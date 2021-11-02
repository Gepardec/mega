package com.gepardec.mega.application.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.reflect.Member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggerProducerTest {

    @Mock
    private InjectionPoint ip;

    @Mock
    private Bean<?> bean;

    @Mock
    private Member member;

    private LoggerProducer producer;

    @BeforeEach
    void beforeEach() {
        producer = new LoggerProducer();
    }

    @Test
    void createLogger_whenBeanAvailable_thenBeanClassIsUsed() {
        Class<?> clazz = Object.class;
        OngoingStubbing<Class<?>> beanClassStubbing = when(bean.getBeanClass());
        beanClassStubbing.thenReturn(clazz);
        OngoingStubbing<Bean<?>> beanStubbing = when(ip.getBean());
        beanStubbing.thenReturn(bean);

        final Logger logger = producer.createLogger(ip);

        assertEquals(Object.class.getName(), logger.getName());
    }

    @Test
    void createLogger_whenBeanNull_thenMemberDeclaringClassIsUsed() {
        Class<?> clazz = Object.class;
        OngoingStubbing<Class<?>> memberClassStubbing = when(member.getDeclaringClass());
        memberClassStubbing.thenReturn(clazz);
        when(ip.getMember()).thenReturn(member);

        final Logger logger = producer.createLogger(ip);

        assertEquals(Object.class.getName(), logger.getName());
    }

    @Test
    void createLogger_whenBeanAndMemberNull_thenDefaultNameIsUsed() {
        when(ip.getBean()).thenReturn(null);
        when(ip.getMember()).thenReturn(null);

        final Logger logger = producer.createLogger(ip);

        assertEquals("default", logger.getName());
    }
}
