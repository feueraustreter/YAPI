// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.worker;

public class Worker extends Thread {

    private Task task = null;
    private boolean run = true;
    private WorkerPool workerPool;
    private boolean delete = false;

    /**
     * @since Version 1.1
     *
     * @param workerPool
     */
    public Worker(ThreadGroup threadGroup, WorkerPool workerPool) {
        super(threadGroup, "");
        this.workerPool = workerPool;
        workerPool.done(this);
    }

    @Override
    public final synchronized void run() {
        while (run) {
            while (task == null) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (!run) {
                    break;
                }
            }
            if (task == null) {
                continue;
            }
            try {
                task.run();
            } catch (Exception e) {
                workerPool.log(getName().substring(13) + " > Task exited with and exception\n" + e.toString());
            }
            task = null;
            workerPool.done(this);
        }
    }

    final synchronized void finish() {
        run = false;
        notifyAll();
    }

    final synchronized void setTask(Task task) {
        if (delete) {
            workerPool.work(task);
            workerPool.delete(this);
            return;
        }
        this.task = task;
        notifyAll();
    }

    final void delete() {
        delete = true;
        finish();
    }

    @Override
    public String toString() {
        return "Worker{" +
                getName() +
                ", hasTask=" + (task != null) +
                '}';
    }
}