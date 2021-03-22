package net.etfbl.voicetodb.components;

import java.util.ArrayDeque;
import java.util.Queue;
import net.etfbl.voicetodb.models.Job;
import org.springframework.stereotype.Component;

@Component
public class JobQueue {

   private final Queue<Job> queue = new ArrayDeque<>();

   public void submit(Job job) {
      queue.add(job);
   }

   public boolean hasJobs() {
      return !queue.isEmpty();
   }

   public Job getJob() {
      return queue.poll();
   }

}
