package org.apache.zookeeper.gx;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {

    String node;
    DataMonitor dm;
    ZooKeeper zk;
    String fileName;
    String exec[];
    Process child;

    public Executor(String hostPort, String znode, String filename, String exec[]) throws IOException {
        this.fileName = filename;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, znode, null, this);
    }

    public static void main(String[] args) {
        if (args.length < 4){
            System.err.println("USAGE: Executor hostPort znode filename programe [args ...]");
            System.exit(2);
        }

        String hostPort = args[0];
        String znode = args[1];
        String filename = args[2];
        String exec[] = new String[args.length - 3];
        System.arraycopy(args,3, exec, 0, exec.length);
        try {
            new Executor(hostPort, znode, filename, exec).run();
        } catch (Exception e){

        }
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    //todo
                    wait();
                }
            }
        } catch (InterruptedException e){

        }
    }

    /**
     * process WatchEvent
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {

    }

    @Override
    public void exists(byte[] data) {

    }

    @Override
    public void closing(int rc) {
        synchronized (this){
            notifyAll();
        }
    }
}
