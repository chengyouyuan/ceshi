package com.winhxd.b2c.common.mq.event;

/**
 * 事件消息服务,事件发送方必须实现该服务<br/>
 * 对应方法
 * {@link com.winhxd.b2c.common.mq.event.EventMessageSender#send(EventType, String, Object)}
 *
 * @author lixiaodong
 */
public interface EventTypeService<T> {
    /**
     * 发送事件消息前调用, 用于保存事件数据对象
     *
     * @param eventId     当前类型事件唯一标识
     * @param eventObject 事件数据对象
     */
    void saveEvent(String eventId, T eventObject);

    /**
     * 事件消息发送成功后调用
     *
     * @param eventId 当前类型事件唯一标识
     */
    void onEventSentSuccess(String eventId);

    /**
     * 返回当前service处理的事件类型
     *
     * @return
     */
    EventType getEventType();
}
