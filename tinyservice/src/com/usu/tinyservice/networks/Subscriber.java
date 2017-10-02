package com.usu.tinyservice.networks;

import org.zeromq.ZMQ;

/**
 * Created by minhld on 8/4/2016.
 */

public class Subscriber extends Thread {
    private String groupIp = "*";
    private int port = Utils.BROKER_SUBSCRIBE_PORT;
    private String[] topics = new String[] { "" };
    private MessageListener mListener;

    public void setMessageListener(MessageListener _listener) {
        this.mListener = _listener;
    }

    public Subscriber() {
        this.start();
    }

    public Subscriber(String _groupIp) {
        this.groupIp = _groupIp;
        this.start();
    }

    public Subscriber(String _groupIp, int _port) {
        this.groupIp = _groupIp;
        this.port = _port;
        this.start();
    }

    public Subscriber(String _groupIp, String[] _topics) {
        this.groupIp = _groupIp;
        this.topics = _topics;
        this.start();
    }

    public Subscriber(String _groupIp, int _port, String[] _topics) {
        this.groupIp = _groupIp;
        this.port = _port;
        this.topics = _topics;
        this.start();
    }

    public void run() {
        try {
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
            String bindGroupStr = "tcp://" + this.groupIp + ":" + this.port;
            subscriber.connect(bindGroupStr);

            // subscribe all the available topics
            for (int i = 0; i < this.topics.length; i++) {
                subscriber.subscribe(topics[i].getBytes());
            }

            // loop until the thread is disposed
            String topic;
            byte[] msg = null;
            while (!Thread.currentThread().isInterrupted()) {
                topic = subscriber.recvStr();
                msg = subscriber.recv();
                if (this.mListener != null) {
                    this.mListener.msgReceived(topic, msg);
                }
            }

            subscriber.close();
            context.term();
        } catch (Exception e) {
            // exception there - leave it for now
            e.printStackTrace();
        }
    }

    public interface MessageListener {
        public void msgReceived(String topic, byte[] msg);
    }
}
