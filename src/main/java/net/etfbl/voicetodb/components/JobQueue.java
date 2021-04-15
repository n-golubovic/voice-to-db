package net.etfbl.voicetodb.components;

import java.util.ArrayDeque;
import net.etfbl.voicetodb.models.Job;
import org.springframework.stereotype.Component;

/**
 * {@code JobQueue} is a simple implementation of Queue for {@link Job} object. This way, it can be used as autowiring
 * component while not having any code overhead.
 */
@Component
public class JobQueue extends ArrayDeque<Job> {

}
