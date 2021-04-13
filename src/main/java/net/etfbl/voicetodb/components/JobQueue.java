package net.etfbl.voicetodb.components;

import net.etfbl.voicetodb.models.Job;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;

@Component
public class JobQueue extends ArrayDeque<Job> {

}
